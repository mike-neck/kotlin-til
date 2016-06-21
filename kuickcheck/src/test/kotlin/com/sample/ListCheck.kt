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

import org.mikeneck.kuickcheck.*

object ListCheck {

    @Property
    val `list mapped by arbitrary function, its size doesn't change` =
            forAll(list(string), function({s:String ->}, int))
                    .satisfy { list, f -> list.map(f).size == list.size }

    @Property
    val `list filtered by function, its size should be smaller than original` =
            forAll(list(string), function({s:String ->}, boolean))
                    .satisfy { list, f -> list.filter(f).size <= list.size }
}
