package com.example.pavan.mobiofire.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.pavan.mobiofire.Activities.Individual_thumb_Activity;
import com.example.pavan.mobiofire.DatabaseHandler.DatabaseHandler;
import com.example.pavan.mobiofire.DatabaseHandler.thumbs_model;
import com.example.pavan.mobiofire.Helpers.CustomVolleyRequestQueue;
import com.example.pavan.mobiofire.R;

import java.util.ArrayList;
import java.util.List;



public class listviewAdapter extends BaseAdapter {
    Context context;
    DatabaseHandler db;
    LayoutInflater inflater;
    List<thumbs_model> thumbs_modelarray = new ArrayList<>();
    String view_mode=" ";

    public listviewAdapter(Context applicationContext, DatabaseHandler db,String view_mode) {
        this.context = applicationContext;
        this.db = db;
        this.thumbs_modelarray = db.getAllContacts();
        this.view_mode=view_mode;
        inflater = (LayoutInflater) applicationContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return thumbs_modelarray.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder {
        TextView title,descrption;
        NetworkImageView imageView;
        LinearLayout normal_view;
        LinearLayout video_view;

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view_mode=="normal_view") {
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.thumb_activity, null);
            holder.title = (TextView) rowView.findViewById(R.id.thumb_title);
            holder.descrption = (TextView) rowView.findViewById(R.id.thumb_desc);
            holder.imageView = (NetworkImageView) rowView.findViewById(R.id.thumb_image);
            holder.video_view=(LinearLayout)rowView.findViewById(R.id.video_view);

            holder.video_view.setVisibility(View.GONE);
            holder.title.setText(thumbs_modelarray.get(i).getTitle());
            holder.descrption.setText(thumbs_modelarray.get(i).getDescription());

            ImageLoader mImageLoader;
            mImageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
            mImageLoader.get(thumbs_modelarray.get(i).getThumb(), ImageLoader.getImageListener(holder.imageView,
                    R.mipmap.ic_launcher, android.R.drawable
                            .ic_dialog_alert));
            holder.imageView.setImageUrl(thumbs_modelarray.get(i).getThumb(), mImageLoader);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(thumbs_modelarray.get(i).getUrl().length()>1) {
                        Intent mIntent = new Intent(context, Individual_thumb_Activity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mIntent.putExtra("url", thumbs_modelarray.get(i).getUrl());
                        mIntent.putExtra("id", thumbs_modelarray.get(i).getId());
                        context.startActivity(mIntent);
                    }
                }
            });
            return rowView;
        }else {
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.thumb_activity, null);
            holder.title = (TextView) rowView.findViewById(R.id.thumb_title_play);
            holder.descrption = (TextView) rowView.findViewById(R.id.thumb_desc_play);
            holder.imageView = (NetworkImageView) rowView.findViewById(R.id.thumb_image_play);
            holder.normal_view=(LinearLayout)rowView.findViewById(R.id.normal_view);

            holder.normal_view.setVisibility(View.GONE);

            holder.title.setText(thumbs_modelarray.get(i).getTitle());
            holder.descrption.setText(thumbs_modelarray.get(i).getDescription());

            ImageLoader mImageLoader;
            mImageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
            mImageLoader.get(thumbs_modelarray.get(i).getThumb(), ImageLoader.getImageListener(holder.imageView,
                    R.mipmap.ic_launcher, android.R.drawable
                            .ic_dialog_alert));
            holder.imageView.setImageUrl(thumbs_modelarray.get(i).getThumb(), mImageLoader);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(context, Individual_thumb_Activity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mIntent.putExtra("url", thumbs_modelarray.get(i).getUrl());
                    mIntent.putExtra("id", thumbs_modelarray.get(i).getId());
                    context.startActivity(mIntent);
                }
            });
            return rowView;
        }
    }
}
