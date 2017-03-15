package mte.crasmonitoring.ui.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.danlew.android.joda.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.List;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import mte.crasmonitoring.eventbus.Events;
import mte.crasmonitoring.model.RemoteUser;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.R;
import mte.crasmonitoring.ui.adapters.RemoteUsersAdapter;
import mte.crasmonitoring.utils.SharedPrefsUtils;

/**
 * Created by Mickael on 21/11/2016.
 */

public abstract class RemoteUsersListFragmentBase extends Fragment {
    public static final int TYPE_SUPERVISORS = 0;
    public static final int TYPE_SUPERVISES = 1;
    protected View rootView;
    private RecyclerView recList;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected RemoteUsersAdapter remoteUsersAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.show_supervisors_fragment, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        this.rootView = rootView;

        recList = (RecyclerView) rootView.findViewById(R.id.remote_users_list);
        recList.setHasFixedSize(true);
        recList.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRemoteUsers();
            }
        });



    }

    @Subscribe
    public void onRefreshUsersRequestEvent(Events.RefreshRemoteUsersEvent refreshRemoteUsersEvent)
    {
        getRemoteUsers();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRemoteUsers();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getRemoteUsers()
    {
        swipeRefreshLayout.setRefreshing(true);
    }

    protected abstract int getType();

    protected void gotUsers(List<RemoteUser> remoteUsers)
    {
        if(remoteUsersAdapter == null)
        {
            remoteUsersAdapter = new RemoteUsersAdapter(remoteUsers,getActivity(), getType());
            AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(remoteUsersAdapter, recList);
            recList.setAdapter(animatorAdapter);
        }
        else
            remoteUsersAdapter.setList(remoteUsers);
        if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onRefreshRemoteUsersEvent(Events.RefreshRemoteUsersEvent refreshRemoteUsersEvent)
    {
        getRemoteUsers();
    }

}
