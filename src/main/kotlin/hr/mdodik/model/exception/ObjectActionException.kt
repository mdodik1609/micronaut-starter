package hr.mdodik.model.exception

import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException

class ObjectNotFoundException(obj: Class<*>, id: Int) : HttpStatusException(
    HttpStatus.NOT_FOUND,
    "${obj.simpleName} not found for id [$id].",
)

class ObjectNotSavedException(obj: Class<*>, id: Int) : HttpStatusException(
    HttpStatus.INTERNAL_SERVER_ERROR,
    "${obj.simpleName} not saved for id $id.",
)

class ObjectNotUpdatedException(obj: Class<*>, id: Int) : HttpStatusException(
    HttpStatus.INTERNAL_SERVER_ERROR,
    "${obj.simpleName} not updated for id $id.",
)

class ObjectNotDeletedException(obj: Class<*>, id: Int) : HttpStatusException(
    HttpStatus.INTERNAL_SERVER_ERROR,
    "${obj.simpleName} not deleted for id $id.",
)