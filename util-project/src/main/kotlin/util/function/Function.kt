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
package util.function

fun <T> id(value: T): T = value

fun <T> id(): (T) -> T = { it }

fun <F, S> const(first: F, @Suppress("UNUSED_PARAMETER") second: S): F = first

fun <F, S> const(): (F, S) -> F = { f, s -> f }

/**
 * currying functions
 */
val <P1, P2, R, F:(P1,P2) -> R> F.curry: (P1) -> ((P2) -> R) get() = { p1 -> { p2 -> this(p1, p2) } }

val <P1, P2, R, F:(P1) -> ((P2) -> R)> F.uncurry: (P1, P2) -> R get() = { p1, p2 -> this(p1)(p2) }

object Fun {
    fun curry(n: Int) =
            (1..n).map { "p$it" to "P$it" }
}

