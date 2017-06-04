/*
 * Copyright 2017 Shinya Mochida
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
package com.example

import java.security.SecureRandom
import java.security.Security
import java.util.*

object RandomGen {
    @JvmStatic
    fun main(args: Array<String>) {
        println(Security.getProviders().toList())

        val sr = SecureRandom.getInstance("NativePRNGNonBlocking")
        println("nextBoolean -> " + sr.nextBoolean())
        println("nextInt -> " + sr.nextInt())
        println("nextInt(200) -> " + sr.nextInt(20))
        println("nextLong -> " + sr.nextLong())

        val nr = Random()
        val byteArray = ByteArray(10)
        nr.nextBytes(byteArray)
        println("nextByteArray -> ${byteArray.map { Integer.toHexString(it.toInt()) }.toList()}")
        
    }
}

sealed class Maybe<T: Comparable<T>> {
    abstract fun getOrDefault(def: T): T
}

class Just<T:Comparable<T>>: Maybe<T>(), Comparable<Just<T>> {
    override fun getOrDefault(def: T): T {
        TODO("not implemented")
    }

    override fun compareTo(other: Just<T>): Int {
        TODO("not implemented")
    }
}

object Arbitrary {
    @JvmStatic
    fun main(args: Array<String>) {
        val list: List<Pair<Int, Float>> = listOf(int to float, int to float)
        assert(list[0] != list[1])
        repeat(100) {
            println(random.nextInt(1))
        }
    }

    private val random: Random = Random(20L)

    val int: Int get() = random.nextInt()
    val float: Float get() = random.nextFloat()
}
