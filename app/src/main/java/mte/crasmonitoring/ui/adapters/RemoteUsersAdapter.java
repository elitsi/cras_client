package mte.crasmonitoring.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.danlew.android.joda.DateUtils;

import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.List;

import mte.crasmonitoring.R;
import mte.crasmonitoring.eventbus.Events;
import mte.crasmonitoring.model.RemoteUser;
import mte.crasmonitoring.ui.fragments.RemoteUsersListFragmentBase;
import mte.crasmonitoring.ui.fragments.ShowSupervisesFragment;
import mte.crasmonitoring.utils.GeneralUtils;


public class RemoteUsersAdapter extends RecyclerView.Adapter<RemoteUsersAdapter.RemoteUserHolder> {
    private static final String TAG = RemoteUsersAdapter.class.getSimpleName();
    protected List<RemoteUser> supList;
    private final Activity activity;
    private int type;
    private ShowSupervisesFragment.SupervisesFragmentListener supervisorsFragmentListener;
    public RemoteUsersAdapter(List<RemoteUser> supList, Activity activity, int type) {
        this.supList = supList;
        this.activity = activity;
        this.type = type;
    }

    public void setSupervisesFragmentListener(ShowSupervisesFragment.SupervisesFragmentListener supervisorsFragmentListener) {
        this.supervisorsFragmentListener = supervisorsFragmentListener;
    }

    @Override
    public int getItemCount() {
        return supList.size();
    }

    @Override
    public void onBindViewHolder(RemoteUsersAdapter.RemoteUserHolder remoteUserHolder, int i) {
        final RemoteUser remoteUser = supList.get(i);
        remoteUserHolder.tvName.setText(remoteUser.getName());

        if(!TextUtils.isEmpty(remoteUser.getPicture()))
        {
            Glide
                    .with(activity)
                    .load(remoteUser.getPicture())
                    .placeholder(R.drawable.user_placeholder)
                    .dontAnimate()
                    .into(remoteUserHolder.civPicture);
        }

        if(type == RemoteUsersListFragmentBase.TYPE_SUPERVISORS)
        {
            setupSupervisor(remoteUserHolder, remoteUser);
        }
        else
        {
            setupSuperviser(remoteUserHolder, remoteUser);
        }
    }

    private void setupSupervisor(RemoteUsersAdapter.RemoteUserHolder remoteUserHolder, final RemoteUser supervisor)
    {
        remoteUserHolder.btnMonitor.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) remoteUserHolder.tvName.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        remoteUserHolder.tvName.setLayoutParams(params);
    }

    private void setupSuperviser(RemoteUsersAdapter.RemoteUserHolder remoteUserHolder, final RemoteUser supervised)
    {
        remoteUserHolder.btnMonitor.setVisibility(View.VISIBLE);
        remoteUserHolder.btnMonitor.setText(supervised.getStatus() ? "Stop monitoring" : "Monitor");
        if(supervisorsFragmentListener != null)
        {
            remoteUserHolder.btnMonitor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    supervisorsFragmentListener.onSupervisedClick(supervised);
                }
            });
        }
    }

    @Override
    public RemoteUsersAdapter.RemoteUserHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new RemoteUsersAdapter.RemoteUserHolder(itemView);
    }

    public static class RemoteUserHolder extends RecyclerView.ViewHolder {
        protected TextView tvName;
        protected ImageView civPicture;
        protected Button btnMonitor;


        public RemoteUserHolder(View v ) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
            civPicture = (ImageView) v.findViewById(R.id.civ_picture);
            btnMonitor = (Button) v.findViewById(R.id.btn_start_monitor);
        }

    }



    public void setList(List<RemoteUser> supList)
    {
//        if(GeneralUtils.listEqualsNoOrder(supList,this.supList))
//        {
//            return;
//        }
        this.supList = supList;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }
}
