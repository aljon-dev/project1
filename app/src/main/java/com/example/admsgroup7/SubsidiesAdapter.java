package com.example.admsgroup7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubsidiesAdapter extends RecyclerView.Adapter<SubsidiesAdapter.ItemHolder> {

    private ArrayList<getSubsidies> SubsidiesList;
    private Context context;

    onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public SubsidiesAdapter(Context context,ArrayList<getSubsidies> SubsidiesList){
        this.SubsidiesList = SubsidiesList;
        this.context = context;
    }
    public interface onItemClickListener{
        void onClick(getSubsidies GetSubsidies);
    }

    @NonNull
    @Override
    public SubsidiesAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subsidies_list,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubsidiesAdapter.ItemHolder holder, int position) {
        getSubsidies GetSubsidies = SubsidiesList.get(position);
        holder.onBind(GetSubsidies);
        holder.itemView.setOnClickListener(v-> onItemClickListener.onClick(SubsidiesList.get(position)));

    }

    @Override
    public int getItemCount() {
        return SubsidiesList.size();
    }
    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView FullName,InstanceNumber,Received,Received_Date,SponsoredBy;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            FullName = itemView.findViewById(R.id.FullName);
            InstanceNumber = itemView.findViewById(R.id.InstanceNumber);
            Received = itemView.findViewById(R.id.Received);
            Received_Date = itemView.findViewById(R.id.Received_Date);
            SponsoredBy = itemView.findViewById(R.id.SponsoredBy);

        }
        public void onBind (getSubsidies GetSubsidies){

            FullName.setText(GetSubsidies.getFullName());
            InstanceNumber.setText(GetSubsidies.getInstanceNumber());
            Received.setText(GetSubsidies.getReceived());
            Received_Date.setText(GetSubsidies.getReceived_Date());
            SponsoredBy.setText(GetSubsidies.getSponsoredBy());

        }
    }
}
