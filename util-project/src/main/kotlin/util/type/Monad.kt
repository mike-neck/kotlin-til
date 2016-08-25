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

import util.unit
import kotlin.reflect.KClass

interface Monad: Functor

interface MonadInstance<M: Monad>: Instance<M> {

    fun <T, R> bind(value: Bind<M, T>, func: (T) -> Bind<M, R>): Bind<M, R>

    fun <T> pure(value: T): Bind<M, T>

    companion object {

        inline fun <reified M: Monad> of(kc: KClass<M> = M::class): ImplementedBy<M, MonadInstance<M>> =
                object: ImplementedBy<M, MonadInstance<M>> {
                    override fun by(impl: MonadInstance<M>) = unit { instances[kc] = impl }
                }

        val instances: MutableMap<KClass<*>, MonadInstance<*>> = mutableMapOf()

        @Suppress("UNCHECKED_CAST")
        inline fun <reified M: Monad, I: MonadInstance<M>> take(kc: KClass<M> = M::class): I =
                instances[kc] as I?
                        ?: throw IllegalStateException("instance definition for $kc is not registered.")
    }
}

interface ImplementedBy<C, in I: Instance<C>> {
    fun by(impl: I): Unit
}
