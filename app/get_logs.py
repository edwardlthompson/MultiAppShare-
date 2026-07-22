"""Fetch failed GitLab pipeline job logs (requires GITLAB_TOKEN env).

Prefer scripts/fetch-gitlab-job-log.ps1 for day-to-day use.
"""
from __future__ import annotations

import os
import sys

import requests

GITLAB_URL = "https://gitlab.com"
PROJECT_ID = os.environ.get("GITLAB_PROJECT_ID", "edwardleethompson/fdroiddata")


def main() -> int:
    token = os.environ.get("GITLAB_TOKEN", "").strip()
    if not token:
        print("Set GITLAB_TOKEN (GitLab PAT with read_api). Never hardcode tokens.", file=sys.stderr)
        return 1

    if len(sys.argv) < 2:
        print(f"Usage: {sys.argv[0]} <pipeline_id>", file=sys.stderr)
        return 1

    pipeline_id = sys.argv[1]
    headers = {"PRIVATE-TOKEN": token}
    encoded = PROJECT_ID.replace("/", "%2F")

    try:
        jobs_url = f"{GITLAB_URL}/api/v4/projects/{encoded}/pipelines/{pipeline_id}/jobs"
        response = requests.get(jobs_url, headers=headers, timeout=60)
        response.raise_for_status()
        jobs = response.json()

        failed_jobs = [j for j in jobs if j["status"] == "failed"]
        print(f"--- Found {len(failed_jobs)} failures in Pipeline {pipeline_id} ---\n")

        for job in failed_jobs:
            print(f"=== JOB: {job['name']} (ID: {job['id']}) ===")
            trace_url = f"{GITLAB_URL}/api/v4/projects/{encoded}/jobs/{job['id']}/trace"
            trace_res = requests.get(trace_url, headers=headers, timeout=60)
            if trace_res.status_code == 200:
                print(trace_res.text[-3000:])
            else:
                print(f"Could not retrieve log. Status: {trace_res.status_code}")
            print("\n" + "=" * 40 + "\n")
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
