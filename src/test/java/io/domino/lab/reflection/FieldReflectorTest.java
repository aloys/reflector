package io.domino.lab.reflection;

import io.domino.lab.reflection.exception.ReflectionException;
import io.domino.lab.reflection.model.ComplexType;
import io.domino.lab.reflection.model.SimpleType;
import io.domino.lab.reflection.model.SpecialType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

/**
 * Created by amazimpaka on 2018-05-05
 */
public class FieldReflectorTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getAllFields() {
        List<Field> fields = FieldReflector.getAllFields(ComplexType.class);

        Assert.assertNotNull(fields);
        Assert.assertFalse(fields.isEmpty());
        Assert.assertEquals(fields.toString(),5,fields.size());

        Map<String,Class<?>> fieldsTypes = fields
                .stream()
                .collect(Collectors.toMap(Field::getName, Field::getType));

        Assert.assertTrue(fieldsTypes.keySet().contains("simpleType"));
        Assert.assertTrue(fieldsTypes.get("simpleType") == SimpleType.class);

        Assert.assertTrue(fieldsTypes.keySet().contains("enabled"));
        Assert.assertTrue(fieldsTypes.get("enabled") == Boolean.TYPE);

        Assert.assertTrue(fieldsTypes.keySet().contains("id"));
        Assert.assertTrue(fieldsTypes.get("id") == Long.TYPE);

        Assert.assertTrue(fieldsTypes.keySet().contains("name"));
        Assert.assertTrue(fieldsTypes.get("name") == String.class);
    }



    @Test
    public void getField() {
        Optional<Field> field = FieldReflector.getField(ComplexType.class,"simpleType");
        Assert.assertTrue(field.isPresent());
        Assert.assertTrue(field.get().getType() == SimpleType.class);

        Assert.assertFalse(FieldReflector.getField(ComplexType.class,"unexisting").isPresent());
    }

    @Test
    public void setAndGetValue() {
        final ComplexType complexType = new ComplexType();

        final SimpleType simpleType = new SimpleType();
        Assert.assertNull(complexType.getSimpleType());
        FieldReflector.setValue(complexType,"simpleType",simpleType);
        Assert.assertEquals(simpleType,FieldReflector.getValue(complexType,"simpleType"));
        Assert.assertEquals(simpleType,complexType.getSimpleType());


        final String name = "test-" + System.currentTimeMillis();
        Assert.assertNull(complexType.getName());
        FieldReflector.setValue(complexType,"name", name);
        Assert.assertEquals(name,FieldReflector.getValue(complexType,"name"));
        Assert.assertEquals(name,complexType.getName());

        Assert.assertFalse(complexType.isEnabled());
        FieldReflector.setValue(complexType,"enabled",true);
        Assert.assertEquals(true,FieldReflector.getValue(complexType,"enabled"));
        Assert.assertEquals(true,complexType.isEnabled());


        Assert.assertEquals(0,complexType.getId());
        FieldReflector.setValue(complexType,"id",100L);
        Assert.assertEquals(100L,FieldReflector.getValue(complexType,"id"));
        Assert.assertEquals(100L,complexType.getId());

    }


    @Test
    public void setValue_InvalidCases() throws IllegalAccessException {

        final ComplexType complexType = new ComplexType();
        // Set invalid field value
        Assert.assertNull(FieldReflector.getValue(complexType,"invalidField"));
        FieldReflector.setValue(complexType,"invalidField",new Object());


        // Check illegal access
        expectedException.expect(ReflectionException.class);

        Field mockField = Mockito.mock(Field.class);
        doThrow(new IllegalAccessException()).when(mockField).set(any(),any());
        FieldReflector.setValue(complexType,mockField,new Object());

    }

    @Test
    public void getValue_InvalidCases() throws IllegalAccessException {

        final ComplexType complexType = new ComplexType();
        // Get invalid field value
        Assert.assertNull(FieldReflector.getValue(complexType, "invalidField"));

        // Change final field value
        FieldReflector.setValue(complexType, "date", new Date());


        // Check illegal access
        expectedException.expect(ReflectionException.class);

        Field mockField = Mockito.mock(Field.class);
        doThrow(new IllegalAccessException()).when(mockField).get(any());
        FieldReflector.getValue(complexType, mockField);
    }

    @Test
    public void isFinal() {
        Optional<Field> optionalDateField = FieldReflector.getField(ComplexType.class, "date");
        Assert.assertTrue(optionalDateField.isPresent());
        Assert.assertTrue(FieldReflector.isFinal(optionalDateField.get()));

        Optional<Field> optionalNameField = FieldReflector.getField(ComplexType.class, "name");
        Assert.assertTrue(optionalNameField.isPresent());
        Assert.assertFalse(FieldReflector.isFinal(optionalNameField.get()));
    }


    @Test
    public void isStatic(){
        Optional<Field> optionalDoubleField = FieldReflector.getField(SpecialType.class, "PI");
        Assert.assertTrue(optionalDoubleField.isPresent());
        Assert.assertTrue(FieldReflector.isStatic(optionalDoubleField.get()));

        Optional<Field> optionalNameField = FieldReflector.getField(SpecialType.class, "name");
        Assert.assertTrue(optionalNameField.isPresent());
        Assert.assertFalse(FieldReflector.isStatic(optionalNameField.get()));
    }


}
