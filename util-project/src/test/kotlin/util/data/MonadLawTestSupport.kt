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

    object MonadRightIdentity {
        inline fun <M: Monad, reified I: Monad.Kind1Instance<M, I>, T> left(
                obj: Type.Kind1<M, I, T>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): Type.Kind1<M, I, T> =
                instance.then { it.fn<T, T>() }
                        .then { it.bind(it.pure) }
                        .then { it(obj) }
                        ?: throw IllegalStateException("instance information is not available.")

        inline fun <M: Monad, reified I: Monad.Kind2Instance<M, I>, S, T> left(
                obj: Type.Kind2<M, I, S, T>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): Type.Kind2<M, I, S, T> =
                instance.then { it.fn<T, S, T>() }
                        .then { it.bind(it.pure) }
                        .then { it(obj) }
                        ?: throw IllegalStateException("instance information is not available.")
    }

    object MonadAssociativity {
        inline fun <M: Monad, reified I: Monad.Kind1Instance<M, I>, T1, T2, T3> left(
                noinline f: (T1) -> Type.Kind1<M, I, T2>,
                noinline g: (T2) -> Type.Kind1<M, I, T3>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): (Type.Kind1<M, I, T1>) -> Type.Kind1<M, I, T3> =
                instance.then { it.fn<T1, T2>() to it.fn<T2, T3>() }
                        .then { it.first.bind(f) to it.second.bind(g) }
                        .then { p -> { m: Type.Kind1<M,I,T1> -> p.second(p.first(m)) } }
                        ?: throw IllegalStateException("instance information is not available.")

        inline fun <M: Monad, reified I: Monad.Kind2Instance<M, I>, S, T1, T2, T3> left(
                noinline f: (T1) -> Type.Kind2<M, I, S, T2>,
                noinline g: (T2) -> Type.Kind2<M, I, S, T3>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): (Type.Kind2<M, I, S, T1>) -> Type.Kind2<M, I, S, T3> =
                instance.then { it.fn<T1, S, T2>() to it.fn<T2, S, T3>() }
                        .then { it.first.bind(f) to it.second.bind(g) }
                        .then { p -> { m: Type.Kind2<M, I, S, T1> -> p.second(p.first(m)) } }
                        ?: throw IllegalStateException("instance information is not available.")

        inline fun <M: Monad, reified I: Monad.Kind1Instance<M, I>, T1, T2, T3> right(
                noinline f: (T1) -> Type.Kind1<M, I, T2>,
                noinline g: (T2) -> Type.Kind1<M, I, T3>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): (Type.Kind1<M, I, T1>) -> Type.Kind1<M, I, T3> =
                instance.then { i -> { m: Type.Kind1<M, I, T1> -> i.bind(m) { i.bind(f(it), g) } } }
                        ?: throw IllegalStateException("instance information is not available.")

        inline fun <M: Monad, reified I: Monad.Kind2Instance<M, I>, S, T1, T2, T3> right(
                noinline f: (T1) -> Type.Kind2<M, I, S, T2>,
                noinline g: (T2) -> Type.Kind2<M, I, S, T3>,
                kc: KClass<I> = I::class,
                instance: I? = kc.objectInstance
        ): (Type.Kind2<M, I, S, T1>) -> Type.Kind2<M, I, S, T3> =
                instance.then { i -> { m: Type.Kind2<M, I, S, T1> -> i.bind(m) { i.bind(f(it), g) } } }
                        ?: throw IllegalStateException("instance information is not available.")
    }
}

infix operator fun <P, Q, R, S, F:(P) -> Q, G: (Q,R) -> S> F.plus(g: G): (P, R) -> S = { p, r -> g(this(p), r) }

operator fun <P, Q, R, F:(P, Q) -> R> F.invoke(q: Q): (P) -> R = { p: P -> this(p, q) }
