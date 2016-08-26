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

import util.plus
import util.type.Bind2
import util.type.Monad
import util.type.MonadInstance2

interface EitherType: Monad

typealias Either<L, R> = Bind2<EitherMonad, EitherType, L, R>

sealed class EitherOf<L, R>: Either<L, R> {
    class Left<L, R>(val lf: () -> L): EitherOf<L, R>() {
        constructor(l: L): this({ l })
    }
    class Right<L, R>(val rf: () -> R): EitherOf<L, R>() {
        constructor(r: R): this({ r })
    }
}

object EitherMonad: MonadInstance2<EitherType, EitherMonad> {
    override fun <A, T, R> map(value: Either<A, T>, func: (T) -> R): Either<A, R> =
            when (value) {
                is EitherOf.Right -> EitherOf.Right(value.rf + func)
                is EitherOf.Left  -> EitherOf.Left(value.lf)
                else              -> throw UnsupportedOperationException("This type of Either is not supported.[${value.javaClass}]")
            }

    override fun <A, T, R> bind(value: Either<A, T>, func: (T) -> Either<A, R>): Either<A, R> =
            when (value) {
                is EitherOf.Right -> func(value.rf())
                is EitherOf.Left  -> EitherOf.Left(value.lf)
                else              -> throw UnsupportedOperationException("This type of Either is not supported.[${value.javaClass}]")
            }

    override fun <A, T> pure(value: T): Either<A, T> = EitherOf.Right<A, T>({ value })
}
