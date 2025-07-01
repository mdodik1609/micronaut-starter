package hr.mdodik.service

import hr.mdodik.model.PasswordSegment
import hr.mdodik.model.exception.PasswordTooWeakException
import hr.mdodik.util.MIN_PASSWORD_LEN
import jakarta.inject.Singleton
import java.security.MessageDigest
import java.util.*
import kotlin.random.Random

@Singleton
class SecurityService(

) {
    fun hashPassword(password: String, salt: String) =
        encodeStringToHex(
            MessageDigest.getInstance("SHA-256").digest((password + salt).toByteArray())
        )

    private fun encodeStringToHex(byteArray: ByteArray): String {
        var hex = ""
        byteArray.forEach { hex += byteToHex(it) }
        return hex
    }

    private fun byteToHex(byte: Byte): String = "%02x".format(byte)

    fun getNextSalt(saltSize: Int): String {
        val salt = ByteArray(saltSize)
        Random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun createPassword(password: String): PasswordSegment {
        if(isPasswordTooWeak(password)) throw PasswordTooWeakException()
        val salt = getNextSalt(16)
        val hashedPassword = hashPassword(password, salt)
        return  PasswordSegment(hashedPassword, salt)
    }

    private fun isPasswordTooWeak(password: String): Boolean = when {
        password.length < MIN_PASSWORD_LEN -> true
        !password.any { it.isUpperCase() } -> true
        !password.any { it.isLowerCase() } -> true
        !password.any { it.isDigit() } -> true
        !password.any { "!@#\$%^&*()-_=+[{]};:'\",<.>/?`~\\|".contains(it) } -> true
        password.any { it.isWhitespace() } -> true
        else -> false
    }

}