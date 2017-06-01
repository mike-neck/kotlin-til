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

object ListCons {
    val strings: List<String> = listOf("foo", "bar")

    infix operator fun <T: Any> List<T>.rem(e: T): List<T> = this.toMutableList().also { it.add(e) }.toList()

    val nextVersion: List<Int> = emptyList<Int>() % 1 % 2 % 3

    @JvmStatic
    fun main(args: Array<String>) {
        println(nextVersion)
    }
}
