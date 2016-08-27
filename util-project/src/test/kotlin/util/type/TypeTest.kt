/*
 * Copyright 2016 Shinya Mochida
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
package util.type

//import util.data.MaybeType
import org.junit.Test
import util.shouldBe
import util.then
import util.unit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

//class TypeTest {
//
//    @Test fun gettingGenericType() =
//            (object : Generics<MaybeType>() {})
//                    .initBy { it.superType.let { "super type: [$it]" }.initBy(::println) }
//                    .initBy { it.showSuperTypeImpl() }
//                    .initBy { it.showOwnerType() }
//                    .initBy { it.showRawType() }
//                    .initBy { it.showActualTypeArguments() }
//                    .unit
//}

abstract class Generics<T> {

    val superType: Type

    val interfaceType: List<Type>

    init {
        superType = this.javaClass.genericSuperclass
        interfaceType = this.javaClass.genericInterfaces.toList()
    }

    fun showSuperTypeImpl(): Unit =
            superType.javaClass.canonicalName
                    .let { "super type class: [$it]" }
                    .unit(::println)

    val asParameterizedType: ParameterizedType
        get() = superType as ParameterizedType

    fun showOwnerType(): Unit =
            asParameterizedType
                    .let { it.ownerType }
                    .let { "super type ownerType: [$it]" }
                    .unit(::println)

    fun showRawType(): Unit =
            asParameterizedType
                    .let { it.rawType }
                    .let { "super type rawType: [$it/${it.javaClass}]" }
                    .unit(::println)

    fun showActualTypeArguments(): Unit =
            asParameterizedType
                    .let { it.actualTypeArguments.toList() }
                    .map { "super type actualTypeArguments: [$it/${it.javaClass}]" }
                    .forEach(::println)
}

sealed class Kind {
    abstract val kind: String
    open class Level1<I: Mapper1<I>, T>: Kind() { override val kind: String = "* -> *" }
    open class Level2<I: Mapper2<I>, S,K>: Kind() { override val kind: String = "* -> * -> *" }
}

interface Mapper1<I: Mapper1<I>> {
    fun <T, R> map(v: Kind.Level1<I, T>, f: (T) -> R): Kind.Level1<I, R>
}

interface Mapper2<I: Mapper2<I>> {
    fun <S, T, R> map(v: Kind.Level2<I, S, T>, f: (T) -> R): Kind.Level2<I, S, R>
}

sealed class Option<T>: Kind.Level1<Option.Companion, T>() {
    data class None<T>(val get: Unit = Unit): Option<T>()
    data class Some<T>(val get: T): Option<T>()

    companion object: Mapper1<Option.Companion> {
        override fun <T, R> map(v: Kind.Level1<Option.Companion, T>, f: (T) -> R): Option<R> =
                when (v) {
                    is None -> None()
                    is Some -> Some(f(v.get))
                    else    -> None()
                }
        fun <T> pure(v: T): Option<T> = Some(v)
    }
}

class Fun<I, R>(val func: (I) -> R): Kind.Level2<Fun.Companion, I, R>() {
    companion object: Mapper2<Fun.Companion> {
        override fun <S, T, R> map(v: Kind.Level2<Fun.Companion, S, T>, f: (T) -> R): Kind.Level2<Fun.Companion, S, R> =
                when (v) {
                    is Fun -> Fun({ f(v.func(it)) })
                    else   -> throw IllegalArgumentException("only take Fun.")
                }
        fun <I, R> pure(f: (I) -> R): Fun<I, R> = Fun(f)
    }
}

class As<T>

class MapTest {

    @Test fun kind1() =
            run<Pair<Mapper1<Option.Companion>, Option<Int>>> { Option.Companion to Option.pure(100) }
                    .let { it.first.map(it.second) { it * it } }
                    .let { it as Option<Int> } shouldBe Option.pure(100 * 100)

    @Test fun kind2() =
            run<Pair<Mapper2<Fun.Companion>, Fun<String, Int>>> { Fun.Companion to Fun.pure { it.length } }
                    .let { it.first.map(it.second) { it * it } }
                    .let { it as Fun<String, Int> }
                    .let { it.func("foo") } shouldBe 9

    @Test fun kind1Suc() =
            suc(Option.pure(10), { it * it }, Int::toChar) shouldBe Option.pure(100.toChar())

    @Test fun kind2Suc() =
            suc(Fun.pure(String::length), { it * it }, Int::toChar, As<Fun<String, Char>>())
                    .func("1234567890") shouldBe 100.toChar()

    companion object {
        inline fun <reified I: Mapper1<I>, T1, T2, T3, F: Kind.Level1<I, T3>> suc(
                initial: Kind.Level1<I, T1>,
                noinline f1: (T1) -> T2,
                noinline f2: (T2) -> T3,
                a: As<F> = As(),
                kc: KClass<I> = I::class
        ): F = kc.objectInstance
                .then { it to initial }
                .then { it.first to it.first.map(it.second, f1) }
                .then { it.first to it.first.map(it.second, f2) }
                .then { it.second as F } ?: throw IllegalArgumentException()

        inline fun <reified I: Mapper2<I>, T1, T2, T3, S, F: Kind.Level2<I, S, T3>> suc(
                initial: Kind.Level2<I, S, T1>,
                noinline f1: (T1) -> T2,
                noinline f2: (T2) -> T3,
                a: As<F> = As(),
                kc: KClass<I> = I::class
        ): F = kc.objectInstance
                .then { it to initial }
                .then { it.first to it.first.map(it.second, f1) }
                .then { it.first to it.first.map(it.second, f2) }
                .then { it.second as F } ?: throw IllegalArgumentException()
    }
}
