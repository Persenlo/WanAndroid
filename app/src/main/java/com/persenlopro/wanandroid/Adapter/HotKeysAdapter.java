package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Data.HotKeys;
import com.persenlopro.wanandroid.R;

import java.util.List;

public class HotKeysAdapter extends RecyclerView.Adapter<HotKeysAdapter.ViewHolder> {

    private Context context;
    private List<HotKeys> hotKeysList;

    public HotKeysAdapter(List<HotKeys> hotKeysList,Context context){
        this.hotKeysList=hotKeysList;
        this.context=context;
    }


    public HotKeysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder= new HotKeysAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotkeys,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                HotKeys hotKeys=hotKeysList.get(position);
                String key=hotKeys.getName();
                Intent intent=new Intent("com.persenlopro.wanandroid.search"+key);
                context.sendBroadcast(intent);
            }
        });
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull HotKeysAdapter.ViewHolder holder, int position) {
        HotKeys hotKeys=hotKeysList.get(position);
        holder.textView.setText(hotKeys.getName());

    }

    public int getItemCount() {
        return hotKeysList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.item_hot_text);
        }
    }
}
