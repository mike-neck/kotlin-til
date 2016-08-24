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
import util.data.MaybeInstance.fn
import util.data.MaybeInstance.map
import util.data.MaybeInstance.pure
import util.plus
import util.shouldBe
import util.unit

class MaybeTest {

    fun <T> id(): (T) -> T = { it }

    /**
     * A test that [Maybe] satisfies functor law.
     *
     * <pre><code>
     *     fmap id  = id
     * </code></pre>
     */
    @Test fun functor1stLaw() =
            pure("test").let { map(it, id()) } shouldBe pure("test")

    val f: (List<String>) -> List<Int> = { it.map(String::length) }

    val g: (List<Int>) -> Int = { it.sum() }

    /**
     * A test that [Maybe] satisfies functor law.
     * 
     * <pre><code>
     *     fmap (f . g) = (fmap f) . (fmap g)
     * </code></pre>
     */
    @Test fun functor2ndLaw() =
            pure(listOf("test", "functor", "maybe"))
                    .let { it.fmap(f).fmap(g) to it.fmap(f + g) }
                    .unit { it.first shouldBe it.second }

    /**
     * A test that [Maybe] satisfies monad law(Left identity).
     *
     * <pre><code>
     *     return a >>= f = f a
     * </code></pre>
     */
    @Test fun monad1stLaw() =
            listOf(0, 1, -1, 1000000, -1000000).forEach(satisfyLeftIdentity)

    val positive: (Int) -> Maybe<Int> = { if (it >= 0) MaybeOf.Just(it) else MaybeOf.None() }

    val satisfyLeftIdentity: (Int) -> Unit = { returnThenBind(it) shouldBe simplyApply(it) }

    val returnThenBind: (Int) -> Maybe<Int> = fn<Int, Int>().let { (it.pure + it.bind) / positive }

    val simplyApply: (Int) -> Maybe<Int> = positive

    /**
     * A test that [Maybe] satisfies monad 2nd law(Right identity).
     *
     * <pre><code>
     *     m >>= return = m
     * </code></pre>
     */
    @Test fun monad2ndLaw() =
            listOf<Maybe<Maybe<Int>>>(MaybeOf.Just(MaybeOf.Just(1)), MaybeOf.Just(MaybeOf.None()), MaybeOf.None())
                    .forEach(satisfyRightIdentity)

    val bindReturn: (Maybe<Maybe<Int>>) -> Maybe<Maybe<Int>> =
            fn<Maybe<Int>, Maybe<Int>>().let { it.bind / it.pure }

    val satisfyRightIdentity: (Maybe<Maybe<Int>>) -> Unit = { bindReturn(it) shouldBe it }
}

infix operator fun <P, Q, R, F:(P) -> ((Q) -> R)> F.div(q: Q): (P) -> R = { p: P -> this(p)(q) }

operator fun <P, Q, R, F:(P, Q) -> R> F.invoke(q: Q): (P) -> R = { p: P -> this(p, q) }
