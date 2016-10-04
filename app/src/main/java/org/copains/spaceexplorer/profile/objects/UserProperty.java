package org.copains.spaceexplorer.profile.objects;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 03/10/2016.
 */

public class UserProperty extends SugarRecord {


    private Long id;
    @Unique
    private String name;
    private String value;
    private Integer type;

    public UserProperty() {

    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
