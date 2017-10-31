@file:JvmName("Exercise1")
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
package com.example.ex1

fun main(args: Array<String>) {
    val text = message.whenNotNull { println(it) }
            .or { "hello" }
    println("result -> $text")
}

val message: String? = "exercise 1"

inline fun <A> A?.whenNotNull(f: (A) -> Unit): A? = if (this != null) this.apply(f) else this

fun <A> A?.or(g: () -> A): A = this ?: g()
