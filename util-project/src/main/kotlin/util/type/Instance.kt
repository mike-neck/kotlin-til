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

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

interface Instance<T>

abstract class InstanceOf<T> {

    val superType: Type

    init { superType = this.javaClass.genericSuperclass }

    private val asParameterizedType: ParameterizedType
        get() = superType as ParameterizedType

    @Suppress("UNCHECKED_CAST")
    val instanceFor: Class<T>
        get() = asParameterizedType.actualTypeArguments[0] as Class<T>
}
