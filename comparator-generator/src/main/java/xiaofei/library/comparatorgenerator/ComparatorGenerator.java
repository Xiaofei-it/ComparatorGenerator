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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        TreeMap<Integer, SortingCriterion> map = AnnotationUtils.getCriterionAnnotation(clazz);
        for (Map.Entry<Integer, SortingCriterion> entry : map.entrySet()) {
            SortingCriterion prev = criteria.put(entry.getKey(), entry.getValue());
            if (prev != null) {
                throw new RuntimeException(
                        "The priority value " + entry.getKey() + " has already been set to field "
                                + prev.getField().getName() + ". Please specify another priority value.");
            }
        }
    }

    public ComparatorGenerator<T> addCriterion(int priority, String fieldName) {
        return addCriterion(priority, fieldName, Order.ASCENDING);
    }

    public ComparatorGenerator<T> addCriterion(int priority, String fieldName, Order order) {
        Field field = TypeUtils.getField(clazz, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("The field " + fieldName + " does not exist.");
        }
        TypeUtils.checkField(field);
        SortingCriterion prev = criteria.put(priority, new SortingCriterion(field, order));
        if (prev != null) {
            throw new IllegalArgumentException(
                    "The priority value " + priority + " has already been set to field "
                            + prev.getField().getName() + ". Please specify another priority value.");
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

        @Override
        public int compare(T o1, T o2) {
            if (o1 == null || o2 == null) {
                throw new NullPointerException("Argument is null.");
            }
            for (SortingCriterion criterion : criteria) {
                Field field = criterion.getField();
                Object v1, v2;
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    v1 = field.get(o1);
                    v2 = field.get(o2);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e.getMessage());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
                if (v1 == null) {
                    throw new NullPointerException("Field " + field.getName() + " of " + o1 + " is null.");
                }
                if (v2 == null) {
                    throw new NullPointerException("Field " + field.getName() + " of " + o2 + " is null.");
                }
                Order order = criterion.getOrder();
                if (v1 instanceof Comparable && v2 instanceof Comparable) {
                    @SuppressWarnings("unchecked")
                    int result = (Comparable.class.cast(v1)).compareTo(Comparable.class.cast(v2));
                    if (result != 0) {
                        return order == Order.ASCENDING ? result : -result;
                    }
                } else {
                    Class<?> type = field.getType();
                    int result = 0;
                    //boolean, byte, char, short, int, long, float, and double.
                    if (type == boolean.class) {
                        result = Boolean.valueOf((boolean) v1).compareTo((boolean) v2);
                    } else if (type == byte.class) {
                        result = Byte.valueOf((byte) v1).compareTo((byte) v2);
                    } else if (type == char.class) {
                        result = Character.valueOf((char) v1).compareTo((char) v2);
                    } else if (type == short.class) {
                        result = Short.valueOf((short) v1).compareTo((short) v2);
                    } else if (type == int.class) {
                        result = Integer.valueOf((int) v1).compareTo((int) v2);
                    } else if (type == long.class) {
                        result = Long.valueOf((long) v1).compareTo((long) v2);
                    } else if (type == float.class) {
                        result = Float.valueOf((float) v1).compareTo((float) v2);
                    } else if (type == double.class) {
                        result = Double.valueOf((double) v1).compareTo((double) v2);
                    } else {
                        throw new RuntimeException(
                                "Congratulations! You have found a bug"
                                        + " which I do not know. Please tell me so that"
                                        + " I can add it here.");
                    }
                    if (result != 0) {
                        return order == Order.ASCENDING ? result : -result;
                    }
                }
            }
            return 0;
        }
    }

    static class SortingCriterion {

        private Field field;

        private Order order;

        SortingCriterion(Field field, Order order) {
            this.field = field;
            this.order = order;
        }

        Field getField() {
            return field;
        }

        Order getOrder() {
            return order;
        }

    }

}