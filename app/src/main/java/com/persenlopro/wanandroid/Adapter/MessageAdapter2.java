package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.BrowserActivity;
import com.persenlopro.wanandroid.Data.Messages;
import com.persenlopro.wanandroid.R;

import java.util.ArrayList;
import java.util.List;

//没Banner的Adapter
public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.ViewHolder> {

    private List<Messages> messagesList;
    private Context context;

    public MessageAdapter2(List<Messages> messagesList,Context context){
        this.messagesList=messagesList;
        this.context=context;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        final MessageAdapter2.ViewHolder holder=new MessageAdapter2.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Messages messages = messagesList.get(position);
                Intent intent=new Intent(context, BrowserActivity.class);
                Bundle b=new Bundle();
                b.putString("url",messages.getLink());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Messages messages = messagesList.get(position);
        holder.title.setText(messages.getTitle());
        holder.shareUser.setText(messages.getShareUser());
        holder.niceDate.setText(messages.getNiceDate());

    }

    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView shareUser;
        public TextView niceDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.message_title);
            shareUser=(TextView)itemView.findViewById(R.id.message_shareUser);
            niceDate=(TextView)itemView.findViewById(R.id.message_niceDate);
        }
    }
}
