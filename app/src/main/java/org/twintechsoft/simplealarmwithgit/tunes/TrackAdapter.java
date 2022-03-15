package org.twintechsoft.simplealarmwithgit.tunes;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.twintechsoft.simplealarmwithgit.R;


public class TrackAdapter extends BaseAdapter {
    private Context context;
    private TrackModel[] trackModels;

    public TrackAdapter(Context context, TrackModel[] trackModels) {
        this.context = context;
        this.trackModels = trackModels;
    }

    @Override
    public int getCount() {
        return trackModels.length;
    }

    @Override
    public Object getItem(int position) {
        return trackModels[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrackModel trac = (TrackModel) getItem(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.track_item, parent,false);
            holder = new ViewHolder();
            holder.trackImage = convertView.findViewById(R.id.track_image);
            holder.titleText = convertView.findViewById(R.id.track_title);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleText.setText(trac.getName());
        if(trac.isPlaying()){
            holder.trackImage.setImageResource(R.drawable.marking);
        }
        else {
            holder.trackImage.setImageResource(0);
        }
        return convertView;
    }
    static class ViewHolder{
        ImageView trackImage;
        TextView titleText;
    }
}
