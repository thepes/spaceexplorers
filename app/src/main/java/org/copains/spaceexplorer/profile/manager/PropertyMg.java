package org.copains.spaceexplorer.profile.manager;

import android.util.Log;

import org.copains.spaceexplorer.profile.objects.UserProperty;

import java.util.List;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 03/10/2016.
 */

public class PropertyMg {

    public static final UserProperty getByName(String name) {
        List<UserProperty> props = UserProperty.find(UserProperty.class, "name = ?", name);
        if (null == props)
            return null;
        return props.get(0);
    }

    public static final UserProperty save(UserProperty prop) {
        if (null == prop.getId()) {
            UserProperty temp = getByName(prop.getName());
            if (null != temp) {
                prop.setId(temp.getId());
                Log.i("spaceexplorers","Found existing prop. Id = " + temp.getId());
            }
        }
        prop.save();
        Log.i("spaceexplorers","Saving prop : " + prop.getName());
        return prop;
    }

}
