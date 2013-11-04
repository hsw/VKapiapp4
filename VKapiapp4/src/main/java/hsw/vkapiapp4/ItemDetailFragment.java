package hsw.vkapiapp4;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import hsw.vkapiapp4.providers.VkProfilesProvider;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    final String LOG_TAG = "VkLoader detail";
    private static final int VKPROFILES_LOADER = 0;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private int id;

    private final Uri mDataUrl = VkProfilesProvider.PROFILE_CONTENT_URI;

    private int mListItemLayout = android.R.layout.simple_list_item_activated_2;

    private final String[] mProjection = {"_ID", "full_name", "nickname", "screen_name", "sex", "bdate", "city", "country", "timezone", "photo_50", "photo_100", "photo_200_orig", "has_mobile", "contacts", "education", "online", "counters", "relation", "last_seen", "status", "universities", "schools", "verified"};

    //private final String[] mFromColumns = {"_ID", "full_name"};

    //private final int[] mToFields = {android.R.id.text1, android.R.id.text2};

    LayoutInflater inflater;
    private TextView tvId;
    private TableLayout tableLayout;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader");
        switch (loaderID) {
            case VKPROFILES_LOADER:
                return new CursorLoader(
                        getActivity(),
                        mDataUrl.buildUpon().appendPath(Integer.toString(id)).build(),
                        mProjection,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");

        if (cursor.moveToNext()) {
            for (int i = 0; i < mProjection.length; i++) {
                Log.d(LOG_TAG, "add row " + mProjection[i] + " = " + cursor.getString(i));
                TableRow tableRow = (TableRow) inflater.inflate(R.layout.table_row, tableLayout, false);
                tableRow.setTag(i);
                ((TextView) tableRow.findViewById(R.id.tvRowName)).setText(mProjection[i]);
                ((TextView) tableRow.findViewById(R.id.tvRowValue)).setText(cursor.getString(i));
                tableLayout.addView(tableRow);
            }
        }
        //mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> listLoader) {
        Log.d(LOG_TAG, "onLoaderReset");
        //mAdapter.changeCursor(null);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            id = getArguments().getInt(ARG_ITEM_ID);
            Log.d(LOG_TAG, "detail id=" + id);
        }

        inflater = getLayoutInflater(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        tvId = (TextView) rootView.findViewById(R.id.item_detail);
        tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout);
        // Show the dummy content as text in a TextView.
        if (id > 0) {
            tvId.setText(Integer.toString(id));
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "frag onActivityCreated, initLoader");
        getLoaderManager().initLoader(VKPROFILES_LOADER, null, this);
    }
}
