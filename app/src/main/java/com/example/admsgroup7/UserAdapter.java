package com.example.admsgroup7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemHolder> {

    private ArrayList<Users> UserList;
    private Context context;

    onItemClickListener onItemClickListener;
    public UserAdapter(Context context,ArrayList<Users> UserList){
            this.UserList = UserList;
            this.context = context;
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onClick(Users users);
    }

    @NonNull
    @Override
    public UserAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userslist,parent,false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ItemHolder holder, int position) {
        Users user = UserList.get(position);
        holder.onBind(user);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onClick(UserList.get(position)));

    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }
    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView FullName,PhoneNumber,email;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            FullName = itemView.findViewById(R.id.FullName);
            PhoneNumber= itemView.findViewById(R.id.PhoneNumber);
            email = itemView.findViewById(R.id.email);
        }
        public void onBind(Users users){
            FullName.setText(users.getFullName());
            PhoneNumber.setText(users.getPhoneNumber());
            email.setText(users.getUserEmail());
        }
    }
}
