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
package util

import util.type.Bind
import util.type.Monad
import util.type.MonadInstance

inline fun <reified M: Monad, T1, T2, T3> suc(
        noinline f1: () -> Bind<M, T1>,
        noinline f2: (T1) -> Bind<M, T2>,
        noinline f3: (T2) -> Bind<M, T3>
): Bind<M, T3> =
        MonadInstance.take<M, MonadInstance<M>>()
                .let { it to f1() }
                .let { it.first to (it.first.bind(it.second, f2)) }
                .let { it.first.bind(it.second, f3) }
