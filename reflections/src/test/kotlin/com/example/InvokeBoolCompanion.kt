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

// 強引に companion object の異なるインスタンスを取得する
fun main(args: Array<String>) {
    val loader = Bool::class.java.classLoader
    val defaultConstructorMarker = loader.loadClass("kotlin.jvm.internal.DefaultConstructorMarker")
    val constructor = Bool.Companion::class.java.getConstructor(defaultConstructorMarker)
    val boolCompanion: Bool.Companion = constructor.newInstance(defaultConstructorMarker.cast(null))
    println(Bool.list() == boolCompanion.list())
}
