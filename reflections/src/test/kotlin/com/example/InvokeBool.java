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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InvokeBool {

    private static final ClassLoader LOADER = InvokeBool.class.getClassLoader();

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Class<?> defaultConstructorMarker = LOADER.loadClass("kotlin.jvm.internal.DefaultConstructorMarker");
        //noinspection JavaReflectionMemberAccess
        final Constructor<Bool.Companion> constructor = Bool.Companion.class.getConstructor(defaultConstructorMarker);
        final Bool.Companion companion = constructor.newInstance(defaultConstructorMarker.cast(null));
        System.out.println(companion);
        System.out.println(Bool.Companion);
    }
}
