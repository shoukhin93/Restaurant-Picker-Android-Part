package com.example.restaurantpicker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.restaurantpicker.Constants;
import com.example.restaurantpicker.Models.Item;
import com.example.restaurantpicker.R;

import java.util.List;

/**
 * Created by Shoukhin on 6/1/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_view, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {

        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText(itemList.get(position).getPrice());

        String imageURL = Constants.ITEM_IMAGE_URL + itemList.get(position).getImageURL();
        Glide.with(context).load(imageURL).into(holder.itemImage);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemPrice;
        public ImageView itemImage;

        public ItemViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.item_name);
            itemPrice = view.findViewById(R.id.item_price);
            itemImage = view.findViewById(R.id.item_image);
        }
    }
}
