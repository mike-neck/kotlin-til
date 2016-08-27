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

import util.then
import util.type.Monad
import util.type.Type
import kotlin.reflect.KClass

class MonadLawTestSupport {

    object FunctorIdentity {
        fun <T> id(): (T) -> T = { it }
    }

    object FunctorCompositionLaw {
        val f: (List<String>) -> List<Int> = { it.map(String::length) }
        val g: (List<Int>) -> Int = List<Int>::sum
    }

    object MonadLeftIdentity {
        inline fun <M: Monad, reified I: Monad.Kind1Instance<M, I>, T, R> left(
                noinline f: (T) -> Type.Kind1<M, I, R>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): (T) -> Type.Kind1<M, I, R> =
                instance.then { it.fn<T, R>() }
                        .then { (it.pure + it.bind)(f) }
                        ?: throw IllegalStateException("instance information is not available.")

        inline fun <M: Monad, reified I: Monad.Kind2Instance<M, I>, S, T, R> left(
                noinline f: (T) -> Type.Kind2<M, I, S, R>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): (T) -> Type.Kind2<M, I, S, R> =
                instance.then { it.fn<T, S, R>() }
                        .then { (it.pure + it.bind)(f) }
                        ?: throw IllegalStateException("instance information is not available.")
    }
}

infix operator fun <P, Q, R, S, F:(P) -> Q, G: (Q,R) -> S> F.plus(g: G): (P, R) -> S = { p, r -> g(this(p), r) }
