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
import util.data.MaybeInstance.bind
import util.data.MaybeInstance.fn
import util.data.MaybeInstance.map
import util.data.MaybeInstance.pure
import util.data.MaybeOf.Just
import util.data.MaybeOf.None
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

    val positive: (Int) -> Maybe<Int> = { if (it >= 0) Just(it) else None() }

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
            listOf<Maybe<Maybe<Int>>>(Just(Just(1)), Just(None()), None())
                    .forEach(satisfyRightIdentity)

    val bindReturn: (Maybe<Maybe<Int>>) -> Maybe<Maybe<Int>> =
            fn<Maybe<Int>, Maybe<Int>>().let { it.bind / it.pure }

    val satisfyRightIdentity: (Maybe<Maybe<Int>>) -> Unit = { bindReturn(it) shouldBe it }

    /**
     * A test that [Maybe] satisfies monad 3rd law(Associativity).
     *
     * <pre><code>
     *     (m >>= f) >>= g = m >>= (\x -> f x >>= g)
     * </code></pre>
     */
    @Test fun monad3rdLaw() =
            listOf<Maybe<String>>(Just("MaybeOf"), Just(""), None(), Just("maybe"), Just("MAYBE"))
                    .forEach(satisfyAssociativity)

    val f3: (String) -> Maybe<List<Char>> = { if (it.length > 0) Just(it.toCharArray().toList()) else None() }

    val g3: (List<Char>) -> Maybe<String> = { it.filter(Char::isUpperCase)
            .let { if (it.size == 0) None() else Just(it.joinToString("")) } }

    val satisfyAssociativity: (Maybe<String>) -> Unit = { bindThenBind(it) shouldBe bindBinding(it) }

    val bindThenBind: (Maybe<String>) -> Maybe<String> =
            (fn<String, List<Char>>().bind / f3) + (fn<List<Char>, String>().bind / g3)

    val bindBinding: (Maybe<String>) -> Maybe<String> = fn<String, String>().bind / {x -> bind(f3(x), g3) }
}

infix operator fun <P, Q, R, F:(P) -> ((Q) -> R)> F.div(q: Q): (P) -> R = { p: P -> this(p)(q) }

operator fun <P, Q, R, F:(P, Q) -> R> F.invoke(q: Q): (P) -> R = { p: P -> this(p, q) }
