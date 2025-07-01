package hr.mdodik.model.exception

import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException

class PasswordTooWeakException() : HttpStatusException(
    HttpStatus.UNAUTHORIZED,
    "Password too weak for user authentication.",
)