package mte.crasmonitoring.user_lists;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import mte.crasmonitoring.Auth.APICallbacks;
import mte.crasmonitoring.Auth.APIManager;
import mte.crasmonitoring.Auth.ChooseProfileTypeActivity;
import mte.crasmonitoring.Auth.UserInfo;
import mte.crasmonitoring.R;
import mte.crasmonitoring.user_lists.adapter.ListAdapter;
import mte.crasmonitoring.utils.SharedPrefsUtils;
import okhttp3.ResponseBody;

/**
 * Created by eli on 21/11/2016.
 */

public class ShowSupervisorsFragmemt extends Fragment {
    private RecyclerView recList;
    private List<Superviser> users ;
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

        // *********   need to change it to the real user.getUid(); ***************
        APIManager.getSupervisors(this.getContext(), "1", new APICallbacks<List<Superviser>>() {
            @Override
            public void successfulResponse(List<Superviser> sup) {
                users =  sup;
                ListAdapter ca = new ListAdapter(users ,getContext());
                recList.setAdapter(ca);
            }


            @Override
            public void FailedResponse(String errorMessage) {

            }
        });

        //        users =
//
//        ListAdapter ca = new ListAdapter(users ,getContext());
//        recList.setAdapter(ca);

        return rootView;
    }
}
