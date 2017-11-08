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
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor

class Reflections

val classLoader: ClassLoader = Reflections::class.java.classLoader

fun main(args: Array<String>) {
    val kClass: KClass<Reflections> = Reflections::class
    println(kClass.java.protectionDomain.codeSource.location)
    inspect("com.example.Kls")
    inspect("com.example.Kls${'$'}DefaultImpls")
    inspect("com.example.Bool")
    inspect("com.example.Bool${'$'}OK")
    inspect("com.example.Compiled")
    inspect("com.example.Comp${'$'}Companion${'$'}WhenMappings")

    val kc = Ref::class
    val primaryConstructor = kc.primaryConstructor
    if (primaryConstructor != null) {
        val ref = primaryConstructor.call("foo")
        println(ref)
    }
    println(Ref.Companion)
}

private fun inspect(className: String) {
    val klass = classLoader.loadClass(className)
    println(klass)
    val kClass: KClass<*> = klass.kotlin
    println(kClass)

    kClass.exceptionallyRun("constructors", KClass<*>::constructors)
    kClass.exceptionallyRun("isAbstract", KClass<*>::isAbstract)
    kClass.exceptionallyRun("isCompanion", KClass<*>::isCompanion)
    kClass.exceptionallyRun("isData", KClass<*>::isData)
    kClass.exceptionallyRun("isFinal", KClass<*>::isFinal)
    kClass.exceptionallyRun("isInner", KClass<*>::isInner)
    kClass.exceptionallyRun("isOpen", KClass<*>::isOpen)
    kClass.exceptionallyRun("isSealed", KClass<*>::isSealed)
    kClass.exceptionallyRun("visibility", KClass<*>::visibility)
    kClass.exceptionallyRun("members", KClass<*>::members)
    kClass.exceptionallyRun("declaredFunctions", KClass<*>::declaredFunctions)
    println()
}

inline fun <A: Any, B> A.exceptionallyRun(message: String, action: (A) -> B) = try {
    println(message)
    val result = action(this)
    println("$message -> $result")
} catch (e: Throwable) {
    println("thrown ${e.javaClass.canonicalName}")
    println(e.message)
}

data class Ref(val value: String) {
    companion object
}
