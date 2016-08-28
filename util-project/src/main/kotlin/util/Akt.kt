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

sealed class Akt<M: Monad, out I: Type.Instance, out K: Type<T>, T>: () -> K {

    abstract val back: I
    abstract val getter: () -> K

    override fun invoke(): K = getter()

    class Kind1<M: Monad, I: Monad.Kind1Instance<M, I>, T> (
            override val back: I,
            override val getter: () -> Type.Kind1<M, I, T>): Akt<M, I, Type.Kind1<M, I, T>, T>() {

        internal fun <R> take(f: (T) -> Type.Kind1<M, I, R>): Kind1<M, I, R> = Kind1(back) { back.bind(getter(), f) }

        operator fun <R> get(f: (T) -> Type.Kind1<M, I, R>): Kind1<M, I, R> = take(f)
    }

    class Kind2<M: Monad, I: Monad.Kind2Instance<M, I>, S, T>(
            override val back: I,
            override val getter: () -> Type.Kind2<M, I, S, T>): Akt<M, I, Type.Kind2<M, I, S, T>, T>() {

        internal fun <R> take(f: (T) -> Type.Kind2<M, I, S, R>): Kind2<M, I, S, R> = Kind2(back) { back.bind(getter(), f) }

        operator fun <R> get(f: (T) -> Type.Kind2<M, I, S, R>): Kind2<M, I, S, R> = Kind2(back) { back.bind(getter(), f) }
    }

    class Kind3<M: Monad, I: Monad.Kind3Instance<M, I>, S1, S2, T>(
            override val back: I,
            override val getter: () -> Type.Kind3<M, I, S1, S2, T>): Akt<M, I, Type.Kind3<M, I, S1, S2, T>, T>() {

        internal fun <R> take(f: (T) -> Type.Kind3<M, I, S1, S2, R>): Kind3<M, I, S1, S2, R> = Kind3(back) { back.bind(getter(), f) }

        operator fun <R> get(f: (T) -> Type.Kind3<M, I, S1, S2, R>): Kind3<M, I, S1, S2, R> = Kind3(back) { back.bind(getter(), f) }
    }

    companion object {
        fun illegal(mc: KClass<*>, ic: KClass<*>): Nothing =
                throw IllegalStateException("Monad instance for $mc(thus $ic) is not found.")
    }
}

inline fun <reified M: Monad, reified I: Monad.Kind1Instance<M, I>, T> akt(
        m: Type.Kind1<M, I, T>,
        ic: KClass<I> = I::class,
        mc: KClass<M> = M::class,
        instance: I? = ic.objectInstance
): Akt.Kind1<M, I, T>  = instance.then { Akt.Kind1(it) { m } } ?: Akt.illegal(mc, ic)

inline fun <reified M: Monad, reified I: Monad.Kind2Instance<M, I>, S, T> akt(
        m: Type.Kind2<M, I, S, T>,
        ic: KClass<I> = I::class,
        mc: KClass<M> = M::class,
        instance: I? = ic.objectInstance
): Akt.Kind2<M, I, S, T> = instance.then { Akt.Kind2(it) { m } } ?: Akt.illegal(mc, ic)


