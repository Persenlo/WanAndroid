package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Data.Websites;
import com.persenlopro.wanandroid.Fragment.RightFragment;
import com.persenlopro.wanandroid.R;
import com.persenlopro.wanandroid.WebsiteActivity;

import java.util.List;

public class WebsiteAdapter extends RecyclerView.Adapter<WebsiteAdapter.ViewHolder> {

    private List<Websites> websitesList;
    private Context context;
    public String category="all";

    WebsiteActivity websiteActivity;



    public WebsiteAdapter(List<Websites> websitesList, Context context){
        this.websitesList=websitesList;
        this.context=context;
    }


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                int position=viewHolder.getAdapterPosition();
                Websites websites=websitesList.get(position);
                category=websites.getCategory();
                Intent intent=new Intent("com.persenlopro.wanandroid"+category);
                context.sendBroadcast(intent);


        }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Websites websites=websitesList.get(position);
        if(position==0) {holder.textView.setText("→  "+websites.getCategory());}
        if(position!=0) {
            Websites before=websitesList.get(position-1);
            if(before.getCategory().equals(websites.getCategory())){
                holder.textView.setVisibility(View.GONE);
            }
            else {
                holder.textView.setText("→  "+websites.getCategory());
            }
        }




    }

    @Override
    public int getItemCount() {
        return websitesList.size();
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
