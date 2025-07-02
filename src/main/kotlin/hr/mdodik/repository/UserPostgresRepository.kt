package hr.mdodik.repository

import hr.mdodik.model.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface UserPostgresRepository : JpaRepository<User, Int> {
    fun findByUsername(username: String): Optional<User>
    fun findByEmilSegment_Email(email: String): Optional<User>
    fun existsByUsername(username: String): Boolean
    fun existsByEmilSegment_Email(email: String): Boolean
} 