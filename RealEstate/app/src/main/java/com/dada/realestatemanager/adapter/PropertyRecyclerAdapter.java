package com.dada.realestatemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dada.realestatemanager.AppController;
import com.dada.realestatemanager.R;
import com.dada.realestatemanager.util.PropertyList;

import java.util.List;

public class PropertyRecyclerAdapter extends RecyclerView.Adapter<PropertyRecyclerAdapter.MyViewHolder> {

    private List<PropertyList> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private static MyClickListener myClickListener;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PropertyRecyclerAdapter(Context context, List<PropertyList> data) {
        this.mContext = context;
        this.mDatas = data;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String url_1 = "http://" + mDatas.get(position).getImage1().replaceAll("\\\\", "");
        String url_2 = "http://" + mDatas.get(position).getImage2().replaceAll("\\\\", "");
        String url_3 = "http://" + mDatas.get(position).getImage3().replaceAll("\\\\", "");
        holder.thumbNail1.setImageUrl(url_1, imageLoader);
        holder.thumbNail2.setImageUrl(url_2, imageLoader);
        holder.thumbNail3.setImageUrl(url_3, imageLoader);
        holder.name.setText(mDatas.get(position).getName());
        if(mDatas.get(position).getCategory().equals("1")){
            holder.category.setText("Rent");
        } else{
            holder.category.setText("Purchase");
        }

        holder.type.setText(mDatas.get(position).getType());
        holder.price.setText("$" + mDatas.get(position).getCost());
        holder.size.setText(mDatas.get(position).getSize());
        holder.description.setText(mDatas.get(position).getDescription() + "\n\nAddress: " +
                mDatas.get(position).getAddress1() + "," + mDatas.get(position).getAddress2() + ", " + mDatas.get(position).getZip());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.card_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, description, price, size, category, type;
        NetworkImageView thumbNail1, thumbNail2, thumbNail3;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            name = (TextView) view.findViewById(R.id.name);
            category = (TextView) view.findViewById(R.id.category);
            type = (TextView) view.findViewById(R.id.type);
            price = (TextView) view.findViewById(R.id.price);
            size = (TextView) view.findViewById(R.id.size);
            description = (TextView) view.findViewById(R.id.description);
            thumbNail1 = (NetworkImageView) view.findViewById(R.id.thumbnail_1);
            thumbNail2 = (NetworkImageView) view.findViewById(R.id.thumbnail_2);
            thumbNail3 = (NetworkImageView) view.findViewById(R.id.thumbnail_3);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getPosition(), view);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        PropertyRecyclerAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
