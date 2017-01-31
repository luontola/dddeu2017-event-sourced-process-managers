package dddeu2017.espm.util;

import dddeu2017.espm.Item;
import dddeu2017.espm.Order;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public abstract class Struct {

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, new MultilineRecursiveToStringStyle() {
            @Override
            protected boolean accept(Class<?> clazz) {
                return clazz == Order.class || clazz == Item.class;
            }
        }).toString();
    }
}
