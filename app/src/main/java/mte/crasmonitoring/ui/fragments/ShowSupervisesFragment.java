package mte.crasmonitoring.ui.fragments;

import android.app.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import mte.crasmonitoring.model.RemoteUser;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.R;
import mte.crasmonitoring.ui.adapters.ExpandableListAdapter;
import mte.crasmonitoring.ui.adapters.RemoteUsersAdapter;
import mte.crasmonitoring.utils.SharedPrefsUtils;

/**
 * Created by eli on 21/11/2016.
 */

public class ShowSupervisesFragment extends RemoteUsersListFragmentBase {

    public interface SupervisesFragmentListener {
        void onSupervisedClick(RemoteUser supervisor);
        void onSupervisedLogClick(RemoteUser supervisor);
    }


    @Override
    protected void gotUsers(List<RemoteUser> remoteUsers) {
        super.gotUsers(remoteUsers);
        remoteUsersAdapter.setSupervisesFragmentListener(new SupervisesFragmentListener() {
            @Override
            public void onSupervisedClick(RemoteUser supervised) {

                if(supervised.getStatus())
                {
                    APIManager.stopMonitorBySupervisor(getContext(), supervised.getID(), new APICallbacks<String>() {
                        @Override
                        public void successfulResponse(String s) {
                            getRemoteUsers();
                        }

                        @Override
                        public void failedResponse(String errorMessage) {

                        }
                    });
                }
                else
                {
                    APIManager.sendMonitorRequest(getContext(), supervised.getID(), new APICallbacks<String>() {
                        @Override
                        public void successfulResponse(String s) {
                            Toast.makeText(getContext(), "Monitor request sent!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failedResponse(String errorMessage) {
                            Toast.makeText(getContext(),"Failure! - " + errorMessage,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onSupervisedLogClick(RemoteUser supervised) {
                APIManager.getUserLogSessions(getContext(),supervised.getID(), new APICallbacks<String>() {
                    @Override
                    public void successfulResponse(String s) {

                        try {

                            int counter = 1;    // holds the sessions number
                            JSONArray jsonArray = new JSONArray(s); // will hold the whole json object from server
                            JSONObject session ;                    // will hold each session in the array
                            int sessionEventsLength;
                            ArrayList<ExpandableListAdapter.Item> data = new ArrayList<>();         // this is the final list which will be sent to session dialogFragment
                            String fullDate,startDateStr_first, startDateStr_sec,startDateStr,endDateStr_first,endDateStr_sec,endDateStr;
                            StringTokenizer tokens;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                session = jsonArray.getJSONObject(i);

                                if(session.getJSONArray("events") != null){
                                    sessionEventsLength = session.getJSONArray("events").length();

                                    if(sessionEventsLength > 0 ){

                                        // getting dates for session

                                        tokens = new StringTokenizer(session.getString("start_time"), " ");
                                        startDateStr_first = tokens.nextToken();
                                        startDateStr_sec = tokens.nextToken();
                                        tokens = new StringTokenizer(session.getString("end_time"), " ");
                                        endDateStr_first = tokens.nextToken();
                                        endDateStr_sec = tokens.nextToken();
                                        String[] separated_start = startDateStr_sec.split(":");
                                        String[] separated_end = endDateStr_sec.split(":");

                                        fullDate = startDateStr_first + " " + separated_start[0]+ ":" +separated_start[1] + "  to:  " + endDateStr_first + " " + separated_end[0]+ ":" +separated_end[1];
                                        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, fullDate));
                                        counter++;
                                    }
                                    for(int j = 0 ; j < sessionEventsLength ; j++){
                                        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, session.getJSONArray("events").getString(j)));
                                    }
                                }
                            }

                            createDialog(data);
                            Log.v("events " , "opening sessions dialog");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failedResponse(String errorMessage) {

                    }
                });
            }
        });
    }

    public void createDialog(ArrayList list){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = SessionsDialogFragment.newInstance(list);
        newFragment.show(ft, "dialog");
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
            public void failedResponse(String errorMessage) {

            }
        });

    }

    @Override
    protected int getType() {
        return TYPE_SUPERVISES;
    }
}
