package com.example.car_5d.park;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.car_5d.R;

import java.util.List;

public class ParkAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Park> parkList;

    public ParkAdapter(Context context, int layout, List<Park> parkList) {
        this.context = context;
        this.layout = layout;
        this.parkList = parkList;
    }

    @Override
    public int getCount() {
        return parkList.size();
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
        TextView tvParkBienso, tvParkTenduong, tvParkDate, tvParkStartTime, tvParkEndTime;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder = new ViewHolder();

            holder.tvParkBienso = view.findViewById(R.id.tvParkBienso);
            holder.tvParkTenduong = view.findViewById(R.id.tvParkTenduong);
            holder.tvParkDate = view.findViewById(R.id.tvParkDate);
            holder.tvParkStartTime = view.findViewById(R.id.tvParkStartTime);
            holder.tvParkEndTime = view.findViewById(R.id.tvParkEndTime);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        Park parks = parkList.get(position);

        holder.tvParkBienso.setText(parks.getBienso());
        holder.tvParkTenduong.setText(parks.getTenduong());
        holder.tvParkDate.setText("D: "+parks.getDatestamp());
        holder.tvParkStartTime.setText("S: "+parks.getTimestart());
        holder.tvParkEndTime.setText("E: "+parks.getTimeend());

        return view;
    }
}
