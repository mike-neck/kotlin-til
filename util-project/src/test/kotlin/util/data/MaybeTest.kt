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
package util.data

import org.junit.Test
import util.data.MaybeInstance.map
import util.data.MaybeInstance.pure
import util.plus
import util.shouldBe
import util.unit

class MaybeTest {

    fun <T> id(): (T) -> T = { it }

    @Test fun functor1stLaw() =
            pure("test").let { map(it, id()) } shouldBe pure("test")

    val f: (List<String>) -> List<Int> = { it.map(String::length) }

    val g: (List<Int>) -> Int = { it.sum() }

    @Test fun functor2ndLaw() =
            pure(listOf("test", "functor", "maybe"))
                    .let { it.fmap(f).fmap(g) to it.fmap(f + g) }
                    .unit { it.first shouldBe it.second }
}
