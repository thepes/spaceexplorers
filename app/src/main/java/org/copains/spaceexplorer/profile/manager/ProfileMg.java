package org.copains.spaceexplorer.profile.manager;

import android.util.Log;

import org.copains.spaceexplorer.profile.objects.UserProfile;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 04/07/2016.
 */

public class ProfileMg {

    public static UserProfile getPlayerProfile() {
        UserProfile ret;
        List<UserProfile> profiles = UserProfile.find(UserProfile.class, "user_type = ?",""+UserProfile.TYPE_PLAYER);
        if ((null == profiles) || (profiles.size() == 0)) {
            ret = initProfile(UserProfile.TYPE_PLAYER);
            Log.i("spaceexplorers", "Profile Not found, creating it");
        } else {
            ret = profiles.get(0);
            Log.i("spaceexplorers", "Profile found created : " + ret.getCreationDate().toString());
        }
        return ret;
    }

    private static UserProfile initProfile(Integer playerType) {
        UserProfile prof = new UserProfile();
        prof.setUserType(playerType);
        prof.setUserName("123456789");
        prof.setCreationDate(Calendar.getInstance().getTime());
        prof.save();
        return prof;
    }

}
