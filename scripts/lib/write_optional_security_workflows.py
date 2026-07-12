#!/usr/bin/env python3
"""Write optional Security Scan + Scorecard workflow files if missing."""
from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
WF = ROOT / ".github" / "workflows"

# trivy-action v0.35.0 — only safe tag after March 2026 supply-chain incident
TRIVY = "57a97c7e7821a5776cebc9bb87c984fa69cba8f1"
# ossf/scorecard-action v2.4.3
SCORECARD = "99c09fe975337306107572b4fdf4db224cf8e2f2"

SECURITY_YML = f"""name: Security Scan

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
  schedule:
    - cron: '0 7 * * 1'
  workflow_dispatch:

permissions:
  contents: read
  security-events: write

jobs:
  trivy:
    name: Trivy
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v7

      - name: Run Trivy vulnerability scanner
        uses: aquasecurity/trivy-action@{TRIVY} # v0.35.0 (immutable; post-incident safe tag)
        with:
          scan-type: fs
          scan-ref: .
          severity: CRITICAL,HIGH
          exit-code: '1'
          ignore-unfixed: true
"""

SCORECARD_YML = f"""name: OpenSSF Scorecard

on:
  branch_protection_rule:
  schedule:
    - cron: '0 8 * * 1'
  push:
    branches: [main]
  workflow_dispatch:

permissions: read-all

jobs:
  analysis:
    name: Scorecard analysis
    runs-on: ubuntu-latest
    permissions:
      security-events: write
      id-token: write
      contents: read
      actions: read
    steps:
      - name: Checkout
        uses: actions/checkout@v7
        with:
          persist-credentials: false

      - name: Run OpenSSF Scorecard
        uses: ossf/scorecard-action@{SCORECARD} # v2.4.3
        with:
          results_file: results.sarif
          results_format: sarif
          publish_results: true

      - name: Upload SARIF
        uses: github/codeql-action/upload-sarif@v4
        with:
          sarif_file: results.sarif
"""


def main() -> int:
    WF.mkdir(parents=True, exist_ok=True)
    for name, body in (
        ("security.yml", SECURITY_YML),
        ("scorecard.yml", SCORECARD_YML),
    ):
        path = WF / name
        if path.exists():
            print(f"SKIP     {path.relative_to(ROOT)} (already present)")
            continue
        path.write_text(body, encoding="utf-8", newline="\n")
        print(f"WROTE    {path.relative_to(ROOT)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
