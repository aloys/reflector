package io.domino.lab.reflection.model;

/**
 * Created by amazimpaka on 2018-05-04
 */
public abstract class AbstractType implements Type{

    private long id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
