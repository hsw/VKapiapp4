package hsw.vkapiapp4.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import hsw.vkapiapp4.vk.ApiResponse;
import hsw.vkapiapp4.vk.VkProfile;

public class VkProfileLoader extends AsyncTaskLoader<VkProfile> {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private VkProfile profile;

    public VkProfileLoader(Context context) {
        super(context);
        Log.d("VkLoader", "P Constructor");
    }

    public static class VkProfileUrl extends GenericUrl {
        @Key
        public String v = "5.2";

        @Key
        public String fields = "nickname,screen_name,sex,bdate,city,country,timezone,photo_50,photo_100,photo_200_orig,has_mobile,contacts,education,online,counters,relation,last_seen,status,can_write_private_message,can_see_all_posts,can_see_audio,can_post,universities,schools,verified";

        @Key
        public String user_ids;

        public VkProfileUrl(int user_id) {
            super("http://api.vk.com/method/users.get");
            user_ids = Integer.toString(user_id);
        }
    }

    @Override
    public VkProfile loadInBackground() {
        Log.d("VkLoader", "P loadInBackground");
        try {
            HttpRequestFactory requestFactory =
                    HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {
                            request.setParser(new JsonObjectParser(JSON_FACTORY));
                        }
                    }
                    );

            GenericUrl url = new VkProfileUrl(1);
            HttpRequest request = requestFactory.buildGetRequest(url);
            Log.d("VkLoader", "P Send req");
            ApiResponse response = request.execute().parseAs(ApiResponse.class);
            Log.d("VkLoader", "P Parse response");
            this.profile = response.profiles.get(0);
            return this.profile;
        } catch (Throwable t) {
            //t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d("VkLoader", "P onStartLoading");
        if (profile != null) {
            deliverResult(profile);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(VkProfile profile) {
        super.deliverResult(profile);
        Log.d("VkLoader", "P deliverResult");
        //adapter.notifyDataSetChanged();
    }
}
