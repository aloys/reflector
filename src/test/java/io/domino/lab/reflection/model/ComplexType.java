package io.domino.lab.reflection.model;

import java.util.Date;

/**
 * Created by amazimpaka on 2018-05-04
 */
public class ComplexType extends AbstractType {

   private SimpleType simpleType;

   private boolean enabled;

   private String name;

   private final Date date = new Date();

   public SimpleType getSimpleType() {
      return simpleType;
   }

   public void setSimpleType(SimpleType simpleType) {
      this.simpleType = simpleType;
   }

   public boolean isEnabled() {
      return enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

    public Date getDate() {
        return date;
    }
}
