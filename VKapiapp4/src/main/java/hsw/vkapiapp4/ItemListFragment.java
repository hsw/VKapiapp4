package hsw.vkapiapp4;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import hsw.vkapiapp4.providers.VkProfilesProvider;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    static final String LOG_TAG = "VkLoader list";
    private static final int VKPROFILES_LOADER = 0;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private static final Uri mDataUrl = VkProfilesProvider.PROFILE_CONTENT_URI;

    private static int mListItemLayout = android.R.layout.simple_list_item_activated_2;

    private static final String[] mProjection = {"_ID", "full_name", "status"};

    private static final String[] mFromColumns = {"_ID", "full_name"};

    private static final int[] mToFields = {android.R.id.text1, android.R.id.text2};

    private SimpleCursorAdapter mAdapter;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader");
        switch (loaderID) {
            case VKPROFILES_LOADER:
                return new CursorLoader(
                        getActivity(),
                        mDataUrl,
                        mProjection,
                        null,
                        null, // new String[]{"101", "200"},
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> listLoader) {
        Log.d(LOG_TAG, "onLoaderReset");
        mAdapter.changeCursor(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(int id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "frag onCreate");

        mAdapter = new SimpleCursorAdapter(
                getActivity(),                // Current context
                mListItemLayout,  // Layout for a single row
                null,                // No Cursor yet
                mFromColumns,        // Cursor columns to use
                mToFields,           // Layout fields to use
                0                    // No flags
        );

        setListAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "frag onViewCreated");

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "frag onActivityCreated, initLoader");
        getLoaderManager().initLoader(VKPROFILES_LOADER, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "frag onAttach");

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "frag onDetach");

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Log.d(LOG_TAG, "frag onListItemClick " + position + " " + id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected((int) id);
    }

    public void setCurrentItem(int id) {
        Log.d(LOG_TAG, "setCurrentItem = " + id);
        setActivatedPosition(id - 1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "frag onSaveInstanceState");
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        final ListView listView = getListView();
        if (position == ListView.INVALID_POSITION) {
            listView.setItemChecked(mActivatedPosition, false);
            listView.smoothScrollToPosition(mActivatedPosition);
        } else {
            listView.setItemChecked(position, true);
            listView.smoothScrollToPosition(position);
        }

        mActivatedPosition = position;
    }
}
