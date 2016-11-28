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
package util

/**
 * This class offers variable into [Akt] execution.
 * 
 * Every function given to [Akt] will be executed under this instance.
 * i.e. every function is not <code>(T) -> (Kind&lt;M, I, R&gt;)</code>, but <code>AktContext.(T) -> (Kind&lt;M, I, R&gt;)</code>.
 * 
 */
class AktContext

sealed class Variable {
    class Vacant: Variable()
    class Occupy: Variable()
}


