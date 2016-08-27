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
package util

import util.type.Monad
import util.type.Type
import kotlin.reflect.KClass

interface AktType: Monad

sealed class Akt<M: Monad, out I: Type.Instance, out K: Type<T>, T> {

    abstract val back: I
    abstract val getter: () -> K

    class Kind1<M: Monad, I: Monad.Kind1Instance<M, I>, T>(
            override val back: I,
            override val getter: () -> Type.Kind1<M, I, T>): Akt<M, I, Type.Kind1<M, I, T>, T>() {

        fun <R> take(f: (T) -> Type.Kind1<M, I, R>): Kind1<M, I, R> = Kind1(back) { back.bind(getter(), f) }
    }

    class Kind2<M: Monad, I: Monad.Kind2Instance<M, I>, S, T>(
            override val back: I,
            override val getter: () -> Type.Kind2<M, I, S, T>): Akt<M, I, Type.Kind2<M, I, S, T>, T>() {

        fun <R> take(f: (T) -> Type.Kind2<M, I, S, R>): Kind2<M, I, S, R> = Kind2(back) { back.bind(getter(), f) }
    }

    class Kind3<M: Monad, I: Monad.Kind3Instance<M, I>, S1, S2, T>(
            override val back: I,
            override val getter: () -> Type.Kind3<M, I, S1, S2, T>): Akt<M, I, Type.Kind3<M, I, S1, S2, T>, T>() {

        fun <R> take(f: (T) -> Type.Kind3<M, I, S1, S2, R>): Kind3<M, I, S1, S2, R> = Kind3(back) { back.bind(getter(), f) }
    }

    companion object {
        fun illegal(mc: KClass<*>, ic: KClass<*>): Nothing =
                throw IllegalStateException("Monad instance for $mc(thus $ic) is not found.")
    }
}

inline fun <reified M: Monad, reified I: Monad.Kind1Instance<M, I>, T1, T2, T3> akt(
        noinline f1: () -> Type.Kind1<M, I, T1>,
        noinline f2: (T1) -> Type.Kind1<M, I, T2>,
        noinline f3: (T2) -> Type.Kind1<M, I, T3>,
        ic: KClass<I> = I::class,
        mc: KClass<M> = M::class,
        instance: I? = ic.objectInstance
): Type.Kind1<M, I, T3> =
        instance.then { Akt.Kind1(it, f1) }
                .then { it.take(f2) }
                .then { it.take(f3) }
                .then { it.getter() } ?: Akt.illegal(mc, ic)

inline fun <reified M: Monad, reified I: Monad.Kind2Instance<M, I>, S, T1, T2, T3> akt(
        noinline f1: () -> Type.Kind2<M, I, S, T1>,
        noinline f2: (T1) -> Type.Kind2<M, I, S, T2>,
        noinline f3: (T2) -> Type.Kind2<M, I, S, T3>,
        ic: KClass<I> = I::class,
        mc: KClass<M> = M::class,
        instance: I? = ic.objectInstance
): Type.Kind2<M, I, S, T3> =
        instance.then { Akt.Kind2(it, f1) }
                .then { it.take(f2) }
                .then { it.take(f3) }
                .then { it.getter() } ?: Akt.illegal(mc, ic)
