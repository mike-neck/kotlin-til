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

import util.type.Bind
import util.type.FunctorInstance
import util.type.Monad
import util.type.MonadInstance

interface MaybeType: Monad

typealias Maybe<T> = Bind<MaybeType, T>

fun <T, R> Maybe<T>.fmap(f: (T) -> R): Maybe<R> = MaybeInstance.map(this, f)

fun <T, R> Maybe<T>.bind(f: (T) -> Maybe<R>): Maybe<R> = MaybeInstance.bind(this, f)

sealed class MaybeOf<out T>: Maybe<T> {

    class None<out T>: MaybeOf<T>()

    data class Just<out T>(val just: T): MaybeOf<T>()
} 

object MaybeInstance: MonadInstance<MaybeType>, FunctorInstance<MaybeType> {

    override fun <T, R> map(value: Maybe<T>, func: (T) -> R): Maybe<R> =
            when (value) {
                is MaybeOf.Just -> MaybeOf.Just(func(value.just))
                else            -> MaybeOf.None()
            }

    override fun <T, R> bind(value: Maybe<T>, func: (T) -> Maybe<R>): Maybe<R> =
            when (value) {
                is MaybeOf.Just -> func(value.just)
                else            -> MaybeOf.None()
            }

    override fun <T> pure(value: T): Maybe<T> = MaybeOf.Just(value)
} 
