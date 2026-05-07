package com.multiappshare.crypto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

/**
 * Passphrase-based encryption for backup JSON (AES-256-GCM, PBKDF2-HMAC-SHA256).
 * FOSS: standard Android/JCA only—no third-party crypto SDKs.
 */
object BackupCipher {

    const val ENVELOPE_FORMAT = "multiappshare-encrypted-backup"
    private const val ENVELOPE_VERSION = 1
    private const val KDF = "PBKDF2WithHmacSHA256"
    internal const val PBKDF2_ITERATIONS = 310_000
    private const val SALT_BYTES = 16
    private const val IV_BYTES = 12
    private const val AES_KEY_BITS = 256
    private const val AES = "AES/GCM/NoPadding"

    private val secureRandom = SecureRandom()

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Serializable
    data class EncryptedEnvelope(
        val format: String = ENVELOPE_FORMAT,
        val version: Int = ENVELOPE_VERSION,
        val kdf: String = KDF,
        val iterations: Int = PBKDF2_ITERATIONS,
        val saltB64: String,
        val ivB64: String,
        val ciphertextB64: String,
    )

    fun isEncryptedEnvelope(fileUtf8: String): Boolean {
        val trimmed = fileUtf8.trimStart()
        if (!trimmed.startsWith("{")) return false
        return try {
            val env = json.decodeFromString<EncryptedEnvelope>(trimmed)
            env.format == ENVELOPE_FORMAT && env.version == ENVELOPE_VERSION && env.kdf == KDF
        } catch (_: Exception) {
            false
        }
    }

    fun encryptUtf8(plaintext: String, passphrase: CharArray): String {
        val salt = ByteArray(SALT_BYTES).also { secureRandom.nextBytes(it) }
        val iv = ByteArray(IV_BYTES).also { secureRandom.nextBytes(it) }
        val keySpec = deriveKey(passphrase, salt, PBKDF2_ITERATIONS)
        val cipher = Cipher.getInstance(AES)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, GCMParameterSpec(128, iv))
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        val b64 = Base64.getEncoder()
        val envelope = EncryptedEnvelope(
            saltB64 = b64.encodeToString(salt),
            ivB64 = b64.encodeToString(iv),
            ciphertextB64 = b64.encodeToString(ciphertext),
        )
        return json.encodeToString(envelope)
    }

    fun decryptUtf8(envelopeJsonUtf8: String, passphrase: CharArray): String {
        val env = json.decodeFromString<EncryptedEnvelope>(envelopeJsonUtf8.trim())
        require(env.format == ENVELOPE_FORMAT) { "Unknown backup format" }
        require(env.version == ENVELOPE_VERSION) { "Unsupported backup version: ${env.version}" }
        require(env.kdf == KDF) { "Unsupported KDF: ${env.kdf}" }

        val decoder = Base64.getDecoder()
        val salt = decoder.decode(env.saltB64)
        val iv = decoder.decode(env.ivB64)
        val ciphertext = decoder.decode(env.ciphertextB64)

        val keySpec = deriveKey(passphrase, salt, env.iterations)
        val cipher = Cipher.getInstance(AES)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, GCMParameterSpec(128, iv))
        val plain = cipher.doFinal(ciphertext)
        return plain.toString(Charsets.UTF_8)
    }

    private fun deriveKey(passphrase: CharArray, salt: ByteArray, iterations: Int): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance(KDF)
        val spec = PBEKeySpec(passphrase, salt, iterations, AES_KEY_BITS)
        val tmp = factory.generateSecret(spec)
        val raw = tmp.encoded
        return SecretKeySpec(raw, "AES")
    }
}
