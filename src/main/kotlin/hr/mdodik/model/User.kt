package hr.mdodik.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class User(
    @JsonProperty("_id") val id: Int,
    @JsonProperty("username") val username: String,
    @JsonProperty("fullName") val fullName: String,
    @JsonProperty("created") val created: Instant,
    @JsonProperty("updated") val updated: Instant,
    @JsonProperty("emilSegment") val emilSegment: EmailSegment,
    @JsonProperty("passwordSegment") val passwordSegment: PasswordSegment,
    @JsonProperty("userRole") val userRole: UserRole,
) {
    fun toDto() = UserDto(
        id = this.id,
        username = this.username,
        fullName = this.fullName,
        userRole = this.userRole,
        email = this.emilSegment.email,
    )
}

data class EmailSegment(
    @JsonProperty("email") val email: String,
    @JsonProperty("isValidated") val isValidated: Boolean,
)

data class PasswordSegment(
    @JsonProperty("password") val password: String,
    @JsonProperty("salt") val salt: String,
)

enum class UserRole {
    FREE,
    PAID,
    PLUS,
    ADMIN
}

data class UserDto(
    val id: Int,
    val username: String,
    val userRole: UserRole,
    val fullName: String,
    val email: String,
)

data class CreateUserRequest(
    val username: String,
    val password: String,
    val fullName: String,
    val email: String,
)

data class UpdateUserRequest(
    val email: String?,
    val fullName: String?,
) {
    fun isEmpty() = email.isNullOrEmpty() && fullName.isNullOrEmpty()
}