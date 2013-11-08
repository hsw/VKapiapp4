package hsw.vkapiapp4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hsw.vkapiapp4.providers.VkProfilesPagerAdapter;

public class ItemPagerFragment extends Fragment {

    final String LOG_TAG = "VkLoader pager";

    private PagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pagerAdapter = new VkProfilesPagerAdapter(getFragmentManager());

        if (getArguments().containsKey(ItemDetailFragment.ARG_ITEM_ID)) {
            id = getArguments().getInt(ItemDetailFragment.ARG_ITEM_ID);
            Log.d(LOG_TAG, "detail id=" + id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_pager, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        if (savedInstanceState == null) {
            mViewPager.setCurrentItem(id - 1);
            Log.d(LOG_TAG, "pager.setCurrentItem " + (id - 1));
        } else {
            Log.d(LOG_TAG, "pager savedInstanceState != null");
        }

        return v;
    }
}
