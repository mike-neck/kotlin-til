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

import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

//import org.junit.jupiter.api.Test

class KotlinFilesTest {

//    @Test
    fun classes(): Unit {
        val forName = Class.forName("com.example.KotlinClasses")
        println(forName)
    }

    class Visitor(val root: Path): FileVisitor<Path> {

        val list: MutableList<Path> = mutableListOf()

        override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult = FileVisitResult.CONTINUE

        override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult =
                if (exc == null) FileVisitResult.CONTINUE
                else throw exc

        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult =
                FileVisitResult.CONTINUE
                        .also { if (file != null && file.fileName.toString().endsWith(".class"))
                            list.add(root.relativize(file)) }

        override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult =
                if (exc == null) FileVisitResult.CONTINUE
                else throw exc
    }

    class ClassAnalyze(val kClass: KClass<*>) {
        val abstract: Try<Boolean> get() = try { Succeed(kClass.isAbstract) } catch (e: UnsupportedOperationException) { Fail() }
        val final: Try<Boolean> get() = try { Succeed(kClass.isFinal) } catch (e: UnsupportedOperationException) { Fail() }
        val companion: Try<Boolean> get() = try { Succeed(kClass.isCompanion) } catch (e: UnsupportedOperationException) { Fail() }
        val data: Try<Boolean> get() = try { Succeed(kClass.isData) } catch (e: UnsupportedOperationException) { Fail() }
        val open: Try<Boolean> get() = try { Succeed(kClass.isOpen) } catch (e: UnsupportedOperationException) { Fail() }
        val sealed: Try<Boolean> get() = try { Succeed(kClass.isSealed) } catch (e: UnsupportedOperationException) { Fail() }
        val inner: Try<Boolean> get() = try { Succeed(kClass.isInner) } catch (e: UnsupportedOperationException) { Fail() }

        override fun toString(): String =
                "ClassAnalyze(class[${kClass.jvmName}]->" +
                        "abstract[$abstract]," +
                        "final[$final]," +
                        "companion[$companion]," +
                        "data[$data]," +
                        "open[$open]," +
                        "sealed[$sealed]," +
                        "inner[$inner]" +
                        ")"
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val location = Ano::class.java.protectionDomain.codeSource.location
            println(location)
            val root = Paths.get(location.toURI())
            val visitor = Visitor(root)
            Files.walkFileTree(root, visitor)
            visitor.list
                    .map { "$it".replace(".class", "").replace("/",".") }
                    .map { Class.forName(it) }
                    .map { it.kotlin }
                    .map { ClassAnalyze(it) }
                    .forEach(::println)
        }
    }
}

sealed class Try<out T> {
    abstract val error: Boolean
}

class Fail<out T>: Try<T>() {
    override val error: Boolean = false
    override fun toString(): String = "Fail"
}

class Succeed<out T>(val boolean: T): Try<T>() {
    override val error: Boolean = true
    override fun toString(): String = "Succ($boolean)"
}
