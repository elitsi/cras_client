package mte.crasmonitoring.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mte.crasmonitoring.R;
import mte.crasmonitoring.ui.adapters.ExpandableListAdapter;


/**
 * Created by eli on 04/05/2017.
 */

public class SessionsDialogFragment extends DialogFragment {

    private RecyclerView recyclerview;
    private ExpandableListAdapter adapter;
    private ArrayList sessions;

    static SessionsDialogFragment newInstance(ArrayList sessionsList) {

        SessionsDialogFragment f = new SessionsDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("sessions", sessionsList);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessions = getArguments().getStringArrayList("sessions");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sessions, container, false);
        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        adapter = new ExpandableListAdapter(sessions);
        recyclerview.setAdapter(adapter);
        //v.setBackgroundResource(android.R.drawable.roun);
        v.setBackgroundResource(R.drawable.layout_bg);
        //getDialog().getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        return dialog;
    }
}
