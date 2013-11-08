package hsw.vkapiapp4.providers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hsw.vkapiapp4.ItemDetailFragment;

public class VkProfilesPagerAdapter extends FragmentStatePagerAdapter {

    public VkProfilesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return ItemDetailFragment.newInstance(position + 1);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 1000;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Profile " + (position + 1);
    }
}
