package com.example.admsgroup7;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.itemHolder> {

    private ArrayList<AnnouncementInfo> AnnouncementList;
    private Context context;

    onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public AnnouncementAdapter(Context context,ArrayList<AnnouncementInfo> AnnouncementList){
        this.context = context;
        this.AnnouncementList = AnnouncementList;
    }

    public interface onItemClickListener{
        void onClick(AnnouncementInfo announcementInfo);

    }

    @NonNull
    @Override
    public AnnouncementAdapter.itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_items,parent,false);

        return new itemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.itemHolder holder, int position) {
        AnnouncementInfo announcementInfo =  AnnouncementList.get(position);
        holder.onBind(announcementInfo);
        holder.itemView.setOnClickListener(v-> onItemClickListener.onClick(AnnouncementList.get(position)));

    }

    @Override
    public int getItemCount() {
        return AnnouncementList.size();
    }


    public static class itemHolder extends RecyclerView.ViewHolder {

        TextView  AnnouncementTitle,Message,Date,time;
        public itemHolder(@NonNull View itemView) {
            super(itemView);
        AnnouncementTitle = itemView.findViewById(R.id.AnnouncementTitle);
        Message = itemView.findViewById(R.id.Message);
        Date = itemView.findViewById(R.id.Date);
        time = itemView.findViewById(R.id.time);


        }
        public void onBind(AnnouncementInfo announcementInfo){

                AnnouncementTitle.setText(announcementInfo.getTitle());
                Message.setText(announcementInfo.getDescription());
                Date.setText(announcementInfo.getDateToday());
                time.setText(announcementInfo.getTime());

        }
    }
}
