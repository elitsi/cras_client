package mte.crasmonitoring.user_lists;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mte.crasmonitoring.R;
import mte.crasmonitoring.user_lists.adapter.TabsPagerAdapter;

/**
 * Created by eli on 21/11/2016.
 */

public class ShowUserListsActivity extends AppCompatActivity  {
    TabsPagerAdapter pagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_user_lists_activity);
       // Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

//        Fragment fragment = new ShowSupervisorsFragmemt();
//        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();


    }

}
