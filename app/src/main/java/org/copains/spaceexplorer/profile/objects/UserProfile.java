package org.copains.spaceexplorer.profile.objects;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 04/07/2016.
 */

public class UserProfile extends SugarRecord {

    public static final Integer TYPE_MASTER = 2;
    public static final Integer TYPE_PLAYER = 1;


    private String userName;
    private Long onlineId;
    private Date creationDate;
    private Integer userType;

    public UserProfile() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(Long onlineId) {
        this.onlineId = onlineId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

}
