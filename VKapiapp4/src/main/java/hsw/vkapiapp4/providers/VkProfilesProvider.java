package hsw.vkapiapp4.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hsw.vkapiapp4.loaders.VkProfilesCursor;
import hsw.vkapiapp4.vk.GetUsersApiResponse;
import hsw.vkapiapp4.vk.VkProfile;

public class VkProfilesProvider extends ContentProvider {
    static final String LOG_TAG = "VkLoader provider";

    static final String API_USERS_GET_URL = "http://api.vk.com/method/users.get";

    static final String AUTHORITY = "vkprofiles";

    static final String PROFILE_PATH = "profiles";

    public static final Uri PROFILE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PROFILE_PATH);

    static final String PROFILE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PROFILE_PATH;

    static final String PROFILE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PROFILE_PATH;

    private static final int URI_PROFILES = 1;

    // Uri с указанным ID
    private static final int URI_PROFILES_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, PROFILE_PATH, URI_PROFILES);
        sUriMatcher.addURI(AUTHORITY, PROFILE_PATH + "/#", URI_PROFILES_ID);
    }

    private static final GenericUrl mUrl = new GenericUrl(API_USERS_GET_URL);

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static final HttpRequestFactory requestFactory =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                                                    @Override
                                                    public void initialize(HttpRequest request) {
                                                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                                                    }
                                                }
            );

    /**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main thread at application launch time.  It must not perform
     * lengthy operations, or application startup will be delayed.
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "provider onCreate");
        return true;
    }

    private HttpContent createProfileRequestContent(int first_id, int last_id, String[] projection) {
        StringBuilder sb_ids = new StringBuilder();
        for (int id = first_id; id <= last_id; id++) {
            if (sb_ids.length() > 0) {
                sb_ids.append(",");
            }
            sb_ids.append(Integer.toString(id));
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("v", "5.2");
        map.put("user_ids", sb_ids.toString());

        if (projection != null && projection.length > 0) {
            StringBuilder sb_fields = new StringBuilder();
            for (String p : projection) {
                if (!p.equals("_ID") &&
                        !p.equals("first_name") &&
                        !p.equals("last_name") &&
                        !p.equals("full_name")) {

                    if (sb_fields.length() > 0) {
                        sb_fields.append(",");
                    }
                    sb_fields.append(p);
                }
            }
            if (sb_fields.length() > 0) {
                map.put("fields", sb_fields.toString());
            }
        }
        Log.d(LOG_TAG, "provider content request: " + map);
        return new UrlEncodedContent(map);
    }

    /**
     * Implement this to handle query requests from clients.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<VkProfile> profiles;
        int first_id = 1;
        int last_id = 100;

        Log.d(LOG_TAG, "provider query " + uri.toString());

        switch (sUriMatcher.match(uri)) {
            case URI_PROFILES:
                if (selectionArgs != null && selectionArgs.length > 1) {
                    first_id = Integer.parseInt(selectionArgs[0]);
                    last_id = Integer.parseInt(selectionArgs[1]);
                }
                break;
            case URI_PROFILES_ID:
                int id = Integer.parseInt(uri.getLastPathSegment());
                first_id = last_id = id;
                Log.d(LOG_TAG, "id = " + id);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Log.d(LOG_TAG, "provider query " + first_id + "-" + last_id + " rows, projection: " + Arrays.toString(projection));

        try {
            HttpContent content = createProfileRequestContent(first_id, last_id, projection);
            HttpRequest request = requestFactory.buildPostRequest(mUrl, content);
            Log.d(LOG_TAG, "Send req");
            GetUsersApiResponse response = request.execute().parseAs(GetUsersApiResponse.class);
            Log.d(LOG_TAG, "Parse response");
            profiles = response.profiles;
            Log.d(LOG_TAG, "Got " + profiles.size() + " profiles");
        } catch (IOException t) {
            //t.printStackTrace();
            throw new RuntimeException(t);
        }

        VkProfilesCursor cursor = new VkProfilesCursor(projection, last_id - first_id + 1);
        cursor.fill(profiles);
        return cursor;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>Note that there are no permissions needed for an application to
     * access this information; if your content provider requires read and/or
     * write permissions, or is not exported, all applications can still call
     * this method regardless of their access permissions.  This allows them
     * to retrieve the MIME type for a URI when dispatching intents.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case URI_PROFILES:
                return PROFILE_CONTENT_TYPE;
            case URI_PROFILES_ID:
                return PROFILE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
