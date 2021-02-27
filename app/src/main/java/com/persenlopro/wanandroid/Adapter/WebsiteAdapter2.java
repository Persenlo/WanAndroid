package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.BrowserActivity;
import com.persenlopro.wanandroid.Data.Websites;
import com.persenlopro.wanandroid.R;

import java.util.ArrayList;
import java.util.List;

public class WebsiteAdapter2 extends RecyclerView.Adapter<WebsiteAdapter2.ViewHolder> {


    private List<Websites> websitesList=new ArrayList<>();
    private Context context;
    private WebsiteAdapter websiteAdapter;
    private String category;



    public WebsiteAdapter2(List<Websites> websitesList, Context context,String category){
        this.websitesList=websitesList;
        this.context=context;
        this.category=category;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WebsiteAdapter2.ViewHolder viewHolder = new WebsiteAdapter2.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common2,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                int position=viewHolder.getAdapterPosition();
                Websites websites=websitesList.get(position);
                Intent intent=new Intent(context, BrowserActivity.class);
                Bundle b=new Bundle();
                b.putString("url",websites.getLink());
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Websites websites=websitesList.get(position);
        if(category.equals("all"))
        holder.textView.setText(websites.getName());
        else if (websites.getTrueName(category).equals("null"))
        {
            holder.textView.setVisibility(View.GONE);
        }
        else holder.textView.setText(websites.getName());
    }

    @Override
    public int getItemCount() {
        return websitesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout= itemView.findViewById(R.id.common2_linerlayout);
            textView=(TextView) itemView.findViewById(R.id.common2_textview);
        }
    }
}
