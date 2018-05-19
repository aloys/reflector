package io.domino.lab.reflection.model;

/**
 * Created by amazimpaka on 2018-05-04
 */
public class SimpleType extends AbstractType{

    private String name;

    private String readableProperty;

    private String writeableProperty;


    public String getReadableProperty() {
        return readableProperty;
    }

    public void setWriteableProperty(String writeableProperty) {
        this.writeableProperty = writeableProperty;
    }
}
