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
import hr.mdodik.service.idGenerator.IdGeneratorService
import hr.mdodik.util.HasLog
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class UserService (
    private val userRepository: UserMongoRepository,
    private val securityService: SecurityService,
    private val idGeneratorService: IdGeneratorService,
): HasLog() {

    suspend fun getUserById(id: Int) =
        userRepository.getOneById(id)
            ?: throw ObjectNotFoundException(User::class.java, id)

    suspend fun getAllUsers() =
        userRepository.getAll()

    suspend fun createUser(createUserRequest: CreateUserRequest): UserDto {
        val passwordSegment = securityService.createPassword(createUserRequest.password)
        val id = idGeneratorService.generateIdFor(User::class)
        val user = User(
            id = id,
            fullName = createUserRequest.fullName,
            username = createUserRequest.username,
            created = Instant.now(),
            updated = Instant.now(),
            userRole = UserRole.FREE,
            emilSegment = EmailSegment(
                email = createUserRequest.email,
                isValidated = false,
            ),
            passwordSegment = passwordSegment
        )
        userRepository.save(user) ?: throw ObjectNotSavedException(User::class.java, id)
        return user.toDto()
    }

    suspend fun updateUser(id: Int, updateUserRequest: UpdateUserRequest): UserDto {
        val result = userRepository.update(id, updateUserRequest)
        if(!result.wasAcknowledged() || (result.wasAcknowledged() && result.modifiedCount == 0L)) {
            throw ObjectNotUpdatedException(User::class.java, id).also {
                log.warn("User $id was not updated.")
            }
        } else if(result.matchedCount == 0L) {
            throw ObjectNotFoundException(User::class.java, id).also {
                log.warn("User $id was not found.")
            }
        }
        return getUserById(id).toDto()
    }

    suspend fun deleteUser(id: Int) {
        val result = userRepository.deleteById(id)
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