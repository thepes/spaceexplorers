package org.copains.spaceexplorer.profile.manager;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.copains.spaceexplorer.SpaceExplorerApplication;
import org.copains.spaceexplorer.backend.userRecordApi.UserRecordApi;
import org.copains.spaceexplorer.backend.userRecordApi.model.UserRecord;
import org.copains.spaceexplorer.profile.objects.UserProfile;

import java.io.IOException;
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
        UserRecordApi.Builder recordApiBuilder = new UserRecordApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                // otherwise they can be skipped
                .setRootUrl(SpaceExplorerApplication.BASE_WS_URL)
                /*.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                            throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                })*/;
        UserRecordApi recordApi = recordApiBuilder.build();
        try {
            UserRecord record = recordApi.create(playerType).execute();
            prof.setUserName(record.getUserName());
            prof.setCreationDate(Calendar.getInstance().getTime());
            prof.setOnlineId(record.getId());
            prof.save();
            return prof;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: handle error case by creating a local pofile
        return null;
    }

}
