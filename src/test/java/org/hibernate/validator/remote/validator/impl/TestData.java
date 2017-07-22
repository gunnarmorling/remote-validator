package org.hibernate.validator.remote.validator.impl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestData {

    @NotNull
    @Size(min = 3, max = 6)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
