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

interface Functor {
    interface Kind1Instance<C: Functor, I: Kind1Instance<C, I>>: Kind1.Instance<C, I> {
        fun <T, R> map(obj: Kind1<C, I, T>, func: (T) -> R): Kind1<C, I, R>
        fun <T, R> fn(): Support<C, I, T, R>
        interface Support<C: Functor, I: Kind1Instance<C, I>, T, R> {
            val map: (Kind1<C, I, T>, func: (T) -> R) -> Kind1<C, I, R>
        }
    }

    interface Kind2Instance<C: Functor, I: Kind2Instance<C, I>>: Kind2.Instance<C, I> {
        fun <T, S, R> map(obj: Kind2<C, I, S, T>, func: (T) -> R): Kind2<C, I, S, R>
        fun <T, S, R> fn(): Support<C, I, T, S, R>
        interface Support<C: Functor, I: Kind2Instance<C, I>, T, S, R> {
            val map: (Kind2<C, I, S, T>, func: (T) -> R) -> Kind2<C, I, S, R>
        }
    }

    interface Kind3Instance<C: Functor, I: Kind3Instance<C, I>>: Kind3.Instance<C, I> {
        fun <T, S1, S2, R> map(obj: Kind3<C, I, S1, S2, T>, func: (T) -> R): Kind3<C, I, S1, S2, R>
        fun <T, S1, S2, R> fn(): Support<C, I, T, S1, S2, R>
        interface Support<C: Functor, I: Kind3Instance<C, I>, T, S1, S2, R> {
            val map: (Kind3<C, I, S1, S2, T>, func: (T) -> R) -> Kind3<C, I, S1, S2, R>
        }
    }
}
