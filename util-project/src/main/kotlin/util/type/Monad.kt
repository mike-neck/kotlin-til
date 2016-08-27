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

import util.type.Type.*

interface Monad: Functor {

    interface Kind1Instance<M: Monad, I: Kind1Instance<M, I>>: Functor.Kind1Instance<M, I> {
        fun <T, R> bind(obj: Kind1<M, I, T>, func: (T) -> Kind1<M, I, R>): Kind1<M, I, R>
        fun <T> pure(value: T): Kind1<M, I, T>
        override fun <T, R> fn(): Support<M, I, T, R>
        interface Support<M: Monad, I: Kind1Instance<M, I>, T, R>: Functor.Kind1Instance.Support<M, I, T, R> {
            val bind: (Kind1<M, I, T>, (T) -> Kind1<M, I, R>) -> Kind1<M, I, R>
            val pure: (T) -> Kind1<M, I, T>
        }
    }

    interface Kind2Instance<M: Monad, I: Kind2Instance<M, I>>: Functor.Kind2Instance<M, I> {
        fun <T, S, R> bind(obj: Kind2<M, I, S, T>, func: (T) -> Kind2<M, I, S, R>): Kind2<M, I, S, R>
        fun <T, S> pure(value: T): Kind2<M, I, S, T>
        override fun <T, S, R> fn(): Support<M, I, T, S, R>
        interface Support<M: Monad, I: Kind2Instance<M, I>, T, S, R>: Functor.Kind2Instance.Support<M, I, T, S, R> {
            val bind: (Kind2<M, I, S, T>, (T) -> Kind2<M, I, S, R>) -> Kind2<M, I, S, R>
            val pure: (T) -> Kind2<M, I, S, T>
        }
    }

    interface Kind3Instance<M: Monad, I: Kind3Instance<M, I>>: Functor.Kind3Instance<M, I> {
        fun <T, S1, S2, R> bind(obj: Kind3<M, I, S1, S2, T>, func: (T) -> Kind3<M, I, S1, S2, R>): Kind3<M, I, S1, S2, R>
        fun <T, S1, S2> pure(value: T): Kind3<M, I, S1, S2, T>
    }
}

