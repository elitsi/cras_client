package mte.crasmonitoring.user_lists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mte.crasmonitoring.R;
import mte.crasmonitoring.user_lists.Supervised;

/**
 * Created by eli on 23/11/2016.
 */

public class ListAdapterSupervises extends RecyclerView.Adapter<ListAdapterSupervises.ContactViewHolder> {

    protected List<Supervised> supList;
    final Context context;
    protected String user_fb_id;


    public ListAdapterSupervises(List<Supervised> supList, Context context) {
        this.supList = supList;
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return supList.size();
    }

    @Override
    public void onBindViewHolder(ListAdapterSupervises.ContactViewHolder contactViewHolder, int i) {
        Supervised ci = supList.get(i);
        contactViewHolder.vName.setText(ci.getName());
        contactViewHolder.vMail.setText(ci.getMail());
        contactViewHolder.vId.setText(ci.get_id());

    }

    @Override
    public ListAdapterSupervises.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_supervised, viewGroup, false);

        return new ListAdapterSupervises.ContactViewHolder(itemView ,supList, context );
    }



    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vMail;
        protected TextView vId;


        public ContactViewHolder(View v , final List<Supervised> supervisedList , final Context context ) {
            super(v);
            vName = (TextView) v.findViewById(R.id.nameLabel);
            vMail = (TextView) v.findViewById(R.id.mailLabel);
            vId = (TextView) v.findViewById(R.id.idLabel);


        }

    }


}
