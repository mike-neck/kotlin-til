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

infix fun <T,R> T?.then(f: (T) -> R?): R? = if (this == null) null else f(this)

infix fun <T> T?.unit(f: (T) -> Unit): Unit { if (this != null) f(this) }

val Any?.unit: Unit get() { @Suppress("UNUSED_EXPRESSION")this; Unit }

infix fun <T> T.initBy(c: (T) -> Unit): T {
    c(this)
    return this
}

fun <T,R> T.initialize(g: () -> R, f: (T,R) -> Unit): R = g() initBy { f(this, it) }

infix fun <T, R> T.and(r: R): Unit = this.let { @Suppress("UNUSED_EXPRESSION")this;r }.unit

infix fun <F, S> Array<F>.comb(ss: Array<S>): Iterable<Pair<F, S>> = this comb ss.toList()

infix fun <F, S> Iterable<F>.comb(ss: Array<S>): Iterable<Pair<F, S>> = this comb ss.toList()

infix fun <F, S> Array<F>.comb(ss: Iterable<S>): Iterable<Pair<F, S>> = this.toList() comb ss

infix fun <F, S> Iterable<F>.comb(ss: Iterable<S>): Iterable<Pair<F, S>> = this.flatMap {f -> ss.map { f to it } }

infix operator fun <P, R, F: () -> P> F.plus(f: (P) -> R): () -> R = { f(this()) }

infix operator fun <P, Q, R, F: (P) -> Q> F.plus(f: (Q) -> R): (P) -> R = { f(this(it)) }

infix operator fun <P, Q, F: (P) -> Q> F.times(p: P): Q = this(p)
