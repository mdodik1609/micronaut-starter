package hr.mdodik.controller

import hr.mdodik.model.CreateUserRequest
import hr.mdodik.model.UpdateUserRequest
import hr.mdodik.model.exception.ObjectNotFoundException
import hr.mdodik.service.UserService
import hr.mdodik.util.ADMIN_USER_ROLE
import hr.mdodik.util.HasLog
import hr.mdodik.util.INTERNAL_MESSAGE_ERROR
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Patch
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Singleton

@Singleton
class UserController(
    private val userService: UserService,
): HasLog() {

    @Post
    suspend fun createUser(
        @Body createUserRequest: CreateUserRequest,
    ): MutableHttpResponse<*> {
        return try {
            val user = userService.createUser(createUserRequest)
            HttpResponse.ok(
                user
            ).also { log.info("POST /user successful for id: [${user.id}].") }
        } catch (e: RuntimeException) {
            HttpResponse.badRequest(
                INTERNAL_MESSAGE_ERROR
            ).also { log.error(e.message) }
        }
    }

    @Get("/{id}")
    suspend fun getUser(
        @PathVariable id: Int,
    ): MutableHttpResponse<*> {
        return try {
            val user = userService.getUserById(id)
            HttpResponse.ok(
                user
            ).also { log.info("GET /user/{$id} successful for id: [${user.id}].") }
        } catch (e: RuntimeException) {
            HttpResponse.badRequest(
                INTERNAL_MESSAGE_ERROR
            ).also { log.error(e.message) }
        }
    }

    @Get
    @RolesAllowed(ADMIN_USER_ROLE)
    suspend fun getAllUsers(): MutableHttpResponse<*> {
        return try {
            val users = userService.getAllUsers()
                .map { it.toDto() }
            HttpResponse.ok(
                users
            ).also { log.info("GET /user successful.") }
        } catch (e: RuntimeException) {
            HttpResponse.badRequest(
                INTERNAL_MESSAGE_ERROR
            ).also { log.error(e.message) }
        }
    }

    @Patch("/{id}")
    suspend fun updateUser(
        @PathVariable id: Int,
        @Body updateUserRequest: UpdateUserRequest,
    ): MutableHttpResponse<*> {
        return try {
            val user = userService.updateUser(id, updateUserRequest)
            HttpResponse.ok(
                user
            ).also { log.info("PATCH /user successful for id: [${user.id}].") }
        } catch (e: HttpStatusException) {
             throw e
        } catch (e: RuntimeException) {
            HttpResponse.badRequest(
                INTERNAL_MESSAGE_ERROR
            ).also { log.error(e.message) }
        }
    }

    @Delete("/{id}")
    @RolesAllowed(ADMIN_USER_ROLE)
    suspend fun deleteUser(
        @PathVariable id: Int
    ): MutableHttpResponse<*> {
        return try {
            userService.deleteUser(id)
            HttpResponse.ok(
                "User deleted successfully."
            ).also { log.info("DELETE /user successful for id: [$id].") }
        } catch (e: RuntimeException) {
            HttpResponse.badRequest(
                INTERNAL_MESSAGE_ERROR
            ).also { log.error(e.message) }
        }
    }
}