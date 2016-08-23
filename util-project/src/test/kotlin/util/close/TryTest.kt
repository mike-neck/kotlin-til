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

import org.junit.Test
import util.shouldBe
import java.io.Closeable
import java.io.IOException
import java.util.concurrent.Callable

class TryTest {

    @Test fun successCase() =
            Try(SuccessCase()) { it.call() }
                    .exec()
                    .right shouldBe "success"

    @Test fun failCase() =
            Try(FailOnCall()) { it.call() }
                    .exec()
                    .left.message shouldBe "fail"

    @Test fun failOnClose() =
            Try(FailOnClose()) { it.call() }
                    .exec()
                    .or { "${it.message}" } shouldBe "close"

    @Test fun failTwice() =
            Try(FailTwice()) { it.call() }
                    .exec()
                    .or { "${it.message}" } shouldBe "call"

    @Test fun withHandleSuccess() =
            Try(SuccessCase()) { it.call() }
                    .handle<Exception> { "error" } shouldBe "success"

    @Test fun withHandleFail() =
            Try(FailOnCall()) { it.call() }
                    .handle<Exception> { "handle - ${it.message}" } shouldBe "handle - fail"

    @Test fun withHandleFailOnClose() =
            Try(FailOnClose()) { it.call() }
                    .handle<Exception> { "handle - ${it.message}" } shouldBe "handle - close"

    @Test fun withHandleFailTwice() =
            Try(FailTwice()) { it.call() }
                    .handle<Exception> { "handle - ${it.message}" } shouldBe "handle - call"
}

class SuccessCase: Closeable, Callable<String> {
    override fun call(): String = "success"
    override fun close() = Unit
}

class FailOnCall: Closeable, Callable<String> {
    override fun call(): String = throw IOException("fail")
    override fun close() = Unit
}

class FailOnClose: Closeable, Callable<String> {
    override fun call(): String = "call"
    override fun close() = throw IOException("close")
}

class FailTwice: Closeable, Callable<String> {
    override fun close() = throw IOException("close")
    override fun call(): String = throw Exception("call")
}
