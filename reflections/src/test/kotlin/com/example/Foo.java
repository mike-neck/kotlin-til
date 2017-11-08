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
package com.example;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Foo {

    public static void main(String[] args) {
        System.out.println(Compiled.upper("foo"));
        for (final Method method : Compiled.class.getDeclaredMethods()) {
            System.out.println(method.getName());
            System.out.println(method.getGenericReturnType().getTypeName());
            for (final Type type : method.getGenericParameterTypes()) {
                System.out.print(type.getTypeName());
                System.out.print(',');
            }
            System.out.println();
        }
    }
}
