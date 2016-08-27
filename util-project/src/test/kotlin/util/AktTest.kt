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

import org.junit.Test
import util.data.*
import util.data.EitherMonad as E
import util.data.MaybeMonad as M

class AktTest {

    @Test fun aktOnMaybe() = akt<MaybeType, M, Int, Int, Int>(
            { M.pure(1) },
            { it: Int -> M.pure(it * 2) },
            { it: Int -> M.pure(it + 1) }
    ) shouldBe Just(3)

    @Test fun aktOnEither() = akt<EitherType, E, String, String, Int, Int>(
            { E.pure<String, String>("EitherStringString") },
            { s: String -> s.toCharArray().filter(Char::isUpperCase).count().let { if (it == 0) Left<String, Int>(s) else Right(it) } },
            { E.pure(it * it) }
    ) shouldBe Right(9)
}
