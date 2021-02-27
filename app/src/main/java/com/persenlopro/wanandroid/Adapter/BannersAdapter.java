package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.persenlopro.wanandroid.BrowserActivity;
import com.persenlopro.wanandroid.Data.Banners;
import com.persenlopro.wanandroid.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class BannersAdapter extends BannerAdapter<Banners,BannersAdapter.BannerViewHolder> {

    private List<Banners> bannersList;
    private Context context;

    public BannersAdapter(List<Banners> bannersList, Context context){
        super(bannersList);
        this.context=context;
        this.bannersList=bannersList;
    }

    public BannerViewHolder onCreateHolder(ViewGroup parent,int ViewType){
        return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_header_banner,parent,false));
    }

    public void onBindView(BannerViewHolder holder,Banners banners,int position,int size){
        Banners banners1=bannersList.get(position);
        Glide.with(holder.itemView)
                .load(banners1.getImagePath())
                .thumbnail(Glide.with(holder.itemView).load(R.drawable.ic_loading))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(holder.imageView);
        holder.textView.setText(banners1.getTitle());
        holder.imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, BrowserActivity.class);
                Bundle b=new Bundle();
                b.putString("url",banners1.getUrl());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        }

        class BannerViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;

            public BannerViewHolder(View view){
                super(view);
                imageView=(ImageView)view.findViewById(R.id.bn_image);
                textView=(TextView)view.findViewById(R.id.bn_title);

        }

        }

}
