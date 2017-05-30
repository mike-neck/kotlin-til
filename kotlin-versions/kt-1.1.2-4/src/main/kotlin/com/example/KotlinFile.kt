/*
 * Copyright 2017 Shinya Mochida
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
@file:JvmName("KotlinClasses")

package com.example

import java.io.Closeable

fun foo(bar: String): String = bar.toUpperCase()
val baz: Int = foo("baz").length

class Qux {
    val prop: String = "ooo"
    fun function(t: Int, msg: String): List<String> =
            if (t < 0) emptyList() else (1..t).map { "$msg$it" }
    fun <R: Closeable> R.tryAndClose(action: (R) -> Unit) {
        try {
            action(this)
        } finally {
            this.close()
        }
    }
    val Quux.id: String
        get() = this.name
    fun Qux.foop(): String = this.prop.plus(this.toString())
    fun bool(b: Bool): Boolean = when(b) {
        Bool.OK -> true
        Bool.NG -> false
    }

    companion object {
        @JvmStatic fun add(q: Qux, qx: Quux): Int =
                q.prop.length + qx.name.length
        fun garply(q: Quux): Garply = object: Garply {
            override val waldo: String = q.name
        }
        fun boolean(b: Bool): String = when (b) {
            Bool.OK -> "true"
            Bool.NG -> "false"
        }
    }
}

class Quux(val name: String) {
    constructor(q: Qux): this(q.prop)
    class NotInner
    fun createInner(): Any {
        class Inner
        return Inner()
    }
}

interface Garply {
    val waldo: String
    fun lorem(q: Qux): Quux = Quux(q.prop)
    val dp: Int
        get() = 1
    companion object: Garply {
        override val waldo: String = "waldo"
    }
}

val garply = object : Garply {
    override val waldo: String get() = "waldo"
}

data class Impl(override val waldo: String): Garply

object Vip

annotation class Ano(val name: String)

enum class Ord {
    LT,EQ,GT
}
enum class Bool(val asBoolean: Boolean) {
    OK(true ){ override val int: Int = 1 },
    NG(false){ override val int: Int = 0 };
    abstract val int: Int

    fun Vip.size(): Int = 10
    val Ano.size: Int
        get() = this.name.length
    companion object {
        fun list(): List<Bool> = values().toList()
    }
}

sealed class Sealed
class Final: Sealed()
