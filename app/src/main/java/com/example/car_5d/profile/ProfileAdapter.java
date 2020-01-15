package com.example.car_5d.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.car_5d.R;

import java.util.List;

public class ProfileAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Profile> profileList;

    public ProfileAdapter(Context context, int layout, List<Profile> profileList) {
        this.context = context;
        this.layout = layout;
        this.profileList = profileList;
    }

    @Override
    public int getCount() {
        return profileList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView name;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();

            holder.name = convertView.findViewById(R.id.tvProfileName);
            holder.imageView = convertView.findViewById(R.id.imvProfileHinh);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Profile profiles = profileList.get(position);

        holder.imageView.setImageResource(profiles.getImageView());
        holder.name.setText(profiles.getName());

        return convertView;
    }
}
