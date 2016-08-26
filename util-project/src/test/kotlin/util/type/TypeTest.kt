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

import org.junit.Test
import util.data.MaybeType
import util.initBy
import util.unit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class TypeTest {

    @Test fun gettingGenericType() =
            (object : Generics<MaybeType>() {})
                    .initBy { it.superType.let { "super type: [$it]" }.initBy(::println) }
                    .initBy { it.showSuperTypeImpl() }
                    .initBy { it.showOwnerType() }
                    .initBy { it.showRawType() }
                    .initBy { it.showActualTypeArguments() }
                    .unit
}

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
