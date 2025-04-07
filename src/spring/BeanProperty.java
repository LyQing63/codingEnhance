package spring;

import java.lang.reflect.Field;

public class BeanProperty {
    private Field field;
    private boolean required;

    public BeanProperty(Field field, boolean required) {
        this.field = field;
        this.required = required;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
