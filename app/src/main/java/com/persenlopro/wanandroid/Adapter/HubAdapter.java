package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Data.Hubs;
import com.persenlopro.wanandroid.Data.Websites;
import com.persenlopro.wanandroid.R;

import org.w3c.dom.Text;

import java.util.List;

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.ViewHolder> {

    private List<Hubs> hubsList;
    private Context context;
    private String firstName;

    public HubAdapter(List<Hubs> hubsList, Context context){
        this.hubsList=hubsList;
        this.context=context;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HubAdapter.ViewHolder viewHolder = new HubAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                int position=viewHolder.getAdapterPosition();
                Hubs hubs=hubsList.get(position);
                firstName=hubs.getFirstName();
                Intent intent=new Intent("com.persenlopro.wanandroid"+firstName);
                context.sendBroadcast(intent);


            }
        });
        return viewHolder;

    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hubs hubs=hubsList.get(position);
        holder.textView.setText(hubs.getFirstName());

    }

    public int getItemCount() {
        return hubsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.common_textview);
        }
    }
}
