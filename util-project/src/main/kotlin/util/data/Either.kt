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
package util.data

import util.type.Monad
import util.type.Type

interface EitherType: Monad

typealias Either<L, R> = Type.Kind2<EitherType, EitherImpl.Companion, L, R>

typealias EitherSupport<S, T, R> = Monad.Kind2Instance.Support<EitherType, EitherImpl.Companion, T, S, R>

typealias EitherMonad = EitherImpl.Companion

fun <L, R> Left(obj: L): Either<L, R> = EitherImpl.Left(obj)

fun <L, R> Right(obj: R): Either<L, R> = EitherImpl.Right(obj)

sealed class EitherImpl<L, R>: Type.Kind2<EitherType, EitherMonad, L, R>() {

    data class Left<L, R>(val left: L): EitherImpl<L, R>()

    data class Right<L, R>(val right: R): EitherImpl<L, R>()

    companion object: Monad.Kind2Instance<EitherType, EitherMonad> {

        override fun <T, S, R> fn(): EitherSupport<S, T, R> = object : EitherSupport<S, T, R> {

            override val map: (Either<S, T>, (T) -> R) -> Either<S, R>
                get() = { o, f -> Companion.map(o, f) }

            override val bind: (Either<S, T>, (T) -> Either<S, R>) -> Either<S, R>
                get() = { o, f -> Companion.bind(o, f) }

            override val pure: (T) -> Either<S, T>
                get() = { o -> Companion.pure(o) }
        }

        override fun <T, S, R> map(obj: Either<S, T>, func: (T) -> R): Either<S, R> =
                when (obj) {
                    is Left  -> Left(obj.left)
                    is Right -> Right(func(obj.right))
                    else     -> illegalArgument(obj)
                }

        override fun <T, S, R> bind(obj: Either<S, T>, func: (T) -> Either<S, R>): Either<S, R> =
                when (obj) {
                    is Left  -> Left(obj.left)
                    is Right -> func(obj.right)
                    else     -> illegalArgument(obj)
                }

        override fun <T, S> pure(value: T): Either<S, T> = Right(value)
    }
}
