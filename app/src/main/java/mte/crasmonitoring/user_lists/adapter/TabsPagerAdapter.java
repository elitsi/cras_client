package mte.crasmonitoring.user_lists.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import mte.crasmonitoring.user_lists.ShowSupervisesFragment;
import mte.crasmonitoring.user_lists.ShowSupervisorsFragmemt;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        //return PageFragment.newInstance(position + 1);
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new ShowSupervisesFragment();
            case 1:
                // Games fragment activity
                return new ShowSupervisorsFragmemt();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "תחת הניטור שלי";
            case 1:
                // Games fragment activity
                return "מנטרים אותי";
        }
        return null;
    }
}