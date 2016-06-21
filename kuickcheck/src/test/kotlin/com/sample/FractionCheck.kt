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

import org.mikeneck.kuickcheck.Property
import org.mikeneck.kuickcheck.forAll
import org.mikeneck.kuickcheck.int

class FractionCheck {

    val `denom is not 0 and denom is not min and numer is not min` = {
        n: Int, d: Int -> d != 0 && n != Integer.MIN_VALUE && d != Integer.MIN_VALUE
    }

    @Property
    val `numer should be positive` = forAll(int, int)
            .filter(`denom is not 0 and denom is not min and numer is not min`)
            .filter { n, d -> (n < 0 && d < 0) || (n > 0 && d > 0) }
            .satisfy { n, d -> Fraction(n, d).numer > 0 }

    @Property
    val `numer should be negative` = forAll(int, int)
            .filter(`denom is not 0 and denom is not min and numer is not min`)
            .filter { n, d -> !(n < 0 && d < 0) && !(n > 0 && d > 0) }
            .filter { n, d -> n != 0 }
            .satisfy { n, d -> Fraction(n, d).numer < 0 }

    @Property
    val `numer should be 0` = forAll(int(0), int)
            .filter(`denom is not 0 and denom is not min and numer is not min`)
            .filter { n, d -> !(n < 0 && d < 0) && !(n > 0 && d > 0) }
            .satisfy { n, d -> Fraction(n, d).numer == 0 }

    @Property
    val `denom should be larger than 0` = forAll(int, int)
            .filter(`denom is not 0 and denom is not min and numer is not min`)
            .satisfy { n, d -> Fraction(n, d).denom > 0 }
}
