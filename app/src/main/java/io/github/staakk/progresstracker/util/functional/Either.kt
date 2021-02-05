package io.github.staakk.progresstracker.util.functional

import androidx.annotation.VisibleForTesting

sealed class Either<out E, out A> {

    inline infix fun <B> map(crossinline f: (A) -> B): Either<E, B> = when (this) {
        is Left -> this
        is Right -> Right(f(value))
    }

    inline infix fun <B> mapLeft(crossinline f: (E) -> B): Either<B, A> = when (this) {
        is Left -> Left(f(value))
        is Right -> this
    }

    inline fun <T> fold(l: (E) -> T, r: (A) -> T) = when (this) {
        is Left -> l(value)
        is Right -> r(value)
    }

    /**
     * Should not be used in the code except for testing purposes.
     */
    @VisibleForTesting
    val left: E
        get() = if (this is Left)
            value
        else
            throw IllegalStateException("Trying to get left value from right.")

    /**
     * Should not be used in the code except for testing purposes.
     */
    @VisibleForTesting
    val right: A
        get() = if (this is Right)
            value
        else
            throw java.lang.IllegalStateException("Trying to get right value from left.")
}

inline infix fun <E, EE : E, A, B> Either<E, A>.flatMap(crossinline f: (A) -> Either<EE, B>) =
    when (this) {
        is Left -> this
        is Right -> f(value)
    }

inline infix fun <E, EE : E, A, AA : A> Either<E, A>.orElse(f: () -> Either<EE, AA>) = when (this) {
    is Left -> f()
    is Right -> this
}

fun <E, EE : E, A> Either<E, Either<EE, A>>.flatten() = flatMap { it }

object Try {

    inline operator fun <A> invoke(crossinline f: () -> A): Either<Throwable, A> =
        try {
            f().right()
        } catch (t: Throwable) {
            t.left()
        }

}

data class Left<out E>(val value: E) : Either<E, Nothing>()
data class Right<out A>(val value: A) : Either<Nothing, A>()

fun <E, A> E.left(): Either<E, A> = Left(this)
fun <E, A> A.right(): Either<E, A> = Right(this)