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
import android.widget.Toast;

import java.util.List;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import mte.crasmonitoring.model.RemoteUser;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.R;
import mte.crasmonitoring.ui.adapters.RemoteUsersAdapter;
import mte.crasmonitoring.utils.SharedPrefsUtils;

/**
 * Created by eli on 21/11/2016.
 */

public class ShowSupervisesFragment extends RemoteUsersListFragmentBase {

    public interface SupervisesFragmentListener {
        void onSupervisedClick(RemoteUser supervisor);
    }


    @Override
    protected void gotUsers(List<RemoteUser> remoteUsers) {
        super.gotUsers(remoteUsers);
        remoteUsersAdapter.setSupervisesFragmentListener(new SupervisesFragmentListener() {
            @Override
            public void onSupervisedClick(RemoteUser supervised) {
                //Toast.makeText(getContext(),"Clicked " + supervisor.getName(),Toast.LENGTH_LONG).show();
                APIManager.sendMonitorRequest(getContext(), supervised.getID(), new APICallbacks<String>() {
                    @Override
                    public void successfulResponse(String s) {

                    }

                    @Override
                    public void FailedResponse(String errorMessage) {
                        Toast.makeText(getContext(),"Failuer! - " + errorMessage,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void getRemoteUsers() {
        super.getRemoteUsers();
        APIManager.getSupervises(this.getContext(), SharedPrefsUtils.getUserId(getContext()), new APICallbacks<List<RemoteUser>>() {
            @Override
            public void successfulResponse(List<RemoteUser> remoteUsers) {
                gotUsers(remoteUsers);
            }

            @Override
            public void FailedResponse(String errorMessage) {

            }
        });

    }

    @Override
    protected int getType() {
        return TYPE_SUPERVISES;
    }
}
