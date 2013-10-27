package hsw.vkapiapp4.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private HttpContent createProfileRequestContent(int first_id, int last_id) {
        StringBuilder sb = new StringBuilder();
        for (int id = first_id; id <= last_id; id++) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(Integer.toString(id));
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("v", "5.2");
        map.put("user_ids", sb.toString());
        return new UrlEncodedContent(map);
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

            GenericUrl url = new GenericUrl("http://api.vk.com/method/users.get");
            HttpContent content = createProfileRequestContent(1, 1000);
            Log.d("VkLoader", "URL = " + url);
            Log.d("VkLoader", "Content = " + content);
            HttpRequest request = requestFactory.buildPostRequest(url, content);
            Log.d("VkLoader", "Send req");
            ApiResponse response = request.execute().parseAs(ApiResponse.class);
            Log.d("VkLoader", "Parse response");
            profiles = response.profiles;
            Log.d("VkLoader", "Got " + profiles.size() + " profiles");
            return profiles;
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
