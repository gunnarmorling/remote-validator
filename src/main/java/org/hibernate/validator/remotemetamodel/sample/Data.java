package org.hibernate.validator.remotemetamodel.sample;

import javax.validation.constraints.Size;

/**
 * Created by hendrikebbers on 21.07.17.
 */
public class Data {

    @Size(min = 3)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
