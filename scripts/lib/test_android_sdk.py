"""Tests for Android SDK path detection and local.properties writes."""
from __future__ import annotations

import tempfile
import unittest
from pathlib import Path

from android_sdk import (
    candidate_sdk_dirs,
    ensure_android_sdk,
    first_existing_sdk,
    sdk_dir_from_local_properties,
    write_local_properties,
)


class AndroidSdkTest(unittest.TestCase):
    def test_candidates_include_home_and_env(self) -> None:
        paths = candidate_sdk_dirs()
        self.assertTrue(any(p.name == "Sdk" for p in paths))

    def test_first_existing_none(self) -> None:
        missing = Path("/no-such-android-sdk-dir-mas-test")
        self.assertIsNone(first_existing_sdk([missing]))

    def test_write_and_read_local_properties(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            sdk = root / "fake-sdk"
            sdk.mkdir()
            write_local_properties(root, sdk)
            found = sdk_dir_from_local_properties(root)
            self.assertEqual(found, sdk.resolve())
            text = (root / "local.properties").read_text(encoding="utf-8")
            self.assertIn("sdk.dir=", text)

    def test_ensure_writes_when_sdk_exists(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            sdk = root / "Android" / "Sdk"
            sdk.mkdir(parents=True)
            write_local_properties(root, sdk)
            again = ensure_android_sdk(root)
            self.assertEqual(again, sdk.resolve())

    def test_ensure_none_when_empty(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            self.assertIsNone(sdk_dir_from_local_properties(root))
            self.assertIsNone(first_existing_sdk([root / "missing"]))


if __name__ == "__main__":
    unittest.main()
