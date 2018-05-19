package io.domino.lab.reflection;

import io.domino.lab.reflection.exception.ReflectionException;
import io.domino.lab.reflection.model.ComplexType;
import io.domino.lab.reflection.model.SimpleType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

/**
 * Created by amazimpaka on 2018-05-06
 */
public class PropertyReflectorTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();


    @Test
    public void getPropertyDescriptors() {
        List<PropertyDescriptor> propertyDescriptors = PropertyReflector.getPropertyDescriptors(ComplexType.class);

        Assert.assertNotNull(propertyDescriptors);
        Assert.assertFalse(propertyDescriptors.isEmpty());
        Assert.assertEquals(propertyDescriptors.toString(),5,propertyDescriptors.size());

        Map<String,PropertyDescriptor> propertyDescriptorsMap = propertyDescriptors
                .stream().collect(Collectors.toMap(PropertyDescriptor::getName, propertyDescriptor -> propertyDescriptor));

        Assert.assertTrue(propertyDescriptorsMap.keySet().contains("simpleType"));
        Assert.assertTrue(propertyDescriptorsMap.get("simpleType").getPropertyType() == SimpleType.class);

        Assert.assertTrue(propertyDescriptorsMap.keySet().contains("enabled"));
        Assert.assertTrue(propertyDescriptorsMap.get("enabled").getPropertyType() == Boolean.TYPE);

        Assert.assertTrue(propertyDescriptorsMap.keySet().contains("id"));
        Assert.assertTrue(propertyDescriptorsMap.get("id").getPropertyType() == Long.TYPE);

        Assert.assertTrue(propertyDescriptorsMap.keySet().contains("name"));
        Assert.assertTrue(propertyDescriptorsMap.get("name").getPropertyType() == String.class);

    }

    @Test
    public void getValue() {
        ComplexType complexType = new ComplexType();
        complexType.setName("demo");
        Assert.assertEquals("demo",PropertyReflector.getValue(complexType,"name"));
    }

    @Test
    public void setValue() {
        ComplexType complexType = new ComplexType();
        PropertyReflector.setValue(complexType,"name","demo");
        Assert.assertEquals("demo",complexType.getName());
    }

    @Test
    public void getPropertyDescriptor() {
        PropertyDescriptor pd = PropertyReflector.getPropertyDescriptor(ComplexType.class,"name");
        Assert.assertNotNull(pd);
        Assert.assertEquals("getName",pd.getReadMethod().getName());
        Assert.assertEquals("setName",pd.getWriteMethod().getName());

        try {
            //Invalid property name
            PropertyReflector.getPropertyDescriptor(SimpleType.class, "invalidProperty");
            fail("Expected exception");
        } catch (ReflectionException e) {
            // expected exception
        }

        try {
            //Property without read or write method
            PropertyReflector.getPropertyDescriptor(SimpleType.class, "name");
            fail("Expected exception");
        } catch (ReflectionException e) {
            // expected exception
        }

    }

    @Test
    public void getMethod_NoWriteMethodFound() {
        expectedException.expect(ReflectionException.class);

        PropertyReflector.setValue(new SimpleType(),"readableProperty","test");
    }

    @Test
    public void getMethod_NoReadMethodFound() {
        expectedException.expect(ReflectionException.class);

        PropertyReflector.getValue(new SimpleType(),"writeableProperty");
    }


}
