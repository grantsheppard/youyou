package com.googlecode.youyou;

import com.google.common.base.Defaults;
import com.google.common.primitives.Primitives;
import org.fest.reflect.exception.ReflectionError;
import org.junit.Test;

import static com.google.common.base.Defaults.defaultValue;
import static com.googlecode.youyou.ObjectMother.Field.field;
import static com.googlecode.youyou.ObjectMother.Field.fields;
import static com.googlecode.youyou.ObjectMother.create;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class ObjectMotherTest {

    @Test
    public void giveBirthToAnObject() {
        Person person = create(Person.class).build();

        assertThat(person, is(notNullValue()));
    }

    @Test
    public void giveBirthToAnObjectWithOnePrivateFieldPopulated() {
        Person person = create(Person.class).with(field("name", "Grant")).build();

        assertThat(person.name, is(equalTo("Grant")));
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
    public void giveBirthToAnObjectWithRandomFieldsPopulated() throws Exception {
//        Person person = create(Person.class).build();
//
//        assertThat(person.name, is(notNullValue()));
//        assertThat(person.phone, is(notNullValue()));
//        assertThat(person.age, is(not(equalTo(defaultValue(int.class)))));
//        assertThat(person.height, is(notNullValue()));
    }

    @Test
    public void giveBirthToAnObjectWithoutDefaultConstructor() throws Exception {
        //fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotGiveBirthToAnObjectWhenFieldDoesNotExist() {
        create(Person.class).with(field("missing", "field")).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotGiveBirthToAnObjectWhenValueForFieldIsWrongType() {
        create(Person.class).with(field("name", 1234)).build();
    }

    private static class Person {

        public Person() {

        }

        private String name;
        String phone;
        int age;
        Integer height;
    }
}
