package com.example.gl62.vietnamtravelplace.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gl62.vietnamtravelplace.R;
import com.example.gl62.vietnamtravelplace.object.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GL62 on 4/3/2017.
 */

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.MyViewHolder> {
    private List<String> lImage;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_image_list;

        public MyViewHolder(View view) {
            super(view);
            img_image_list = (ImageView) view.findViewById(R.id.img_image_list);

        }
    }

    public ImageViewAdapter(List<String> _lImage,Context _context) {
        this.context = _context;
        this.lImage = _lImage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String sUrl = lImage.get(position);
        Picasso.with(context)
                .load("http://bwhere.vn/uploads/small/"+sUrl)
                .placeholder(R.drawable.ic_loading)
                .into(holder.img_image_list);
    }

    @Override
    public int getItemCount() {
        return lImage.size();
    }

}
