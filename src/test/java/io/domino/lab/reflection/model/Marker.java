package io.domino.lab.reflection.model;

import java.lang.annotation.*;

/**
 * Created by amazimpaka on 2018-05-05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Marker {
}
