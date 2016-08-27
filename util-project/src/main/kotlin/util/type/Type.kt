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

sealed class Type {
    abstract val kind: String
    open class Kind0<C, I: Kind0.Instance<C, I>>: Type() {
        override val kind: String = "*"
        interface Instance<C, I: Instance<C, I>>
    }
    open class Kind1<C, I: Kind1.Instance<C, I>, T>: Type() {
        override val kind: String = "* -> *"
        interface Instance<C, I: Instance<C, I>>
    }
    open class Kind2<C, I: Kind2.Instance<C, I>, S, T>: Type() {
        override val kind: String = "* -> * -> *"
        interface Instance<C, I: Instance<C, I>>
    }
    open class Kind3<C, I: Kind3.Instance<C, I>, S1, S2, T>: Type() {
        override val kind: String = "* -> * -> * -> *"
        interface Instance<C, I: Instance<C, I>>
    }
}
