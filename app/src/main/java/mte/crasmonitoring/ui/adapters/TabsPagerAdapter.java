package mte.crasmonitoring.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import mte.crasmonitoring.ui.fragments.ShowSupervisorsFragment;
import mte.crasmonitoring.ui.fragments.ShowSupervisesFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private static final int SUPERVISORS_FRAGMENTS_POSITION = 1;
    private static final int SUPERVISES_FRAGMENTS_POSITION = 0;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case SUPERVISES_FRAGMENTS_POSITION:
                return new ShowSupervisesFragment();
            case SUPERVISORS_FRAGMENTS_POSITION:
                return new ShowSupervisorsFragment();
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
            case SUPERVISES_FRAGMENTS_POSITION:
                return "I'm Monitoring";
            case SUPERVISORS_FRAGMENTS_POSITION:
                return "Monitoring Me";
        }
        return null;
    }
}