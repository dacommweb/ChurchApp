package com.suntechnology.churchapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.suntechnology.churchapp.R;
import com.suntechnology.churchapp.classes.Events;
import com.suntechnology.churchapp.helper.CodingMsg;
import com.suntechnology.churchapp.helper.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by FRED on 6/1/2017.
 */

public class EventRVAdapter extends RecyclerView.Adapter<EventRVAdapter.ViewHolder> {
    ArrayList<Events> eventList;
    Context context;
    EventComm eventComm;

    public EventRVAdapter(Context context, ArrayList<Events> eventList) {
        this.context = context;
        this.eventList = eventList;
        eventComm= (EventComm) context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public EventRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventRVAdapter.ViewHolder holder, final int position) {

        holder.txtEventitle.setText(eventList.get(position).getEventTitle());
        holder.txtEventDate.setText(eventList.get(position).getEventDateFormated());
        holder.txtEvenDesc.setText(eventList.get(position).getEventDesc());
        holder.txtEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventComm.eventMessage("edit",eventList.get(position),position);
            }
        });
   holder.txtDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventComm.eventMessage("delete",eventList.get(position),position);
                eventList.remove(position);
                notifyItemRemoved(position);
            }
        });

    }
    public interface EventComm{
        void eventMessage(String action,Events event,int position);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEventitle, txtEventDate, txtEvenDesc,txtEditEvent,txtDeleteEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            txtEventitle = (TextView) itemView.findViewById(R.id.txtEventitle);
            txtEventDate = (TextView) itemView.findViewById(R.id.txtEventDate);
            txtEvenDesc = (TextView) itemView.findViewById(R.id.txtEvenDesc);
            txtEditEvent = (TextView) itemView.findViewById(R.id.txtEditEvent);
            txtDeleteEvent = (TextView) itemView.findViewById(R.id.txtDeleteEvent);
        }
    }
}

