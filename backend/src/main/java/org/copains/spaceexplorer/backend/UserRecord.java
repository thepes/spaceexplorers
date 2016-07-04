package org.copains.spaceexplorer.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 01/07/2016.
 */
@Entity
public class UserRecord {

    @Id
    private Long id;
    @Index
    private String userName;
    private String deviceId;

    public UserRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


}
