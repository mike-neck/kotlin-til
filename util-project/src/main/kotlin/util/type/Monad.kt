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

interface MonadicBind<I: MonadInstance<M, I>, M: Monad, T>: Bind<I, M, T>

interface MonadInstance<M: Monad, I: MonadInstance<M, I>>: FunctorInstance<M, I> {

    fun <T, R> bind(value: MonadicBind<I, M, T>, func: (T) -> MonadicBind<I, M, R>): MonadicBind<I, M, R>

    fun <T> pure(value: T): MonadicBind<I, M, T>

    override fun <T, R> map(value: Bind<I, M, T>, func: (T) -> R): Bind<I, M, R> =
            map(value as MonadicBind<I, M, T>, func)

    fun <T, R> map(value: MonadicBind<I, M, T>, func: (T) -> R): MonadicBind<I, M, R>

    companion object {

        inline fun <M: Monad, reified I: MonadInstance<M, I>> take(ic: KClass<I> = I::class): I =
                ic.objectInstance ?: throw IllegalStateException("no object instance found.")
    }
}

interface MonadInstance2<M: Monad, I: MonadInstance2<M, I>>: FunctorInstance2<M, I> {

    fun <A, T, R> bind(value: Bind2<I, M, A, T>, func: (T) -> Bind2<I, M, A, R>): Bind2<I, M, A, R>

    fun <A, T> pure(value: T): Bind2<I, M, A, T>

    companion object {

        inline fun <M: Monad, reified I: MonadInstance2<M, I>> take(ic: KClass<I> = I::class): I =
                ic.objectInstance ?: throw IllegalStateException("no object instance found.")
    }
}
