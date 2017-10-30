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

import kotlin.reflect.KClass

data class TypeInfo<out T : Any>(val classifier: Classifier, val parameters: List<Classifier>, val nullable: Boolean) {
    constructor(classifier: Classifier, nullable: Boolean): this(classifier, emptyList(), nullable)
    @Suppress("UNCHECKED_CAST")
    constructor(kClass: KClass<T>): this(Classifier(kClass as KClass<Any>), false)
    @Suppress("UNCHECKED_CAST")
    constructor(kClass: KClass<T>, parameters: List<Classifier>): this(Classifier(kClass as KClass<Any>), parameters, false)

    override fun toString(): String = classifier.asString +
            if (parameters.isEmpty()) "" else "<${parameters.map(Classifier::asString).joinToString(", ")}>" +
                    if (nullable) "?" else ""
}

data class Classifier (val asKClass: KClass<*>) {
    val asString: String = asKClass.qualifiedName?: "${asKClass.simpleName}"
}

object CheckClassifier {
    @JvmStatic
    fun main(args: Array<String>) {
        val string: TypeInfo<String> = TypeInfo(String::class)
        val array: TypeInfo<Array<String>> = TypeInfo(arrayOf<String>()::class, listOf(Classifier(String::class)))
        println(string)
        println(array)
    }
}
