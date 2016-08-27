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

interface MaybeType: Monad

typealias MaybeFunctions = MaybeImpl.Companion

typealias MaybeSupport<T, R> = Monad.Kind1Instance.Support<MaybeType, MaybeImpl.Companion, T, R>

typealias Maybe<T> = Type.Kind1<MaybeType, MaybeImpl.Companion, T>

fun <T> None(): Maybe<T> = MaybeImpl.None()

fun <T> Just(obj: T): Maybe<T> = MaybeImpl.Just(obj)

sealed class MaybeImpl<T>: Type.Kind1<MaybeType, MaybeImpl.Companion, T>() {

    data class None<T>(val none: Unit = Unit): MaybeImpl<T>()

    data class Just<T>(val get: T): MaybeImpl<T>()

    companion object: Monad.Kind1Instance<MaybeType, MaybeImpl.Companion> {
        override fun <T, R> fn(): MaybeSupport<T, R> = object : MaybeSupport<T, R> {

                    override val map: (Maybe<T>, (T) -> R) -> Maybe<R>
                        get() = { o, f -> Companion.map(o, f) }

                    override val bind: (Maybe<T>, (T) -> Maybe<R>) -> Maybe<R>
                        get() = { o, f -> Companion.bind(o, f) }

                    override val pure: (T) -> Maybe<T>
                        get() = { o -> Companion.pure(o) }
                }

        override fun <T, R> map(obj: Maybe<T>, func: (T) -> R): Maybe<R> =
                when(obj) {
                    is Just -> Just(func(obj.get))
                    is None -> None()
                    else    -> illegalArgument(obj)
                }

        override fun <T, R> bind(obj: Maybe<T>, func: (T) -> Kind1<MaybeType, Companion, R>): Maybe<R> =
                when (obj) {
                    is Just -> func(obj.get)
                    is None -> None()
                    else    -> illegalArgument(obj)
                }

        override fun <T> pure(value: T): Maybe<T> = Just(value)
    }
}
