package hsw.vkapiapp4.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

public class VkProfilesCursorLoader extends CursorLoader {
    /**
     * Creates a fully-specified CursorLoader.  See
     * {@link android.content.ContentResolver#query(android.net.Uri, String[], String, String[], String)
     * ContentResolver.query()} for documentation on the meaning of the
     * parameters.  These will be passed as-is to that call.
     *
     * @param context
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     */
    public VkProfilesCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
