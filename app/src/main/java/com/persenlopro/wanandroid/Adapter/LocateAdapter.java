package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Data.Locate;
import com.persenlopro.wanandroid.Data.Websites;
import com.persenlopro.wanandroid.R;

import java.util.List;

public class LocateAdapter extends RecyclerView.Adapter<LocateAdapter.ViewHolder> {

    private List<Locate> locateList;
    private Context context;
    private String name="all";

    public LocateAdapter(List<Locate> locateList, Context context){
        this.locateList=locateList;
        this.context=context;
    }



    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LocateAdapter.ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                int position=viewHolder.getAdapterPosition();
                Locate locate=locateList.get(position);
                name=locate.getChapterName();
                Intent intent=new Intent("com.persenlopro.wanandroid"+name);
                context.sendBroadcast(intent);


            }
        });
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Locate locate=locateList.get(position);
        holder.textView.setText(locate.getChapterName());

    }

    public int getItemCount() {
        return locateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.common_linerlayout);
            textView=(TextView)itemView.findViewById(R.id.common_textview);
        }
    }
}
