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
import util.data.StateMonad as S

class AktTest {

    /**
     * <code><pre>
     *     aktOnEither = do
     *         x <- return "EitherStringString"
     *         y <- countUpperCase x
     *         if (y == 0) then (Left x) else return y
     * </pre></code>
     */
//    @Test fun aktOnEither() = akt<EitherType, E, String, String, Int, Int>(
//            { E.pure<String, String>("EitherStringString") },
//            { s: String -> s.toCharArray().filter(Char::isUpperCase).count().let { if (it == 0) Left<String, Int>(s) else Right(it) } },
//            { E.pure(it * it) }
//    ) shouldBe Right(9)

    /**
     * <code><pre>
     *     aktOnState = do
     *         x <- get
     *         put(x + 1)
     *         return 10
     *     
     *     test = runState aktOnState 1
     * </pre></code>
     */
    @Test fun aktOnState() = akt(S.get<Int>())[
            { S.put(it + 1) }][
            { S.pure<Int, Int>(10) }].let { S.runState(it(), 1) } shouldBe (10 to 2)

    /**
     * <code><pre>
     *     aktOnMaybe :: Maybe Int
     *     aktOnMaybe = do
     *         x <- return 10
     *         y <- return (x * 2)
     *         return (y + 1)
     * </pre></code>
     */
    @Test fun aktOnMaybe() = akt( M.pure(1) )[
            { M.pure(it * 2) }][
            { M.pure(it + 1) }]() shouldBe Just(3)
}
