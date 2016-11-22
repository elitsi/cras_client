package mte.crasmonitoring.user_lists;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mte.crasmonitoring.Auth.UserInfo;
import mte.crasmonitoring.R;

/**
 * Created by eli on 21/11/2016.
 */

public class ShowSupervisorsFragmemt extends Fragment {
    private RecyclerView recList;
    private ArrayList<UserInfo> users ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.show_supervisors_fragment, container, false);
        ((TextView) rootView.findViewById(R.id.txtSupervisors)).setText("this is supervisors fragment");
        recList = (RecyclerView) rootView.findViewById(R.id.supervisors_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        return rootView;
    }
}
