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

import util.type.Monad
import util.type.Type

interface StateType: Monad

typealias State<S, T> = Type.Kind2<StateType, StateImpl.Companion, S, T>

typealias StateSupport<S, T, R> = Monad.Kind2Instance.Support<StateType, StateImpl.Companion, T, S, R>

typealias StateMonad = StateImpl.Companion

class StateImpl<S, T>(val state: (S) -> Pair<T, S>): Type.Kind2<StateType, StateImpl.Companion, S, T>() {

    val eval: (S) -> T = { state(it).first }

    val exec: (S) -> S = { state(it).second }

    companion object: Monad.Kind2Instance<StateType, StateImpl.Companion> {

        override fun <T, S, R> fn(): StateSupport<S, T, R> =
                object: StateSupport<S, T, R> {

                    override val map: (State<S, T>, (T) -> R) -> State<S, R>
                        get() = { o, f -> Companion.map(o, f) }

                    override val bind: (State<S, T>, (T) -> State<S, R>) -> State<S, R>
                        get() = { o, f -> Companion.bind(o, f) }

                    override val pure: (T) -> State<S, T>
                        get() = { o -> Companion.pure(o) }
                }

        override fun <T, S, R> map(obj: State<S, T>, func: (T) -> R): State<S, R> =
                if (obj is StateImpl) StateImpl { obj.state(it).let { func(it.first) to it.second } }
                else illegalArgument(obj)

        override fun <T, S, R> bind(obj: State<S, T>, func: (T) -> State<S, R>): State<S, R> =
                if (obj is StateImpl) StateImpl { obj.state(it)
                        .let { it.second to func(it.first) as StateImpl<S, R> }
                        .let { it.second.state(it.first) } }
                else illegalArgument(obj)

        override fun <T, S> pure(value: T): State<S, T> = StateImpl { value to it }

        fun <T, S> state(f: (S) -> Pair<T, S>): State<S, T> = StateImpl(f)

        fun <T, S> runState(state: State<S, T>, initial: S): Pair<T, S> =
                if (state is StateImpl) state.state(initial)
                else throw IllegalArgumentException("argument is not State.")

        fun <T, S> evalState(state: State<S, T>, initial: S): T =
                if (state is StateImpl) state.eval(initial)
                else throw IllegalArgumentException("argument is not State.")

        fun <T, S> execState(state: State<S, T>, initial: S): S =
                if (state is StateImpl) state.exec(initial)
                else throw IllegalArgumentException("argument is not State.")

        fun <S> get(): State<S, S> = StateImpl { it to it }

        fun <S> put(st: S): State<S, Unit> = StateImpl { Unit to st }
    }
}
