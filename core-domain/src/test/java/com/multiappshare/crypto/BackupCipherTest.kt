package com.multiappshare.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupCipherTest {

    @Test
    fun encrypt_decrypt_roundtrip() {
        val plain = """{"version":1,"groups":[]}"""
        val passphrase = "correcthorsebatterystaple".toCharArray()
        val envelope = BackupCipher.encryptUtf8(plain, passphrase)
        assertTrue(BackupCipher.isEncryptedEnvelope(envelope))
        val decoded = BackupCipher.decryptUtf8(envelope, passphrase)
        assertEquals(plain, decoded)
        passphrase.fill('\u0000')
    }

    @Test
    fun wrongPassphrase_fails() {
        val plain = """{"version":1,"groups":[]}"""
        val pass = "correcthorsebatterystaple".toCharArray()
        val envelope = BackupCipher.encryptUtf8(plain, pass)
        pass.fill('\u0000')
        val wrong = "wrongpassphrase!!".toCharArray()
        try {
            BackupCipher.decryptUtf8(envelope, wrong)
            throw AssertionError("expected decryption failure")
        } catch (_: Exception) {
            // AEAD failure expected
        } finally {
            wrong.fill('\u0000')
        }
    }

    @Test
    fun plaintext_notDetectedAsEnvelope() {
        assertFalse(BackupCipher.isEncryptedEnvelope("[]"))
        assertFalse(BackupCipher.isEncryptedEnvelope("not json"))
    }
}
