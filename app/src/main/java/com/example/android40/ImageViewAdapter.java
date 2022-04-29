package com.example.android40;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageViewAdapter extends ArrayAdapter<Photo>{

    Context mContext;
    int mResource;
    ArrayList<Photo> images;


    public ImageViewAdapter(@NonNull Context context, int resource, ArrayList<Photo> objects) {
        super(context, R.layout.imageview_item);
        this.mContext = context;
        this.images = objects;
        this.mResource = resource;
    }

    @Nullable
    @Override
    public Photo getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.imageview_item, parent, false);
            holder.view = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(holder.view != null){
            ImageLoader loader = new ImageLoader(holder.view);
            loader.execute(images.get(position).getPath());
            holder.view.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            holder.view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.view.setPadding(8, 8, 8, 8);
        }

        return convertView;
    }

    static class ViewHolder{
        ImageView view;
    }

    public void removeItem(int index){
        images.remove(index);
        notifyDataSetChanged();
    }

    public void addItem(String p){
        images.add(new Photo(p));
        notifyDataSetChanged();
    }
}
