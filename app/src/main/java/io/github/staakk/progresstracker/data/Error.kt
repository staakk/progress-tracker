package io.github.staakk.progresstracker.data

interface Error

sealed class CreationError : Error {
}

sealed class UpdateError : Error {
}

sealed class DeletionError : Error {
}

sealed class FetchError : Error {
    object ResourceNotFound : FetchError()
}

sealed class SearchError {

}

