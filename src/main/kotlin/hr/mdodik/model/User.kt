package hr.mdodik.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "users")
@Serdeable
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("_id") val id: Int = 0,
    
    @Column(unique = true, nullable = false)
    @JsonProperty("username") val username: String,
    
    @Column(name = "full_name", nullable = false)
    @JsonProperty("fullName") val fullName: String,
    
    @Column(nullable = false)
    @JsonProperty("created") val created: Instant,
    
    @Column(nullable = false)
    @JsonProperty("updated") val updated: Instant,
    
    @Embedded
    @JsonProperty("emilSegment") val emilSegment: EmailSegment,
    
    @Embedded
    @JsonProperty("passwordSegment") val passwordSegment: PasswordSegment,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
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

@Embeddable
@Serdeable
data class EmailSegment(
    @Column(nullable = false)
    @JsonProperty("email") val email: String,
    
    @Column(name = "is_validated", nullable = false)
    @JsonProperty("isValidated") val isValidated: Boolean,
)

@Embeddable
@Serdeable
data class PasswordSegment(
    @Column(nullable = false)
    @JsonProperty("password") val password: String,
    
    @Column(nullable = false)
    @JsonProperty("salt") val salt: String,
)

enum class UserRole {
    FREE,
    PAID,
    PLUS,
    ADMIN
}

@Serdeable
data class UserDto(
    val id: Int,
    val username: String,
    val userRole: UserRole,
    val fullName: String,
    val email: String,
)

@Serdeable
data class CreateUserRequest(
    val username: String,
    val password: String,
    val fullName: String,
    val email: String,
)

@Serdeable
data class UpdateUserRequest(
    val email: String?,
    val fullName: String?,
) {
    fun isEmpty() = email.isNullOrEmpty() && fullName.isNullOrEmpty()
}