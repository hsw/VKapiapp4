package hsw.vkapiapp4;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hsw.vkapiapp4.providers.VkProfilesPagerAdapter;

public class ItemPagerFragment extends Fragment implements ViewPager.OnPageChangeListener {

    static final String LOG_TAG = "VkLoader pager";

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private int mProfileId;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onPageSelected(int id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onPageSelected(int id) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        mPagerAdapter = new VkProfilesPagerAdapter(getFragmentManager());

        Bundle args = getArguments();
        if (args != null && args.containsKey(ItemDetailFragment.ARG_ITEM_ID)) {
            mProfileId = args.getInt(ItemDetailFragment.ARG_ITEM_ID);
            Log.d(LOG_TAG, "detail mProfileId=" + mProfileId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        View v = inflater.inflate(R.layout.fragment_item_pager, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mPagerAdapter);

        if (mProfileId > 0) {
            mViewPager.setCurrentItem(mProfileId - 1);
            Log.d(LOG_TAG, "pager.setCurrentItem " + (mProfileId - 1));
        }

        return v;
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

    public void setCurrentItem(int id) {
        Log.d(LOG_TAG, "setCurrentItem = " + id);
        this.mProfileId = id;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(id - 1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        Log.d(LOG_TAG, "onPageSelected = " + position);
        mCallbacks.onPageSelected(position + 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
