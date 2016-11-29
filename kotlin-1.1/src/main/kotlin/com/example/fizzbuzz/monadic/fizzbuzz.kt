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
package com.example.fizzbuzz.monadic

sealed class Choice<L, R>

data class Fixed<L, R>(val value: L): Choice<L, R>()
data class Fluid<L, R>(val value: R): Choice<L, R>()


sealed class Value

class Fizz: Value()
class Buzz: Value()
class FizzBuzz: Value()
class Num(val num: Int): Value()

typealias Action<L, A, B> = (A) -> Choice<L, B>
data class Kleisli<L, in A, B>(val runKleisli: (A) -> Choice<L, B>)



object ChoiceMonad {

    fun <L, A, B> fmap(): ((A) -> B) -> (Choice<L, A>) -> Choice<L, B> = { f -> { ch -> when(ch) {
        is Fixed -> Fixed(ch.value)
        is Fluid -> Fluid(f(ch.value))
    } } }

    fun <L, R> pure(): (R) -> Choice<L, R> = ::Fluid

    fun <L, A, B> bind(): (Choice<L, A>) -> ((A) -> Choice<L, B>) -> Choice<L, B> = { ch -> { f -> when(ch) {
        is Fixed -> Fixed(ch.value)
        is Fluid -> f(ch.value)
    } } }
}

object KleisliMonoid {
    typealias Kl<L, A, B> = Kleisli<L, A, B>
    val m = ChoiceMonad
    fun <L, A> mempty(): Kl<L, A, A> = Kleisli(ChoiceMonad.pure())
    fun <L, A> mappend(): (Kl<L, A, A>) -> (Kl<L, A, A>) -> Kl<L, A, A> = { (f) -> { (g) -> Kleisli { a -> m.bind<L, A, A>()(f(a))(g) } } }
}

object KleisliFoldable {

    typealias ToKleisli<L, A> = (Action<L, A, A>) -> Kleisli<L, A, A>

    fun <L, A> foldMap(): ((Action<L, A, A>) -> Kleisli<L, A, A>) -> (List<Action<L, A, A>>) -> Action<L, A, A> = TODO()

    
}

object ChoiceObj {

    val multipleOf: (Int) -> (Int) -> Boolean = { n -> { it % n == 0 } }

}


inline infix fun <A, B> A.`$`(f: (A) -> B): B = f(this)

inline infix operator fun <A, B, C> ((A) -> B).plus(crossinline f: (B) -> C): (A) -> C = { a -> f(this(a)) }


