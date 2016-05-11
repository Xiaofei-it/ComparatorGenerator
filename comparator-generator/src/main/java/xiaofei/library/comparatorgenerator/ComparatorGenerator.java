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

package xiaofei.library.comparatorgenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import xiaofei.library.comparatorgenerator.internal.AnnotationUtils;
import xiaofei.library.comparatorgenerator.internal.FieldMember;
import xiaofei.library.comparatorgenerator.internal.GeneratedComparator;
import xiaofei.library.comparatorgenerator.internal.Member;
import xiaofei.library.comparatorgenerator.internal.MethodMember;
import xiaofei.library.comparatorgenerator.internal.SortingCriterion;
import xiaofei.library.comparatorgenerator.internal.TypeUtils;


public class ComparatorGenerator<T> {

    private Class<T> clazz;

    private TreeMap<Integer, SortingCriterion> criteria;

    public ComparatorGenerator(Class<T> clazz) {
        this.clazz = clazz;
        this.criteria = new TreeMap<Integer, SortingCriterion>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return -o1.compareTo(o2);
                    }
                });
        TreeMap<Integer, SortingCriterion> map = AnnotationUtils.getCriteriaIn(clazz);
        for (Map.Entry<Integer, SortingCriterion> entry : map.entrySet()) {
            SortingCriterion prev = criteria.put(entry.getKey(), entry.getValue());
            if (prev != null) {
                throw new RuntimeException(
                        "The priority value " + entry.getKey() + " has already been set to the member "
                                + prev.getMember().getName() + ". Please specify another priority value.");
            }
        }
    }

    public ComparatorGenerator<T> addCriterion(int priority, String memberName) {
        return addCriterion(priority, memberName, Order.ASCENDING);
    }

    public ComparatorGenerator<T> addCriterion(int priority, final String memberName, Order order) {
        final Field field = TypeUtils.getField(clazz, memberName);
        final Method method = TypeUtils.getMethod(clazz, memberName);
        if (field ==null && method == null) {
            throw new IllegalArgumentException("Member " + memberName + " does not exist.");
        }
        if (field != null && method != null) {
            throw new IllegalArgumentException("A field and a method has the same name: " + memberName
                    + ". Which one do you want?");
        }
        Member member;
        if (field != null) {
            TypeUtils.checkField(field);
            member = new FieldMember(field);
        } else {
            TypeUtils.checkMethod(method);
            member = new MethodMember(method);
        }
        SortingCriterion prev = criteria.put(priority, new SortingCriterion(member, order));
        if (prev != null) {
            throw new IllegalArgumentException(
                    "The priority value " + priority + " has already been set to the member "
                            + prev.getMember().getName() + ". Please specify another priority value.");
        }
        return this;
    }

    public Comparator<T> generate() {
        List<SortingCriterion> criteria = new ArrayList<SortingCriterion>();
        criteria.addAll(this.criteria.values());
        if (criteria.isEmpty()) {
            throw new RuntimeException(
                    "There is no sorting criterion specified. "
                            + "Please specify at least one criterion "
                            + "by adding @Criterion on the corresponding field or method or using "
                            + "ComparatorGenerator.addCriterion(int, String, Order).");
        }
        this.clazz = null;
        this.criteria = null;
        return new GeneratedComparator<T>(criteria);
    }

}