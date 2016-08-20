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
package org.mikeneck.util

import java.util.*

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

fun <T> T?.either(): Either<Unit, T> = if (this == null) Either.Left({ Unit }) else Either.Right<Unit, T>({ this })

fun <T, L> T?.either(f: () -> L): Either<L, T> = if (this == null) Either.Left(f) else Either.Right<L, T>({ this })

fun <R> Either<R, R>.result(): R = when (this) {
    is Either.Left ->  this.left
    is Either.Right -> this.right
}

fun <E: Throwable, T> Either<E, T>.get(): T = when (this) {
    is Either.Left  -> throw this.left
    is Either.Right -> this.right
}

infix operator fun <P, R, F: () -> P> F.plus(f: (P) -> R): () -> R = { f(this()) }

infix operator fun <P, Q, R, F: (P) -> Q> F.plus(f: (Q) -> R): (P) -> R = { f(this(it)) }

sealed class Either<L, R> {

    abstract val left: L
    abstract val right: R

    abstract fun <T> map(f: (R) -> T): Either<L, T>

    abstract fun <T> fmap(f: (R) -> Either<L, T>): Either<L, T>

    fun orThrow(f: (L) -> Throwable): R = when(this) {
        is Either.Left ->  throw f(this.left)
        is Either.Right -> this.right
    }

    fun or(f: (L) -> R): R = when(this) {
        is Either.Left  -> this.left.let(f)
        is Either.Right -> this.right
    }

    class Left<L, R>(val lf: () -> L): Either<L, R>() {

        constructor(l: L): this ({ l })

        override val left: L by lazy(lf)

        override val right: R
            get() = throw NoSuchElementException("this is left.")

        override fun <T> map(f: (R) -> T): Either<L, T> = Left(lf)

        override fun <T> fmap(f: (R) -> Either<L, T>): Either<L, T> = Left(lf)
    }

    class Right<L, R>(val rf: () -> R): Either<L, R>() {

        constructor(r: R): this ({ r })

        override val right: R by lazy(rf)

        override val left: L
            get() = throw NoSuchElementException("this is right.")

        override fun <T> map(f: (R) -> T): Either<L, T> = Right(rf + f)

        override fun <T> fmap(f: (R) -> Either<L, T>): Either<L, T> = (rf + f)()
    }
}
