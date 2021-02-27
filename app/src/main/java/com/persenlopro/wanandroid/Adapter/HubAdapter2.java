package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.ArticleViewAbility;
import com.persenlopro.wanandroid.Data.Hubs;
import com.persenlopro.wanandroid.R;

import java.util.List;

public class HubAdapter2 extends RecyclerView.Adapter<HubAdapter2.ViewHolder> {

    private List<Hubs> hubsList;
    private Context context;

    private String firstName;

    public HubAdapter2(List<Hubs> hubsList,Context context,String firstName){
        this.hubsList=hubsList;
        this.context=context;
        this.firstName=firstName;
    }


    @NonNull
    public HubAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HubAdapter2.ViewHolder viewHolder = new HubAdapter2.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common2,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                int position=viewHolder.getAdapterPosition();
                Hubs hubs=hubsList.get(position);
                int realId=hubs.getRealId();
                Intent intent=new Intent(context, ArticleViewAbility.class);
                Bundle bundle=new Bundle();
                bundle.putInt("id",realId);
                bundle.putString("author","null");
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull HubAdapter2.ViewHolder holder, int position) {
        Hubs hubs=hubsList.get(position);
        holder.textView.setText(hubs.getSecondName());
    }

    public int getItemCount() {
        return hubsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.common2_textview);
        }
    }
}
