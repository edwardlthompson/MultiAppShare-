# Security Policy

## Supported versions

| Version | Supported |
|---------|-----------|
| Latest release on `main` | Yes |
| Older tags | Best effort |

## Reporting a vulnerability

**Do not** open public GitHub issues for security-sensitive reports.

1. Use [GitHub private vulnerability reporting](https://github.com/edwardlthompson/MultiAppShare-/security/advisories/new) if enabled, **or**
2. Open a minimal issue asking for a private contact channel.

We aim to acknowledge reports within **7 days**.

## Scope

Multi App Share is an offline Android client. In scope:

- Encrypted backup crypto (`BackupCipher`, export/import paths)
- Data at rest (Room, DataStore, JSON exports)
- Intent/deeplink handling (`multiappshare://`)
- Dependency vulnerabilities (Dependabot, CodeQL)

Out of scope: third-party apps invoked via `ACTION_SEND`, OEM share sheet behavior.

## Threat model

See [`docs/THREAT_MODEL.md`](docs/THREAT_MODEL.md).
