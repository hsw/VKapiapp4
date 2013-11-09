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
    static final String LOG_TAG = "VkLoader detail";
    private static final int VKPROFILES_LOADER = 1;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private int mProfileId;

    private static final Uri mDataUrl = VkProfilesProvider.PROFILE_CONTENT_URI;

    private static final String[] mProjection = {"_ID", "full_name", "nickname", "screen_name", "sex", "bdate", "city", "country", "timezone", "photo_50", "photo_100", "photo_200_orig", "has_mobile", "contacts", "education", "online", "counters", "relation", "last_seen", "status", "universities", "schools", "verified"};

    //private final String[] mFromColumns = {"_ID", "full_name"};

    //private final int[] mToFields = {android.R.mProfileId.text1, android.R.mProfileId.text2};

    private LayoutInflater mInflater;
    private TableLayout mTableLayout;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader " + loaderID);
        switch (loaderID) {
            case VKPROFILES_LOADER:
                return new CursorLoader(
                        getActivity(),
                        mDataUrl.buildUpon().appendPath(Integer.toString(mProfileId)).build(),
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
        fillTable(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> listLoader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    private void fillTable(Cursor cursor) {
        if (cursor.moveToFirst()) {
            Log.d(LOG_TAG, "fillTable");
            for (int i = 0; i < mProjection.length; i++) {
                TableRow tableRow = (TableRow) mInflater.inflate(R.layout.table_row, mTableLayout, false);
                tableRow.setTag(i);
                ((TextView) tableRow.findViewById(R.id.tvRowName)).setText(mProjection[i]);
                ((TextView) tableRow.findViewById(R.id.tvRowValue)).setText(cursor.getString(i));
                mTableLayout.addView(tableRow);
            }
        }
    }

    public static ItemDetailFragment newInstance(int page) {
        ItemDetailFragment ItemDetailFragment = new ItemDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_ITEM_ID, page);
        ItemDetailFragment.setArguments(arguments);
        return ItemDetailFragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
        Log.d(LOG_TAG, "default constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mProfileId = getArguments().getInt(ARG_ITEM_ID);
            Log.d(LOG_TAG, "detail mProfileId=" + mProfileId);
        }

        mInflater = getLayoutInflater(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        //tvId = (TextView) rootView.findViewById(R.mProfileId.item_detail);
        mTableLayout = (TableLayout) rootView.findViewById(R.id.table_layout);
        // Show the dummy content as text in a TextView.

        /*
        if (mProfileId > 0) {
            tvId.setText(Integer.toString(mProfileId));
        }
        */

        Log.d(LOG_TAG, "initLoader");
        getLoaderManager().initLoader(VKPROFILES_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "onActivityCreated");
    }
}
