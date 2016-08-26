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
package util.type

import kotlin.reflect.KClass

interface Monad: Functor

interface MonadInstance<M: Monad, I: MonadInstance<M, I>>: Instance<M> {

    fun <T, R> bind(value: Bind<I, M, T>, func: (T) -> Bind<I, M, R>): Bind<I, M, R>

    fun <T> pure(value: T): Bind<I, M, T>

    companion object {

        @Suppress("UNCHECKED_CAST")
        inline fun <M: Monad, reified I: MonadInstance<M, I>> take(ic: KClass<I> = I::class): I =
                ic.objectInstance ?: throw IllegalStateException("no object instance found.")
    }
}
