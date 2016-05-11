/**
 *
 * Copyright 2016 Xiaofei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package xiaofei.library.comparatorgenerator.internal;

import xiaofei.library.comparatorgenerator.Order;

/**
 * Created by Xiaofei on 16/5/11.
 */
public class SortingCriterion {

    private Member member;

    private Order order;

    public SortingCriterion(Member value, Order order) {
        this.member = value;
        this.order = order;
    }

    public Member getMember() {
        return member;
    }

    public Order getOrder() {
        return order;
    }

}
