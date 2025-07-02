package hr.mdodik.service

import hr.mdodik.model.CreateUserRequest
import hr.mdodik.model.EmailSegment
import hr.mdodik.model.UpdateUserRequest
import hr.mdodik.model.User
import hr.mdodik.model.UserDto
import hr.mdodik.model.UserRole
import hr.mdodik.model.exception.ObjectNotDeletedException
import hr.mdodik.model.exception.ObjectNotFoundException
import hr.mdodik.model.exception.ObjectNotSavedException
import hr.mdodik.model.exception.ObjectNotUpdatedException
import hr.mdodik.repository.UserMongoRepository
import hr.mdodik.repository.UserPostgresRepository
import hr.mdodik.service.idGenerator.IdGeneratorService
import hr.mdodik.util.HasLog
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class UserService (
    private val userMongoRepository: UserMongoRepository,
    private val userPostgresRepository: UserPostgresRepository,
    private val securityService: SecurityService,
    private val idGeneratorService: IdGeneratorService,
    @Value("\${app.database.type:mongo}") private val databaseType: String,
): HasLog() {

    private val userRepository: Any
        get() = when (databaseType.lowercase()) {
            "postgres", "postgresql" -> userPostgresRepository
            else -> userMongoRepository
        }

    suspend fun getUserById(id: Int): User {
        return when (databaseType.lowercase()) {
            "postgres", "postgresql" -> {
                userPostgresRepository.findById(id)
                    .orElseThrow { ObjectNotFoundException(User::class.java, id) }
            }
            else -> {
                userMongoRepository.getOneById(id)
                    ?: throw ObjectNotFoundException(User::class.java, id)
            }
        }
    }

    suspend fun getAllUsers(): List<User> {
        return when (databaseType.lowercase()) {
            "postgres", "postgresql" -> {
                userPostgresRepository.findAll().toList()
            }
            else -> {
                userMongoRepository.getAll()
            }
        }
    }

    suspend fun createUser(createUserRequest: CreateUserRequest): UserDto {
        val passwordSegment = securityService.createPassword(createUserRequest.password)
        val now = Instant.now()
        
        val user = User(
            id = 0, // Will be auto-generated for PostgreSQL, or set by ID generator for MongoDB
            fullName = createUserRequest.fullName,
            username = createUserRequest.username,
            created = now,
            updated = now,
            userRole = UserRole.FREE,
            emilSegment = EmailSegment(
                email = createUserRequest.email,
                isValidated = false,
            ),
            passwordSegment = passwordSegment
        )

        val savedUser = when (databaseType.lowercase()) {
            "postgres", "postgresql" -> {
                userPostgresRepository.save(user)
            }
            else -> {
                val id = idGeneratorService.generateIdFor(User::class)
                val userWithId = user.copy(id = id)
                userMongoRepository.save(userWithId) 
                    ?: throw ObjectNotSavedException(User::class.java, id)
                userWithId
            }
        }
        
        return savedUser.toDto()
    }

    suspend fun updateUser(id: Int, updateUserRequest: UpdateUserRequest): UserDto {
        return when (databaseType.lowercase()) {
            "postgres", "postgresql" -> {
                val existingUser = userPostgresRepository.findById(id)
                    .orElseThrow { ObjectNotFoundException(User::class.java, id) }
                
                val updatedUser = existingUser.copy(
                    fullName = updateUserRequest.fullName ?: existingUser.fullName,
                    emilSegment = existingUser.emilSegment.copy(
                        email = updateUserRequest.email ?: existingUser.emilSegment.email
                    ),
                    updated = Instant.now()
                )
                
                userPostgresRepository.update(updatedUser)
                updatedUser.toDto()
            }
            else -> {
                val result = userMongoRepository.update(id, updateUserRequest)
                if(!result.wasAcknowledged() || (result.wasAcknowledged() && result.modifiedCount == 0L)) {
                    throw ObjectNotUpdatedException(User::class.java, id).also {
                        log.warn("User $id was not updated.")
                    }
                } else if(result.matchedCount == 0L) {
                    throw ObjectNotFoundException(User::class.java, id).also {
                        log.warn("User $id was not found.")
                    }
                }
                getUserById(id).toDto()
            }
        }
    }

    suspend fun deleteUser(id: Int) {
        when (databaseType.lowercase()) {
            "postgres", "postgresql" -> {
                if (!userPostgresRepository.existsById(id)) {
                    throw ObjectNotFoundException(User::class.java, id).also {
                        log.warn("User with id $id was not found.")
                    }
                }
                userPostgresRepository.deleteById(id)
                log.info("User with ID $id was deleted.")
            }
            else -> {
                val result = userMongoRepository.deleteById(id)
                if(!result.wasAcknowledged()) {
                    throw ObjectNotDeletedException(User::class.java, id).also {
                        log.warn("User with ID $id was not deleted.")
                    }
                } else {
                    if(result.deletedCount == 0L) {
                        throw ObjectNotFoundException(User::class.java, id).also {
                            log.warn("User with id $id was not found.")
                        }
                    }
                }
                log.info("User with ID $id was deleted.")
            }
        }
    }
}