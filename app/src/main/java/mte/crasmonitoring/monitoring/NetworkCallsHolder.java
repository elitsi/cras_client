package mte.crasmonitoring.monitoring;

import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import java.util.ArrayList;
import java.util.List;

import mte.crasmonitoring.model.SendViolationToApi;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Mickael on 2/26/2017.
 */

public class NetworkCallsHolder {
    public static int VIOLATION_UNAPPROVED_APP = 1;
    public static int VIOLATION_DRIVING_LIMIT = 2;
    private SendViolationToApi sendViolationToApi;
    private List<Integer> pendingRequests;
    private Context context;
    private Subscription networkConnectivitySubscription;
    public NetworkCallsHolder(Context context, SendViolationToApi sendViolationToApi) {
        this.context = context;
        this.sendViolationToApi = sendViolationToApi;
        pendingRequests = new ArrayList<>();
    }

    public void startMonitoring()
    {
        networkConnectivitySubscription =
        ReactiveNetwork.observeNetworkConnectivity(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Connectivity>() {
                    @Override public void call(Connectivity connectivity) {
                        if(connectivity.isAvailable())
                        {
                            List<Integer> clonedList = new ArrayList<>(pendingRequests);
                            pendingRequests.clear();
                            for(Integer requestType:clonedList)
                            {
                                if(requestType == VIOLATION_UNAPPROVED_APP)
                                    sendViolationToApi.sendAppViolationEvent();
                                else
                                    sendViolationToApi.sendDrivingViolationEvent();
                            }
                        }
                    }
                });
    }

    public void addRequest(int requestType)
    {
        pendingRequests.add(requestType);
    }

    public void stopMonitoring()
    {
        if (networkConnectivitySubscription != null && !networkConnectivitySubscription.isUnsubscribed()) {
            networkConnectivitySubscription.unsubscribe();
        }
    }

}
