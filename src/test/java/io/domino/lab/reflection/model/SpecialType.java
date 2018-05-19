package io.domino.lab.reflection.model;

/**
 * Created by amazimpaka on 2018-05-04
 */
@Marker
public class SpecialType {

    public static final double PI = Math.PI;

    private final String name;


    public SpecialType(String name) {
        this.name = name;
    }
}
