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

import xiaofei.library.comparatorgenerator.internal.*;
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
        TreeMap<Integer, SortingCriterion> map = xiaofei.library.comparatorgenerator.internal.AnnotationUtils.getCriteriaIn(clazz);
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
        final Field field = xiaofei.library.comparatorgenerator.internal.TypeUtils.getField(clazz, memberName);
        final Method method = xiaofei.library.comparatorgenerator.internal.TypeUtils.getMethod(clazz, memberName);
        if (field ==null && method == null) {
            throw new IllegalArgumentException("Member " + memberName + " does not exist.");
        }
        if (field != null && method != null) {
            throw new IllegalArgumentException("A field and a method has the same name: " + memberName
                    + ". Which one do you want?");
        }
        Member member;
        if (field != null) {
            xiaofei.library.comparatorgenerator.internal.TypeUtils.checkField(field);
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
                            + "by adding @Criterion on the corresponing field or using "
                            + "ComparatorGenerator.addCriterion(int, String, Order).");
        }
        this.clazz = null;
        this.criteria = null;
        return new GeneratedComparator<T>(criteria);
    }

    private static class GeneratedComparator<T> implements Comparator<T> {

        private List<SortingCriterion> criteria;

        public GeneratedComparator(List<SortingCriterion> criteria) {
            this.criteria = criteria;
        }

        private static <T> int compare(Member member, T o1, T o2) {
//                if (!field.isAccessible()) {
//                    field.setAccessible(true);
//                }
            Object v1 = member.getValue(o1);
            Object v2 = member.getValue(o2);
            if (v1 == null) {
                throw new IllegalArgumentException("The value of " + member.getName() + " of " + o1 + " is null.");
            }
            if (v2 == null) {
                throw new IllegalArgumentException("The value of " + member.getName() + " of " + o2 + " is null.");
            }
            if (v1 instanceof Comparable && v2 instanceof Comparable) {
                return  (Comparable.class.cast(v1)).compareTo(Comparable.class.cast(v2));
            } else {
                Class<?> type = member.getType();
                //boolean, byte, char, short, int, long, float, and double.
                if (type == boolean.class) {
                    return Boolean.valueOf((boolean) v1).compareTo((boolean) v2);
                } else if (type == byte.class) {
                    return Byte.valueOf((byte) v1).compareTo((byte) v2);
                } else if (type == char.class) {
                    return Character.valueOf((char) v1).compareTo((char) v2);
                } else if (type == short.class) {
                    return Short.valueOf((short) v1).compareTo((short) v2);
                } else if (type == int.class) {
                    return Integer.valueOf((int) v1).compareTo((int) v2);
                } else if (type == long.class) {
                    return Long.valueOf((long) v1).compareTo((long) v2);
                } else if (type == float.class) {
                    return Float.valueOf((float) v1).compareTo((float) v2);
                } else if (type == double.class) {
                    return Double.valueOf((double) v1).compareTo((double) v2);
                } else {
                    throw new IllegalStateException(
                            "Congratulations! You have found a bug"
                                    + " which I do not know. Please tell me so that"
                                    + " I can add it here.");
                }
            }
        }

        @Override
        public int compare(T o1, T o2) {
            if (o1 == null || o2 == null) {
                throw new NullPointerException("Parameter is null.");
            }
            Member member;
            Order order;
            int result;
            for (SortingCriterion criterion : criteria) {
                member = criterion.getMember();
                order = criterion.getOrder();
                result = compare(member, o1, o2);
                if (result != 0) {
                    return order == Order.ASCENDING ? result : -result;
                }
            }
            return 0;
        }
    }

}