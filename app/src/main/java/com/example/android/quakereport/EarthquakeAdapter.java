package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<EarthquakeInfo> {
    public EarthquakeAdapter(Context context, List<EarthquakeInfo> Earthquake) {
        super(context, 0, Earthquake);

    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listView=convertView;
        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_info,parent,false);

        }
        EarthquakeInfo currentearthquake=getItem(position);
        double magnitude=currentearthquake.getmMagnitude();
        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        String magn=decimalFormat.format(magnitude);

        TextView magni=(TextView)listView.findViewById(R.id.magn);
        magni.setText(magn);

        String fullLocation=currentearthquake.getmPlace();
        String offset;
        String primary;
        if(fullLocation.contains("of")) {
            int endindex = fullLocation.indexOf("of");
            endindex += 2;

            offset = fullLocation.substring(0, endindex);
             primary = fullLocation.substring(endindex, fullLocation.length());
        }
        else{
            offset=getContext().getString(R.string.near_the);
            primary=fullLocation;
        }
        TextView place1=(TextView)listView.findViewById(R.id.place1);
        place1.setText(offset);

        TextView place2=(TextView)listView.findViewById(R.id.place2);
        place2.setText(primary);

        Date date=new Date(currentearthquake.getmDate());
        String real_date=dateformat(date);
        TextView daate=(TextView)listView.findViewById(R.id.date);
        daate.setText(real_date);

        String real_time=timeformat(date);
        TextView time=(TextView)listView.findViewById(R.id.time);
        time.setText(real_time);

        GradientDrawable gradientDrawable=(GradientDrawable)magni.getBackground();
        int CircleColor=getMagnColor(currentearthquake.getmMagnitude());
        gradientDrawable.setColor(CircleColor);

        return listView;
    }

    private String dateformat(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("LLL dd, yyy");
        return simpleDateFormat.format(date);
    }
    private String timeformat(Date date){
        SimpleDateFormat timeformater=new SimpleDateFormat("h:mm a");
        return timeformater.format(date);
    }

    private int getMagnColor(double magnitude){
        int ColorMagnitude;
        int TrueMagn=(int)Math.floor(magnitude);

        switch (TrueMagn){
            case 0:
            case 1: ColorMagnitude=R.color.magn1;
            break;
            case 2: ColorMagnitude=R.color.magn2;
            break;
            case 3:ColorMagnitude=R.color.magn3;
            break;
            case 4: ColorMagnitude=R.color.magn4;
            break;
            case 5:ColorMagnitude=R.color.magn5;
            break;
            case 6: ColorMagnitude=R.color.magn6;
            break;
            case 7:ColorMagnitude=R.color.magn7;
            break;
            case 8: ColorMagnitude=R.color.magn8;
            break;
            case 9:ColorMagnitude=R.color.magn9;
            break;

            default: ColorMagnitude=R.color.magn10;
                break;

        }
        return ContextCompat.getColor(getContext(),ColorMagnitude);

    }


}
