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
import util.data.EitherImpl.Companion.fn
import util.data.EitherImpl.Companion.map
import util.data.EitherImpl.Companion.pure
import util.data.MonadLawTestSupport.FunctorCompositionLaw.f
import util.data.MonadLawTestSupport.FunctorCompositionLaw.g
import util.data.MonadLawTestSupport.FunctorIdentity.id
import util.plus
import util.shouldBe

class EitherTest {

    /**
     * A test that [Either] satisfies functor law.
     *
     * <pre><code>
     *     fmap id  = id
     * </code></pre>
     */
    @Test fun functorIdentity() =
            listOf<Either<String, Int>>(Left("test"), Right(10))
                    .map { map(it, id()) to it }
                    .forEach { it.first shouldBe it.second }

    /**
     * A test that [Either] satisfies functor law.
     *
     * <pre><code>
     *     fmap (f . g) = (fmap f) . (fmap g)
     * </code></pre>
     */
    @Test fun functorFuncCombination() =
            listOf<Either<String, List<String>>>(pure(listOf("test", "functor", "maybe")), Left("left"))
                    .let { it to (lsp.map(f + g) to (rsp1.map(f) + rsp2.map(g))) }
                    .let { p -> p.first.map { p.second.first(it) to p.second.second(it) } }
                    .forEach { it.first shouldBe it.second }

    val lsp: EitherSupport<String, List<String>, Int> = fn()

    val rsp1: EitherSupport<String, List<String>, List<Int>> = fn()

    val rsp2: EitherSupport<String, List<Int>, Int> = fn()

}
