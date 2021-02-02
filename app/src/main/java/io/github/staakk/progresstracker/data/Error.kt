package io.github.staakk.progresstracker.data

interface Error

sealed class CreationError : Error {
    object IdAlreadyExists : CreationError()
}

sealed class UpdateError : Error {
    object ResourceDoesNotExist : UpdateError()
}

sealed class DeletionError : Error {
    object CannotDeleteResource : DeletionError()
}

sealed class QueryError : Error {
    object ResourceNotFound : QueryError()
}

