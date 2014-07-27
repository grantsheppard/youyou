package com.googlecode.youyou.test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Primitives;
import org.fest.reflect.exception.ReflectionError;
import org.objenesis.ObjenesisStd;

import java.util.Set;

import static com.google.common.collect.Iterables.contains;
import static com.google.common.collect.Lists.asList;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.primitives.Primitives.isWrapperType;
import static org.fest.reflect.core.Reflection.field;

public class ObjectMother<T> {
    private Class<T> clazz;
    private Set<Field> fields = newHashSet();
    private boolean randomiseFields;
    private FieldValueRandomiserFactory fieldValueRandomiserFactory = new TypeDefaultFieldValueRandomiserFactory();

    private ObjectMother(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> ObjectMother<T> create(Class<T> clazz) {
        return new ObjectMother<T>(clazz);
    }

    public ObjectMother<T> with(Field field, Field ... fields) {
        this.fields.addAll(asList(field, fields));
        return this;
    }

    public ObjectMother<T> with(Iterable<Field> fields) {
        Iterables.addAll(this.fields, fields);
        return this;
    }

    public T build() {
        T object = new ObjenesisStd().newInstance(clazz);
        if (randomiseFields)
            randomiseFields();
        if (!fields.isEmpty())
            for (Field field : fields) {
                Class clazz = determineFieldType(object, field);
                try {
                    field(field.name).ofType(clazz).in(object).set(field.value);
                } catch (ReflectionError e) {
                    throw new IllegalArgumentException(e);
                }
            }
        return object;
    }

    private void randomiseFields() {
        for (final java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            if (!contains(fields, new Predicate<java.lang.reflect.Field>() {
                @Override
                public boolean apply(java.lang.reflect.Field input) {
                    for (Field field : fields) {
                        return field.name.equals(input.getName());
                    }
                    return false;
                }
            })) {
                fields.add(Field.field(field.getName(), fieldValueRandomiserFactory.createFor(field.getType())));
            }
        }
    }

    private Class determineFieldType(T object, Field field) {
        Class clazz = field.value.getClass();
        if (isFieldPrimitive(object, field, clazz))
            clazz = Primitives.unwrap(clazz);
        return clazz;
    }

    private boolean isFieldPrimitive(T object, Field field, Class clazz) {
        try {
            return isWrapperType(clazz) && !isWrapperType(object.getClass().getDeclaredField(field.name).getType());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectMother<T> withRandomFields() {
        this.randomiseFields = true;
        return this;
    }

    public ObjectMother<T> withTypeRandomiserFactory(FieldValueRandomiserFactory fieldValueRandomiserFactory) {
        this.fieldValueRandomiserFactory = fieldValueRandomiserFactory;
        return this;
    }

    public static class Field {
        final String name;
        final Object value;

        public Field(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public static Field field(String name, Object value) {
            return new Field(name, value);
        }

        public static Iterable<Field> fields(String name1, Object value1, String name2, Object value2) {
            return ImmutableSet.of(new Field(name1, value1), new Field(name2, value2));
        }

        public static Iterable<Field> fields(String name1, Object value1, String name2, Object value2,
                                             String name3, Object value3) {
            return ImmutableSet.of(new Field(name1, value1), new Field(name2, value2), new Field(name3, value3));
        }
    }
}
