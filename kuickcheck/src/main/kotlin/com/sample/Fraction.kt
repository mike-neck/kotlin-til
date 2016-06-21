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
package com.sample

class Fraction(n: Int, d: Int) {

    val numer = if (d < 0) -n else n

    val denom = d.abs

    init {
        require(d != 0, {"denom should be not 0."})
        require(d != Integer.MIN_VALUE)
        require(n != Integer.MIN_VALUE)
    }
}

private val Int.abs: Double get() = Math.abs(this.toDouble())
