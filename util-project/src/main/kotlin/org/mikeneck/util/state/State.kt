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
package org.mikeneck.util.state

import org.mikeneck.util.plus
import org.mikeneck.util.times

class State<S, out A>(val state: (S) -> Pair<A, S>) {

    fun eval(s: S): A = (state + { it.first }) * s

    fun exec(s: S): S = (state + { it.second }) * s

    fun runs(s: S): Pair<A, S> = state(s)

    fun <B> map(f: (A) -> B): State<S, B> = State { Pair(f(state(it).first), it) }

    fun <B> bind(f: (A) -> State<S, B>): State<S, B> =
            State { state(it).let { f(it.first).state(it.second) } }

    companion object {
        fun <S, A> ret(x: A): State<S, A> = State { x to it }

        fun <S> put(s: S): State<S, Unit> = State { Unit to s }

        fun <S> get(): State<S, S> = State { it to it }
    }
}
