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
package util.close

import util.either.Either
import util.initialize
import java.io.Closeable
import java.io.IOException
import java.util.*

class Try<C: Closeable, R>(private val closeable: C, private val f: (C) -> R) {

    private fun run(): Alternative<R, Exception> =
            try {
                Alternative.First(f(closeable))
            }
            catch (e: Exception) {
                try { run { closeable.close() }.let { Alternative.Second<R, Exception>(e) } }
                catch (i: IOException) {
                    i.initialize({ e }) { f,s -> s.addSuppressed(f) }.let { Alternative.Second<R, Exception>(it) } }
            }

    private fun close(): Alternative<Unit, Exception> =
            try { closeable.close().let { Alternative.First(it) } }
            catch (e: IOException) { Alternative.Second(e) }

    private fun getResult(): Alternative<R, Exception> =
            run().let { a -> when(a) {
                is Alternative.Second -> a
                is Alternative.First  ->
                    run { close() }.let { when(it) {
                        is Alternative.First  -> a
                        is Alternative.Second -> Alternative.Second<R, Exception>(it.second) 
                    } }
            } }

    fun exec(): Either<Exception, R> =
            run { getResult() }
                    .let { when (it) {
                        is Alternative.First -> Either.Right(it.first)
                        is Alternative.Second -> Either.Left(it.second)
                    } }

    @Suppress("UNCHECKED_CAST")
    fun <E: Throwable> handle(handler: (E) -> R): R =
            run { exec() }.or { handler(it as E) }
}

private sealed class Alternative<out F, out S> {
    abstract val first: F
    abstract val second: S
    class First<out F, out S>(override val first: F): Alternative<F, S>() {
        override val second: S get() = throw NoSuchElementException("This is first.")
    }
    class Second<out F, out S>(override val second: S): Alternative<F, S>() {
        override val first: F get() = throw NoSuchElementException("This is second.")
    }
}
