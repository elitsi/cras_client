package mte.crasmonitoring.user_lists;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import mte.crasmonitoring.Auth.APICallbacks;
import mte.crasmonitoring.Auth.APIManager;
import mte.crasmonitoring.R;
import mte.crasmonitoring.user_lists.adapter.ListAdapterSupervisors;

/**
 * Created by eli on 21/11/2016.
 */

public class ShowSupervisorsFragmemt extends Fragment {
    private RecyclerView recList;
    private List<Supervisor> supervisors ;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.show_supervisors_fragment, container, false);

        recList = (RecyclerView) rootView.findViewById(R.id.supervisors_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        APIManager.getSupervisors(this.getContext(), user.getUid(), new APICallbacks<List<Supervisor>>() {
            @Override
            public void successfulResponse(List<Supervisor> sup) {
                supervisors =  sup;
                ListAdapterSupervisors ca = new ListAdapterSupervisors(supervisors ,getContext());
                recList.setAdapter(ca);
            }


            @Override
            public void FailedResponse(String errorMessage) {

            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
