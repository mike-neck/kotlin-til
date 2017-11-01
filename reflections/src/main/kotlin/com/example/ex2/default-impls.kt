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
package com.example.ex2

interface NamedFile {
    val name: String
    val extension: String
    fun preciseName(): String = "$name.$extension"
    companion object {
        fun create(name: String, extension: String): NamedFile = object : NamedFile {
            override val name: String get() = name
            override val extension: String get() = extension
        }
    }
}
