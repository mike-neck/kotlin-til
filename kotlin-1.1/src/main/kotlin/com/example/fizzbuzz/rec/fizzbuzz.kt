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
package com.example.fizzbuzz.rec

sealed class Value {
    abstract val show: String
    abstract val next: Value
}

object Term: Value() {
    override val show: String get() = throw UnsupportedOperationException("This is term")
    override val next: Value get() = throw UnsupportedOperationException("This is term")
}

class Num(val value: Int, override val next: Value): Value() {
    override val show: String get() = "$value "
}
class Fizz(override val next: Value): Value() {
    override val show: String get() = "Fizz "
}
class Buzz(override val next: Value): Value() {
    override val show: String get() = "Buzz "
}
class FizzBuzz(override val next: Value): Value() {
    override val show: String get() = "FizzBuzz\n"
}

typealias Gen = (Value) -> Value

val num: (Int) -> (Gen) -> Gen = { n -> { f -> { v -> f(Num(n, v)) } } }
val fizz: (Gen) -> Gen = { f -> { v -> f(Fizz(v)) } }
val buzz: (Gen) -> Gen = { f -> { v -> f(Buzz(v)) } }
val fizzBuzz: (Gen) -> Gen = { f -> { v -> f(FizzBuzz(v)) } }

interface Succ<L: Succ<L>> {
    val next: L
}

sealed class Loop: Succ<Loop> {
    abstract val max: Int
    companion object: (Int) -> Loop {
        override operator fun invoke(max: Int): Loop = Mid(1, max)
    }
}

class Mid(val current: Int, override val max: Int): Loop() {
    override val next: Loop get() = if (current == max - 1) End(max) else Mid(current + 1, max)
}
class End(override val max: Int): Loop() {
    override val next: Loop get() = Mid(1, max)
}

typealias Count = Loop

tailrec fun run(count: Count, three: Loop = Loop(3), five: Loop = Loop(5), result: Gen = id()): Value =
        when(count) {
            is End -> result(Term)
            is Mid -> when(five) {
                is End -> when(three) {
                    is End -> run(count.next, three.next, five.next, fizzBuzz(result))
                    is Mid -> run(count.next, three.next, five.next, buzz(result))
                }
                is Mid -> when(three) {
                    is End -> run(count.next, three.next, five.next, fizz(result))
                    is Mid -> run(count.next, three.next, five.next, num(count.current)(result))
                }
            }
        }

tailrec fun show(value: Value): Unit = when(value) {
    is Term -> Unit
    else    -> show(value.next.apply { print(value.show) })
}

fun mkValue(count: Count): Value = run(count)

fun from1(max: Int): Count = Mid(1, max + 1)

val runApp: (Int) -> Unit = ::from1 + ::mkValue + ::show


fun main(args: Array<String>) = runApp(30)



inline infix operator fun <P, Q, R> ((P) -> Q).plus(crossinline f: (Q) -> R): (P) -> R = { f(this(it)) }

fun <P> id(): (P) -> P = { it }

