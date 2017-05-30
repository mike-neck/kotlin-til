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

import com.example.annotations.ClassAnnotation
import java.io.Closeable

@ClassAnnotation
fun foo(bar: String): String = bar.toUpperCase()
@ClassAnnotation
val baz: Int = foo("baz").length

@ClassAnnotation
class Qux {
    @ClassAnnotation
    val prop: String = "ooo"
    @ClassAnnotation
    fun function(t: Int, msg: String): List<String> =
            if (t < 0) emptyList() else (1..t).map { "$msg$it" }
    @ClassAnnotation
    fun <R: Closeable> R.tryAndClose(action: (R) -> Unit) {
        try {
            action(this)
        } finally {
            this.close()
        }
    }
    @ClassAnnotation
    val Quux.id: String
        @ClassAnnotation
        get() = this.name
    @ClassAnnotation
    fun Qux.foop(): String = this.prop.plus(this.toString())
    @ClassAnnotation
    fun bool(b: Bool): Boolean = when(b) {
        Bool.OK -> true
        Bool.NG -> false
    }

    @ClassAnnotation
    companion object {
        @ClassAnnotation
        @JvmStatic fun add(q: Qux, qx: Quux): Int =
                q.prop.length + qx.name.length
        @ClassAnnotation
        fun garply(q: Quux): Garply = @ClassAnnotation object: Garply {
            override val waldo: String = q.name
        }
        @ClassAnnotation
        fun boolean(b: Bool): String = when (b) {
            Bool.OK -> "true"
            Bool.NG -> "false"
        }
    }
}

@ClassAnnotation
class Quux(val name: String) {
    @ClassAnnotation
    constructor(q: Qux): this(q.prop)
    @ClassAnnotation
    class NotInner
    @ClassAnnotation
    fun createInner(): Any {
        @ClassAnnotation
        class Inner
        return Inner()
    }
}

@ClassAnnotation
interface Garply {
    @ClassAnnotation
    val waldo: String
    @ClassAnnotation
    fun lorem(q: Qux): Quux = Quux(q.prop)
    @ClassAnnotation
    val dp: Int
        @ClassAnnotation
        get() = 1
    @ClassAnnotation
    companion object: Garply {
        @ClassAnnotation
        override val waldo: String = "waldo"
    }
}

@ClassAnnotation
val garply = @ClassAnnotation object : Garply {
    @ClassAnnotation
    override val waldo: String get() = "waldo"
}

@ClassAnnotation
data class Impl(override val waldo: String): Garply

@ClassAnnotation
object Vip

@ClassAnnotation
annotation class Ano(val name: String)

@ClassAnnotation
enum class Ord {
    @ClassAnnotation
    LT,
    @ClassAnnotation
    EQ,
    @ClassAnnotation
    GT
}

@ClassAnnotation
enum class Bool(val asBoolean: Boolean) {
    @ClassAnnotation
    OK(true ){ @ClassAnnotation override val int: Int = 1 },
    @ClassAnnotation
    NG(false){ @ClassAnnotation override val int: Int = 0 };
    @ClassAnnotation
    abstract val int: Int

    @ClassAnnotation
    fun Vip.size(): Int = 10
    @ClassAnnotation
    val Ano.size: Int
        @ClassAnnotation
        get() = this.name.length
    @ClassAnnotation
    companion object {
        @ClassAnnotation
        fun list(): List<Bool> = values().toList()
    }
}

@ClassAnnotation
sealed class Sealed
@ClassAnnotation
class Final: Sealed()
