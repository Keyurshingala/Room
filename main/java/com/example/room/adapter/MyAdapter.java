package com.example.room.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.room.databinding.RecycleItemBinding;
import com.example.room.roomDB.User;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    List<User> userList;
    onClickListener onClickListener;

    public MyAdapter(List<User> userList, Context context, onClickListener onClickListener) {
        this.context = context;
        this.userList = userList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecycleItemBinding binding = RecycleItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = userList.get(position);

        holder.binding.tvId.setText(user.toString());
        holder.binding.tvName.setText(user.getName());
        holder.binding.tvContact.setText(user.getContact());
        holder.binding.tvEmail.setText(user.getEmail());

        holder.binding.card.setOnClickListener(v -> {
            onClickListener.onCardClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RecycleItemBinding binding;

        public MyViewHolder(@NonNull RecycleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface onClickListener {
        void onCardClick(int pos);
    }
}
