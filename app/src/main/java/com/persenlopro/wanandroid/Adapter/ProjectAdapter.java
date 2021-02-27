package com.persenlopro.wanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Data.Project;
import com.persenlopro.wanandroid.R;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private List<Project> projectList;
    private Context context;
    private String id;

    public ProjectAdapter(List<Project> projectList,Context context){
        this.projectList=projectList;
        this.context=context;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProjectAdapter.ViewHolder viewHolder= new ProjectAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotkeys,parent,false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                Project project=projectList.get(position);
                id=project.getId();
                Intent intent=new Intent("com.persenlopro.wanandroid"+id);
                Intent intent1=new Intent("com.persenlopro.wanandroid.project.recyclerclose");
                context.sendBroadcast(intent);
                context.sendBroadcast(intent1);
            }
        });

        return viewHolder;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project=projectList.get(position);
        holder.textView.setText(project.getName());

    }

    public int getItemCount() {
        return projectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.item_hot_text);
        }
    }
}
