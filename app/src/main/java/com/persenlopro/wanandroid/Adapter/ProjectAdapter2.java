package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.persenlopro.wanandroid.BrowserActivity;
import com.persenlopro.wanandroid.Data.Project;
import com.persenlopro.wanandroid.R;

import java.util.List;

public class ProjectAdapter2 extends RecyclerView.Adapter<ProjectAdapter2.ViewHolder> {

    private List<Project> projectList;
    private Context context;
    private int mPosition;


    public ProjectAdapter2(List<Project> projectList,Context context){
        this.projectList=projectList;
        this.context=context;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProjectAdapter2.ViewHolder viewHolder=new ProjectAdapter2.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                Project project=projectList.get(position);
                String link=project.getLink();
                Intent intent=new Intent(context, BrowserActivity.class);
                Bundle b=new Bundle();
                b.putString("url",link);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project=projectList.get(position);
        mPosition=position;
        Glide.with(holder.itemView)
                .load(project.getImagePath())
                .thumbnail(Glide.with(holder.itemView).load(R.drawable.ic_loading))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(holder.image);
        holder.mTitle.setText(project.getTitle());
        holder.author.setText(project.getAuthor());
        holder.niceData.setText(project.getNiceData());
    }

    public int getItemCount() {
        return projectList.size();
    }

    public int getPosition(){return mPosition;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView mTitle;
        public TextView author;
        public TextView niceData;
        public ImageButton button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.iv_item_project_pic);
            mTitle=(TextView)itemView.findViewById(R.id.tv_item_project_title);
            author=(TextView)itemView.findViewById(R.id.tv_item_project_author);
            niceData=(TextView)itemView.findViewById(R.id.tv_item_project_niceData);
            button=(ImageButton)itemView.findViewById(R.id.bt_item_project_git);
        }
    }
}
