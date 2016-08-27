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
import util.data.MaybeImpl.Companion.fn
import util.data.MaybeImpl.Companion.map
import util.data.MaybeImpl.Companion.pure
import util.data.MonadLawTestSupport.FunctorIdentity.id
import util.plus
import util.shouldBe
import util.unit
import util.data.MonadLawTestSupport.FunctorCompositionLaw as FC
import util.data.MonadLawTestSupport.MonadAssociativity as MA
import util.data.MonadLawTestSupport.MonadLeftIdentity as LI
import util.data.MonadLawTestSupport.MonadRightIdentity as RI

class MaybeTest {

    /**
     * A test that [Maybe] satisfies functor law.
     *
     * <pre><code>
     *     fmap id  = id
     * </code></pre>
     */
    @Test fun functorIdentity() =
            listOf(pure("test"), None())
                    .map { map(it, id()) to it }
                    .forEach { it.first shouldBe it.second }

    val fsp: MaybeSupport<List<String>, List<Int>> = fn()

    val gsp: MaybeSupport<List<Int>, Int> = fn()

    val hsp: MaybeSupport<List<String>, Int> = fn()

    /**
     * A test that [Maybe] satisfies functor law.
     * 
     * <pre><code>
     *     fmap (f . g) = (fmap f) . (fmap g)
     * </code></pre>
     */
    @Test fun functorCombination() =
            pure(listOf("test", "functor", "maybe"))
                    .let { hsp.map(FC.f + FC.g)(it) to (fsp.map(FC.f) + gsp.map(FC.g))(it) }
                    .unit { it.first shouldBe it.second }

    /**
     * A test that [Maybe] satisfies monad law(Left identity).
     *
     * <pre><code>
     *     return a >>= f = f a
     * </code></pre>
     */
    @Test fun monadLeftIdentity() =
            listOf(0, 1, -1, 1000000, -1000000)
                    .map { LI.left(positive)(it) to positive(it) }
                    .forEach { it.first shouldBe it.second }

    val positive: (Int) -> Maybe<Int> = { if (it >= 0) Just(it) else None() }

    /**
     * A test that [Maybe] satisfies monad 2nd law(Right identity).
     *
     * <pre><code>
     *     m >>= return = m
     * </code></pre>
     */
    @Test fun monadRightIdentity() =
            listOf(Just(Just(1)), Just(None()), None())
                    .map { RI.left(it) to it }
                    .forEach { it.first shouldBe it.second }

    /**
     * A test that [Maybe] satisfies monad 3rd law(Associativity).
     *
     * <pre><code>
     *     (m >>= f) >>= g = m >>= (\x -> f x >>= g)
     * </code></pre>
     */
    @Test fun monadAssociativity() =
            listOf(Just("MaybeOf"), Just(""), None(), Just("maybe"), Just("MAYBE"))
                    .map { MA.left(f, g)(it) to MA.right(f, g)(it) }
                    .forEach { it.first shouldBe it.second }

    val f: (String) -> Maybe<List<Char>> = { if (it.length > 0) Just(it.toCharArray().toList()) else None() }

    val g: (List<Char>) -> Maybe<String> = { it.filter(Char::isUpperCase)
            .let { if (it.size == 0) None() else Just(it.joinToString("")) } }
}
