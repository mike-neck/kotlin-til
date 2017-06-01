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

import com.example.Property.Companion.prop
import java.util.*

object ListCons {
    val strings: List<String> = listOf("foo", "bar")

    infix operator fun <T: Any> List<T>.rem(e: T): List<T> = this.toMutableList().also { it.add(e) }.toList()

    infix operator fun <T: Any> T.rem(e: T): List<T> = listOf(this, e)

    val nextVersion: List<Int> = emptyList<Int>() % 1 % 2 % 3

    val property: List<Property<*>> = 
            (prop("positive int is greater than 0") forAll { Random().nextInt(200) + 1 } satisfy { it > 0 }) %
                    (prop("negative int is less than 0") forAll { 0 - Random().nextInt(200) } satisfy { it < 0 }) %
                    (prop("integer powered by two is greater than 0")
                            forAll { Random().nextInt(400) - 200 } satisfy { it * it > 0 })

    @JvmStatic
    fun main(args: Array<String>) {
        println(nextVersion)
    }
}

class Property<T>(val theme: String, val expression: () -> T, val property: (T) -> Unit) {

    companion object {
        fun prop(theme: String): Prop = Prop(theme)
    }
    class Prop(val theme: String) {
        infix fun <T> forAll(expression: () -> T): Builder<T> = object: Builder<T> {
            override fun satisfy(property: (T) -> Unit): Property<T> = Property(theme, expression, property)
        }
    }
    interface Builder<T> {
        infix fun satisfy(property: (T) -> Unit): Property<T>
    } 
}
