package mte.crasmonitoring.ui.fragments;

import java.util.List;

import mte.crasmonitoring.model.RemoteUser;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.utils.SharedPrefsUtils;


public class ShowSupervisorsFragment extends RemoteUsersListFragmentBase {

    @Override
    public void getRemoteUsers() {
        super.getRemoteUsers();
        APIManager.getSupervisors(this.getContext(), SharedPrefsUtils.getUserId(getContext()), new APICallbacks<List<RemoteUser>>() {
            @Override
            public void successfulResponse(List<RemoteUser> sup) {
                gotUsers(sup);
            }
            @Override
            public void failedResponse(String errorMessage) {
            }
        });
    }

    @Override
    protected int getType() {
        return TYPE_SUPERVISORS;
    }
}
