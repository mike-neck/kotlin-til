@file:JvmName("Compiled")
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
package com.example

import java.io.Closeable

@JvmName("upper")
fun toUpper(string: String): String = string.toUpperCase()

val fooLength: Int = toUpper("foo").length

class Comp {
    val prop: String = "ooo"
    fun function(t: Int, msg: String): List<String> = if (t <= 0) emptyList() else (1..t).map { "$msg$it" }
    fun <R: Closeable> R.tryAndClose(action: (R) -> Unit): Unit  = this.use(action)
    val Cls.id: String
        get() = this.name
    fun Cls.foop(): String = this@Comp.prop.plus(this.toString())

    companion object {
        @JvmStatic
        fun add(c: Comp, cls: Cls): Int = c.prop.length + cls.name.length

        fun cly(cls: Cls): Kls = object : Kls {
            override val name: String = cls.name
        }

        fun boolean(b: Bool): String = when (b) {
            Bool.NG -> "false"
            Bool.OK -> "true"
        }
    }
}

class Cls(val name: String) {
    constructor(comp: Comp): this(comp.prop)
}

interface Kls {
    val name: String
    fun comp(c: Comp): Cls = Cls(c)
    val len: Int
        get() = 1
    companion object: Kls {
        override val name: String = "name"
    }
}

object Obj

annotation class Ano(val name: String)

enum class Ord {
    LT,EQ,GT
}

enum class Bool(val asBoolean: Boolean) {
    OK(true) { override val int: Int = 1 },
    NG(true) { override val int: Int = 0 };
    abstract val int: Int

    fun Obj.size(): Int = 10
    val Ano.size: Int
        get() = this.name.length

    companion object {
        fun list(): Set<Bool> = values().toSet()
    }
}

val Set<Bool>.asList: List<Bool> get() = this.map { it }
