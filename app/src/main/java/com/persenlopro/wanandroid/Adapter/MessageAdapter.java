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

import com.persenlopro.wanandroid.BrowserActivity;
import com.persenlopro.wanandroid.Data.Banners;
import com.persenlopro.wanandroid.Data.Messages;
import com.persenlopro.wanandroid.R;
import com.youth.banner.Banner;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private List<Messages> mMessagesList;
    private Context context;
    private View mHeaderView;
    private boolean isHeader;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;



    public MessageAdapter(List<Messages> messagesList, Context context){
        mMessagesList = messagesList;
        this.context = context;
    }


    //根据position返回不同的ItemViewType
    @Override
    public int getItemViewType(int position) {
        if (position==0){isHeader=true;     return TYPE_HEADER;}
        else if(position==getItemCount()-1){isHeader=false;     return TYPE_FOOTER;}
        else {isHeader=false;     return TYPE_NORMAL;}
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //根据viewType来显示header
        if (viewType==TYPE_HEADER){
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_header,parent,false)); }
        //显示footer
        if(viewType==TYPE_FOOTER){
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_main,parent,false));
        }

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        final  ViewHolder holder=new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Messages messages = mMessagesList.get(position);
                Intent intent=new Intent(context, BrowserActivity.class);
                Bundle b=new Bundle();
                b.putString("url",messages.getLink());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position==getItemCount()-1) return;
        if(isHeader==true){
            Banner banner=((ViewHolder) holder).banner;
            banner.setAdapter(new BannersAdapter(Banners.getList(),context));
        }
        else{
            Messages messages = mMessagesList.get(getRealPosition(holder));
            holder.title.setText(messages.getTitle());
            holder.shareUser.setText(messages.getShareUser());
            holder.niceDate.setText(messages.getNiceDate());}

    }



    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView shareUser;
        TextView niceDate;
        public Banner banner;

        public ViewHolder(View view){
            super(view);
            if(isHeader==true){
                banner = itemView.findViewById(R.id.banner);}
            title =(TextView) view.findViewById(R.id.message_title);
            shareUser =(TextView) view.findViewById(R.id.message_shareUser);
            niceDate =(TextView) view.findViewById(R.id.message_niceDate);

        }

    }

    class BannersViewHolder extends RecyclerView.ViewHolder{
        public Banner banner;

        public BannersViewHolder(View itemView){
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
        }
    }


    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mMessagesList.size() : mMessagesList.size() + 1;
    }



}
