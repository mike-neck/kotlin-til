/*
 * Copyright 2016 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util.either

import util.plus
import java.util.*

sealed class Either<L, R> {

    abstract val left: L
    abstract val right: R

    abstract fun <T> map(f: (R) -> T): Either<L, T>

    abstract fun <T> bind(f: (R) -> Either<L, T>): Either<L, T>

    fun orThrow(f: (L) -> Throwable): R = when(this) {
        is Left ->  throw f(this.left)
        is Right -> this.right
    }

    fun or(f: (L) -> R): R = when(this) {
        is Left -> this.left.let(f)
        is Right -> this.right
    }

    class Left<L, R>(val lf: () -> L): Either<L, R>() {

        constructor(l: L): this ({ l })

        override val left: L by lazy(lf)

        override val right: R
            get() = throw NoSuchElementException("this is left.")

        override fun <T> map(f: (R) -> T): Either<L, T> = Left(lf)

        override fun <T> bind(f: (R) -> Either<L, T>): Either<L, T> = Left(lf)
    }

    class Right<L, R>(val rf: () -> R): Either<L, R>() {

        constructor(r: R): this ({ r })

        override val right: R by lazy(rf)

        override val left: L
            get() = throw NoSuchElementException("this is right.")

        override fun <T> map(f: (R) -> T): Either<L, T> = Right(rf + f)

        override fun <T> bind(f: (R) -> Either<L, T>): Either<L, T> = (rf + f)()
    }
}

fun <T> T?.either(): Either<Unit, T> = if (this == null) Either.Left({ Unit }) else Either.Right<Unit, T>({ this })

fun <T, L> T?.either(f: () -> L): Either<L, T> = if (this == null) Either.Left(f) else Either.Right<L, T>({ this })

fun <R> Either<R, R>.result(): R = when (this) {
    is Either.Left ->  this.left
    is Either.Right -> this.right
}

fun <E: Throwable, T> Either<E, T>.get(): T = when (this) {
    is Either.Left -> throw this.left
    is Either.Right -> this.right
}
