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

import org.junit.jupiter.api.Test
import java.io.*
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.staticFunctions
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
        val java: Class<*> get() = kClass.java

        val abstract: Try<Boolean> get() = Try.of { kClass.isAbstract }
        val final: Try<Boolean> get() = Try.of { kClass.isFinal }
        val companion: Try<Boolean> get() = Try.of { kClass.isCompanion }
        val data: Try<Boolean> get() = Try.of { kClass.isData }
        val open: Try<Boolean> get() = Try.of { kClass.isOpen }
        val sealed: Try<Boolean> get() = Try.of { kClass.isSealed }
        val inner: Try<Boolean> get() = Try.of { kClass.isInner }

        val annotations: Try<Int> get() = Try.of { kClass.annotations.size }
        val constructors: Try<Int> get() = Try.of { kClass.constructors.size }
        val members: Try<Int> get() = Try.of { kClass.members.size }
        val functions: Try<Int> get() = Try.of { kClass.functions.size }
        val memberProperties: Try<Int> get() = Try.of { kClass.memberProperties.size }
        val objectInstance: Try<Boolean> get() = Try.of { kClass.objectInstance?.let { true }?: false }

        val primaryCtor: Try<Boolean> get() = Try.of { kClass.primaryConstructor?.let { true }?: false }
        val nestedClass: Try<Int> get() = Try.of { kClass.nestedClasses.size }
        val staticFunctions: Try<Int> get() = Try.of { kClass.staticFunctions.size }

        val annotationClass: Try<Boolean> get() = Try.of { java.isAnnotation }
        val anonymous: Try<Boolean> get() = Try.of { java.isAnonymousClass }

        override fun toString(): String =
                "ClassAnalyze " +
                        "[${kClass.jvmName}]\n" +
                        "-- KClass inspection --\n" +
                        "  abstract[$abstract], " +
                        "final[$final], " +
                        "companion[$companion], " +
                        "data[$data]\n" +
                        "  open[$open], " +
                        "sealed[$sealed], " +
                        "inner[$inner]\n" +
                        "  annotations[$annotations], " +
                        "constructors[$constructors], " +
                        "members[$members]\n" +
                        "  functions[$functions], " +
                        "memberProperties[$memberProperties], " +
                        "objectInstance[$objectInstance]\n" +
                        "  primaryCtor[$primaryCtor], " +
                        "nestedClass[$nestedClass], " +
                        "staticFunctions[$staticFunctions]\n" +
                        "-- Java Class inspection --\n" +
                        "annotation class[$annotationClass], " +
                        "anonymous[$anonymous]" +
                        "\n"
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
    companion object {
        fun <T> of(evaluate: () -> T): Try<T> = try { Succeed(evaluate()) } catch (e: Throwable) { Fail() } 
    }
}

class Fail<out T>: Try<T>() {
    override val error: Boolean = false
    override fun toString(): String = "Fail"
}

class Succeed<out T>(val boolean: T): Try<T>() {
    override val error: Boolean = true
    override fun toString(): String = "Succ($boolean)"
}

object JavapKotlinFilesTest {

    fun URI.asPath(): Path = Paths.get(this)

    @JvmStatic
    fun main(args: Array<String>): Unit {
        val location = Ano::class.java.protectionDomain.codeSource.location ?: throw IllegalStateException()
        val pkg = location.toURI().asPath().toAbsolutePath()
        println(pkg)
        val root = pkg.parent.parent.parent ?: throw IllegalStateException()
        val visitor = Visitor(root)
        Files.walkFileTree(pkg, visitor)
        visitor.list()
                .map { RunJavap(it, it.toString()) }
                .map { it.file to it.getProcess(root) }
                .map { it.first to
                        Runnable { it.second.start().inputStream.use {
                            it.bufferedReader(Charsets.UTF_8).forEachLine{ println(it) }
                        } } }
                .forEach { it.also { println() }.also { println(it.first) }.second.run() }
    }

    class Visitor(val root: Path): FileVisitor<Path> {

        val list: MutableList<Path> = mutableListOf()

        fun list(): List<Path> = list.toList()

        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult =
                FileVisitResult.CONTINUE
                        .also { if (file != null && file.fileName.toString().endsWith(".class"))
                            list.add(root.relativize(file)) }

        override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult = FileVisitResult.CONTINUE

        override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult =
                if (exc == null) FileVisitResult.CONTINUE else throw exc

        override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult =
                if (exc == null) FileVisitResult.CONTINUE else throw exc
    }

    data class RunJavap(val file: Path, val path: String) {
        val command: Array<String> get() = arrayOf("javap", "-p", "-c", "-l", path)
        fun getProcess(root: Path): ProcessBuilder = ProcessBuilder(*command).directory(root.toFile())
                .also { it.environment()["PATH"] =
                        "/usr/local/sbin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:${System.getenv("JAVA_HOME")}/bin" }
    }
}
