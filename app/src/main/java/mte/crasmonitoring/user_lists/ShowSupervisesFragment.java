package mte.crasmonitoring.user_lists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import mte.crasmonitoring.Auth.APICallbacks;
import mte.crasmonitoring.Auth.APIManager;
import mte.crasmonitoring.Auth.UserInfo;
import mte.crasmonitoring.R;
import mte.crasmonitoring.user_lists.adapter.ListAdapterSupervises;
import mte.crasmonitoring.user_lists.adapter.ListAdapterSupervisors;


public class ShowSupervisesFragment extends Fragment {
    private RecyclerView recList;
    private List<Supervised> superviseds ;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.show_supervises_fragment, container, false);


        recList = (RecyclerView) rootView.findViewById(R.id.supervises_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        APIManager.getSupervises(this.getContext(), user.getUid(), new APICallbacks<List<Supervised>>() {
            @Override
            public void successfulResponse(List<Supervised> sup) {
                superviseds =  sup;
                ListAdapterSupervises ca = new ListAdapterSupervises(superviseds ,getContext());
                recList.setAdapter(ca);
            }


            @Override
            public void FailedResponse(String errorMessage) {
                String f = "ff";
            }
        });


        return rootView;
    }
}
