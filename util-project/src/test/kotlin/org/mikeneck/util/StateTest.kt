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

import org.junit.Test
import org.mikeneck.util.Command.*
import org.mikeneck.util.state.State
import org.mikeneck.util.state.State.Companion.get
import org.mikeneck.util.state.State.Companion.put
import org.mikeneck.util.state.State.Companion.ret
import java.util.*

class StateTest {

    infix fun <T> T.shouldBe(exp: T): Unit =
            { "Expected [$exp] got [$this]" }
                    .let { if (this == exp) Unit else throw AssertionError(it) }

    val initial = true to 0

    @Test fun game1() =
            play(listOf(ADD, SUB, ADD, ADD)).eval(initial) shouldBe 2

    @Test fun game2() =
            play(listOf()).eval(initial) shouldBe 0

    @Test fun game3() =
            play(listOf(MOD, ADD, MOD, SUB, SUB, SUB)).eval(initial) shouldBe -3

    @Test fun bind() =
            get<Int>()
                    .bind { put(it) }
                    .bind { ret<Int, Int>(1) }.runs(3) shouldBe (1 to 3)

    @Test
    fun bind2() =
            get<Int>()
                    .bind { x -> put(x + 1).bind { ret<Int, Int>(x + 1) } }
                    .bind { get<Int>() }
                    .bind { y -> put(y * 2).bind { ret<Int, Int>(y * 2) } }
                    .state(1) shouldBe (4 to 4)
}

enum class Command {
    ADD, SUB, MOD
}

sealed class Contents<out E> {
    abstract val head: E
    abstract val tail: List<E>
    class Empty<out E>: Contents<E>() {
        override val head: E get() = throw NoSuchElementException("empty")
        override val tail: List<E> get() = throw NoSuchElementException("empty")
    }
    class Full<out E>(
            override val head: E,
            override val tail: List<E>): Contents<E>()
}

fun <E> list(cs: List<E>): Contents<E> =
        when (cs.size) {
            0    -> Contents.Empty()
            else -> Contents.Full(cs.first(), cs.drop(1))
        }

fun play(cs: List<Command>): State<Pair<Boolean, Int>, Int> =
        list(cs).let { c ->
            when (c) {
                is Contents.Empty -> get<Pair<Boolean,Int>>()
                        .bind { ret<Pair<Boolean, Int>, Int>(it.second) }
                is Contents.Full  -> calc(c.head).bind { play(c.tail) }
            }
        }

fun calc(c: Command): State<Pair<Boolean, Int>, Int> =
        get<Pair<Boolean, Int>>()
                .map { when(c) {
                    Command.ADD -> if (it.first) it.first to it.second + 1 else it
                    Command.SUB -> if (it.first) it.first to it.second - 1 else it
                    Command.MOD -> !it.first to it.second
                } }
                .bind { p -> put(p).bind { ret<Pair<Boolean, Int>, Int>(p.second) } }
