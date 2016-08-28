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
import util.*
import util.data.MonadLawTestSupport.each
import util.data.MonadLawTestSupport.FunctorCompositionLaw as FC
import util.data.MonadLawTestSupport.FunctorIdentity as FI
import util.data.MonadLawTestSupport.MonadAssociativity as MA
import util.data.MonadLawTestSupport.MonadLeftIdentity as LI
import util.data.MonadLawTestSupport.MonadRightIdentity as RI
import util.data.StateMonad as S

class StateTest {

    /**
     * A test that [State] satisfies functor law.
     *
     * <pre><code>
     *     fmap id  = id
     * </code></pre>
     */
    @Test fun functorIdentity() =
            listOf<State<String, Int>>(S.pure(10), S.state { it.length to it })
                    .map { S.map(it, FI.id()) to it }
                    .map { it.each { S.runState(it, "test") } }
                    .forEach { it.first shouldBe it.second }

    /**
     * A test that [State] satisfies functor law.
     *
     * <pre><code>
     *     fmap (f . g) = (fmap f) . (fmap g)
     * </code></pre>
     */
    @Test fun functorCombination() =
            S.pure<List<String>, String>(listOf("test", "functor", "maybe"))
                    .let { (S.map(it, FC.f + FC.g)) to (fsp.map(FC.f) + gsp.map(FC.g))(it) }
                    .let { it.each { S.runState(it, "test") } }
                    .unit { it.first shouldBe it.second }

    val fsp: StateSupport<String, List<String>, List<Int>> = S.fn()

    val gsp: StateSupport<String, List<Int>, Int> = S.fn()

    /**
     * A test that [State] satisfies monad law(Left identity).
     *
     * <pre><code>
     *     return a >>= f = f a
     * </code></pre>
     */
    @Test fun monadLeftIdentity() =
            listOf(0, 1, -1, 1000000, -1000000)
                    .map { LI.left(positive)(it) to positive(it) }
                    .map { it.each { S.runState(it, "test") } }
                    .forEach { it.first shouldBe it.second }

    val positive: (Int) -> State<String, Int> = { if (it < 0) S.pure(-it) else S.pure(it) }

    /**
     * A test that [State] satisfies monad law(Right identity).
     *
     * <pre><code>
     *     m >>= return = m
     * </code></pre>
     */
    @Test fun monadRightIdentity() =
            listOf(1, -1, 0, 1000000, -1000000)
                    .map { S.pure<Int, String>(it) }
                    .map { RI.left(it) to it }
                    .map { it.each { S.runState(it, "test") } }
                    .forEach { it.first shouldBe it.second }

    /**
     * A test that [State] satisfies monad law(Associativity).
     *
     * <pre><code>
     *     (m >>= f) >>= g = m >>= (\x -> f x >>= g)
     * </code></pre>
     */
    @Test fun monadAssociativity() =
            listOf("EitherOf", "", "either", "EITHER")
                    .map(p)
                    .map { MA.left(f, g)(it) to MA.right(f, g)(it) }
                    .map { it.each { S.runState(it, "test") } }
                    .forEach { it.first shouldBe it.second }

    val f: (String) -> State<String, List<Char>> = { it.toCharArray().filter(Char::isUpperCase).let { S.pure(it) } }

    val g: (List<Char>) -> State<String, Int> = { it.size.let { S.pure(it) } }

    val p: (String) -> State<String, String> = { S.pure(it) }

    /**
     * This test code is equivalent to that of Haskell shown below.
     * <pre><code>
     *     bind = do
     *         x <- get
     *         put x
     *         return 1
     *
     *     test = runState $ bind 3
     * </code></pre>
     */
    @Test fun bindTest() =
            run { S.get<Int>() }
                    .let { S.bind(it) { S.put(it) } }
                    .let { S.bind(it) { S.pure<Int, Int>(1) } }
                    .let { S.runState(it, 3) } shouldBe (1 to 3)

    /**
     * This test code is equivalent to that of Haskell shown below.
     * <pre><code>
     *     bind2 = do
     *         x <- get
     *         let x' = x + 1
     *         put x'
     *         return (x' + x)
     *         y <- get
     *         let y' = y * 2
     *         put y'
     *         return (y' + y)
     *
     *     test = runState $ bind2 1
     * </code></pre>
     */
    @Test fun bindTest2() =
            run { S.get<Int>() }
                    .let { S.bind(it, calculation { it + 1 }) }
                    .let { S.bind(it) { S.get<Int>() } }
                    .let { S.bind(it, calculation { it * 2 }) }
                    .let { S.runState(it, 1) } shouldBe (6 to 4)

    fun calculation(f: (Int) -> Int): (Int) -> State<Int, Int> =
            { x -> x.let(f).let { xx -> S.put(xx).let { S.bind(it) { S.pure<Int, Int>(x + xx) } } } }

    @Test fun bindTest2WithAktKind2() =
            Akt.Kind2(S) { S.get<Int>() }
                    .take(calculation { it + 1 })
                    .take { S.get<Int>() }
                    .take(calculation { it * 2 })
                    .getter().let { S.runState(it, 1) } shouldBe (6 to 4)

    @Test fun bindTest2WithAkt() =
            akt({ S.get<Int>() },
                    calculation { it + 1 },
                    calculation { it * 2 }
            ).let { S.runState(it, 1) } shouldBe (9 to 6)
}
