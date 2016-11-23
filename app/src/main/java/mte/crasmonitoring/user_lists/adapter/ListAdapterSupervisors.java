package mte.crasmonitoring.user_lists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mte.crasmonitoring.R;
import mte.crasmonitoring.user_lists.Supervisor;


public class ListAdapterSupervisors extends RecyclerView.Adapter<ListAdapterSupervisors.ContactViewHolder> {

    protected List<Supervisor> supList;
    final Context context;
    protected String user_fb_id;


    public ListAdapterSupervisors(List<Supervisor> supList, Context context) {
        this.supList = supList;
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return supList.size();
    }

    @Override
    public void onBindViewHolder(ListAdapterSupervisors.ContactViewHolder contactViewHolder, int i) {
        Supervisor ci = supList.get(i);
        contactViewHolder.vName.setText(ci.getName());
        contactViewHolder.vMail.setText(ci.getMail());

    }

    @Override
    public ListAdapterSupervisors.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ListAdapterSupervisors.ContactViewHolder(itemView ,supList, context );
    }



    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vMail;


        public ContactViewHolder(View v , final List<Supervisor> supervisorList , final Context context ) {
            super(v);
            vName = (TextView) v.findViewById(R.id.nameLabel);
            vMail = (TextView) v.findViewById(R.id.mailLabel);


        }

    }


}
