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

import java.util.List;

import hsw.vkapiapp4.vk.ApiResponse;
import hsw.vkapiapp4.vk.VkProfile;

public class VkProfilesLoader extends AsyncTaskLoader<List<VkProfile>> {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private List<VkProfile> profiles;

    public VkProfilesLoader(Context context) {
        super(context);
        Log.d("VkLoader", "Constructor");
    }

    /**
     * URL for VK API.
     */
    public static class VkUrl extends GenericUrl {
        public VkUrl(String encodedUrl) {
            super(encodedUrl);
        }

        @Key
        public String fields;
    }

    @Override
    public List<VkProfile> loadInBackground() {
        Log.d("VkLoader", "loadInBackground");
        try {
            HttpRequestFactory requestFactory =
                    HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {
                            request.setParser(new JsonObjectParser(JSON_FACTORY));
                        }
                    }
                    );

            VkUrl url = new VkUrl("http://api.vk.com/method/users.get?v=5.2&user_ids=1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16");
            HttpRequest request = requestFactory.buildGetRequest(url);
            Log.d("VkLoader", "Send req");
            ApiResponse response = request.execute().parseAs(ApiResponse.class);
            Log.d("VkLoader", "Parse response");
            this.profiles = response.profiles;
            return this.profiles;
        } catch (Throwable t) {
            //t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d("VkLoader", "onStartLoading");
        if (profiles != null) {
            deliverResult(profiles);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<VkProfile> list) {
        super.deliverResult(list);
        Log.d("VkLoader", "deliverResult");
        //adapter.notifyDataSetChanged();
    }
}
