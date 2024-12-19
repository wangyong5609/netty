/*
 * Copyright 2016 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import io.netty.util.IntSupplier;

/**
 * Default select strategy.
 */
final class DefaultSelectStrategy implements SelectStrategy {
    static final SelectStrategy INSTANCE = new DefaultSelectStrategy();

    private DefaultSelectStrategy() { }

    @Override
    public int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception {
        // 1. 如果 taskQueue 不为空，也就是 hasTasks() 返回 true，
        // 		那么执行一次 selectNow()，该方法不会阻塞
        // 2. 如果 hasTasks() 返回 false，那么执行 SelectStrategy.SELECT 分支，
        //    进行 select(...)，这块是带阻塞的
        // 这个很好理解，就是按照是否有任务在排队来决定是否可以进行阻塞
        return hasTasks ? selectSupplier.get() : SelectStrategy.SELECT;
    }
}
