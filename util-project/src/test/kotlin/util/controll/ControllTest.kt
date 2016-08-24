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
package util.controll

import util.controll.MaybeOf.Companion.bind
import util.controll.MaybeOf.Companion.map
import util.controll.MaybeOf.Companion.orElse
import util.controll.MaybeOf.Companion.pure
import util.unit
import java.util.*

interface Bind<C, T>

interface FunctorType

interface FunctorInstance<F: FunctorType> {
    fun <T, R> map(functor: Bind<F, T> , f: (T) -> R): Bind<F, R>
}

interface MonadType: FunctorType

interface MonadInstance<M: MonadType> {
    fun <T, R> bind(monad: Bind<M, T>, f: (T) -> Bind<M, R>): Bind<M, R>
    fun <T> pure(value: T): Bind<M, T>
}

interface MaybeType: FunctorType, MonadType

typealias Maybe<T> = Bind<MaybeType, T>

sealed class MaybeOf<T>: Maybe<T> {

    class None<T>: MaybeOf<T>()

    class Just<T>(val value: T): MaybeOf<T>()

    companion object: FunctorInstance<MaybeType>, MonadInstance<MaybeType> {

        fun <T> Maybe<T>.fromJust(): T =
                when (this) {
                    is Just -> this.value
                    else    -> throw NoSuchElementException("No value")
                }

        fun <T> Maybe<T>.orElse(def: T): T =
                when (this) {
                    is Just -> this.value
                    else    -> def
                }

        override fun <T, R> map(functor: Maybe<T>, f: (T) -> R): Maybe<R> =
                when (functor) {
                    is Just -> Just(f(functor.value))
                    else    -> None()
                }

        override fun <T, R> bind(monad: Maybe<T>, f: (T) -> Maybe<R>): Maybe<R> =
                when (monad) {
                    is Just -> f(monad.value)
                    else    -> None()
                }

        override fun <T> pure(value: T): Maybe<T> = Just(value)
    }
}

fun main(args: Array<String>) =
        pure(67).let { map(it, Int::toChar) }
                .let { bind(it) { if (it.isDigit()) MaybeOf.None() else pure(it.toString()) } }
                .let { it.orElse("Not exists") }
                .unit(::println)





