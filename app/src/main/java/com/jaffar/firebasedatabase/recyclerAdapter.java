package com.jaffar.firebasedatabase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.viewHolder> {

    private static final String TAG = "Recycler";
    List<Employee> list;
    Context context;

    public recyclerAdapter(Context context, List<Employee> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_face, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Log.d("just check", "name: "+list.get(position).name);
        holder.name.setText(list.get(position).name);
        holder.position.setText(list.get(position).position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name, position;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.storeName1);
            position = itemView.findViewById(R.id.storePosition1);
        }
    }

}