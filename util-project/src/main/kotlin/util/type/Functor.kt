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

interface Functor

interface FunctorInstance<F: Functor, I: FunctorInstance<F, I>>: Instance<F> {

    fun <T, R> map(value: Bind<I, F, T>, func: (T) -> R): Bind<I, F, R>
}

interface FunctorInstance2<F: Functor, I: FunctorInstance2<F, I>>: Instance<F> {

    fun <A, T, R> map(value: Bind2<I, F, A, T>, func: (T) -> R): Bind2<I, F, A, R>
}
