package com.dada.realestatemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.dada.realestatemanager.R;
import com.dada.realestatemanager.activity.MapsActivity;
import com.dada.realestatemanager.util.PropertyItem;

import java.util.List;

/**
 * Created by zhang on 2016/7/27.
 */
public class MapAdapter extends BaseAdapter {

    List<PropertyItem> items;
    Context context;

    public MapAdapter(Context context, List<PropertyItem> items) {
        this.items = items;
        this.context = context;
    }

    private class ViewHolder
    {
        ImageView img;
        TextView nameValue;
        TextView typeValue;
        TextView sizeValue;
        TextView costValue;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PropertyItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(MapsActivity.LAYOUT_INFLATER_SERVICE);
        if(convertView ==null)
        {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.icon);
            holder.nameValue =(TextView)convertView.findViewById(R.id.name_value);
            holder.sizeValue = (TextView)convertView.findViewById(R.id.size_value);
            holder.typeValue = (TextView)convertView.findViewById(R.id.type_value);
            holder.costValue = (TextView)convertView.findViewById(R.id.cost_value);
            convertView.setTag(holder);
        }

        else
        {
            holder = (ViewHolder)convertView.getTag();
            //holder.img.setImageResource();
           // PropertyItem pItem = ( PropertyItem)(getItem(position));
            holder.typeValue.setText(getItem(position).type);
            holder.sizeValue.setText(getItem(position).size);
            holder.nameValue.setText(getItem(position).name);
            holder.costValue.setText(getItem(position).cost);
        }

        return convertView;
    }
}
