package com.googlecode.youyou.test;

import com.google.common.reflect.TypeToken;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.google.common.base.Defaults.defaultValue;
import static com.googlecode.youyou.test.ObjectMother.Field.field;
import static com.googlecode.youyou.test.ObjectMother.Field.fields;
import static com.googlecode.youyou.test.ObjectMother.create;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ObjectMotherTest {

    @Test
    public void giveBirthToAnObject() {
        Person person = create(Person.class).build();

        assertThat(person, is(notNullValue()));
    }

    @Test
    public void giveBirthToAnObjectWithPrivateFieldPopulated() {
        Person person = create(Person.class).with(field("name", "Grant")).build();

        assertThat(person.name, is(equalTo("Grant")));
    }

    @Test
    public void giveBirthToAnObjectWithFinalFieldPopulated() {
        Person person = create(Person.class).with(field("address", "12 Some St")).build();

        assertThat(person.address, is(equalTo("12 Some St")));
    }

    @Test
    public void giveBirthToAnObjectWithMultipleFieldsOfTheSameTypePopulated() {
        Person person = create(Person.class).with(fields("name", "Grant", "phone", "55659984")).build();

        assertThat(person.name, is(equalTo("Grant")));
        assertThat(person.phone, is(equalTo("55659984")));
    }

    @Test
    public void giveBirthToAnObjectWithPrimitiveFieldPopulated() {
        Person person = create(Person.class).with(field("age", 35)).build();

        assertThat(person.age, is(equalTo(35)));
    }

    @Test
    public void giveBirthToAnObjectWithBoxedPrimitiveFieldPopulated() {
        Person person = create(Person.class).with(field("height", 182)).build();

        assertThat(person.height, is(equalTo(182)));
    }

    @Test
    public void giveBirthToAnObjectWithMultipleFieldsOfDifferentTypesPopulated() {
        Person person = create(Person.class).with(fields("name", "Grant", "age", 35)).build();

        assertThat(person.name, is(equalTo("Grant")));
        assertThat(person.age, is(equalTo(35)));
    }

    @Test
    public void giveBirthToAnObjectWithManyFieldsPopulated() {
        Person person = create(Person.class).with(field("name", "Grant"), field("phone", "55659984"),
                field("age", 35), field("height", 182)).build();

        assertThat(person.name, is(equalTo("Grant")));
        assertThat(person.phone, is(equalTo("55659984")));
        assertThat(person.age, is(equalTo(35)));
        assertThat(person.height, is(equalTo(182)));
    }

    @Test
    public void giveBirthToAnObjectWithNonReferencedFieldsPopulatedWithRandomValues() throws Exception {
        Person person = create(Person.class).withRandomFields().build();

        assertThat(person.name, is(notNullValue()));
        assertThat(person.phone, is(notNullValue()));
        assertThat(person.age, is(not(equalTo(defaultValue(int.class)))));
        assertThat(person.height, is(notNullValue()));
    }

    @Test
    public void giveBirthToAnObjectWithRandomisedFieldsFromParentClass() throws Exception {
        Assert.fail();
    }

    @Test
    public void giveBirthToAnObjectWithFieldsPopulatedFromUserProvidedTypeRandomiserFactory() {
        FieldValueRandomiserFactory factory = createTypeRandomiserFactoryThatReturnsDefaultValuesByType();

        Person person = create(Person.class).withTypeRandomiserFactory(factory).build();

        assertThat(person.name, is(nullValue()));
        assertThat(person.name, is(nullValue()));
        assertThat(person.age, is(equalTo(0)));
        assertThat(person.height, is(nullValue()));
        assertThat(person.address, is(nullValue()));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void cannotGiveBirthToAnObjectWhenFieldDoesNotExist() {
        create(Person.class).with(field("missing", "field")).build();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void cannotGiveBirthToAnObjectWhenValueForFieldIsWrongType() {
        create(Person.class).with(field("name", 1234)).build();
    }

    private FieldValueRandomiserFactory createTypeRandomiserFactoryThatReturnsDefaultValuesByType() {
        return new FieldValueRandomiserFactory() {
            @Override
            public <T> FieldValueRandomiser<T> createFor(T type) {
                return new FieldValueRandomiser<T>() {
                    @Override
                    public T nextRandom() {
                        return (T) defaultValue(new TypeToken<T>() {}.getRawType());
                    }
                };
            }
        };
    }

    private static class Person {

        private String name;
        String phone;
        int age;
        Integer height;
        final String address;

        public Person(String address) {
            this.address = address;
        }
    }
}
