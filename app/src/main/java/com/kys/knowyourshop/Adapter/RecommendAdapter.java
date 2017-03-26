package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Recommend;
import com.kys.knowyourshop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 23/03/2017.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendHolder> {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Recommend> recommendArrayList = new ArrayList<>();
    public ShopsClickListener shopsClickListener;

    public RecommendAdapter(Context context, ShopsClickListener shopsClickListener) {
        this.context = context;
        this.shopsClickListener = shopsClickListener;
        inflater = LayoutInflater.from(context);
    }

    public void LoadRecyclerView(ArrayList<Recommend> recommends) {
        this.recommendArrayList = recommends;
        notifyDataSetChanged();
    }

    @Override
    public RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = inflater.inflate(R.layout.recommend_layout, parent, false);
        return new RecommendHolder(myView);
    }

    @Override
    public void onBindViewHolder(RecommendHolder holder, int position) {
        Recommend recommend = recommendArrayList.get(position);
        holder.ratingBar.setRating(Float.parseFloat(recommend.rating));
        holder.rate_number.setText(recommend.rating);
        holder.shopName.setText(recommend.shop_name);
        holder.shopAdd.setText(recommend.shop_add);
        Picasso.with(context).load(recommend.icon).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return recommendArrayList.size();
    }

    class RecommendHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        RatingBar ratingBar;
        TextView rate_number, shopName, shopAdd;
        BootstrapButton button;

        RecommendHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.shop_logo);
            ratingBar = (RatingBar) itemView.findViewById(R.id.shop_rating);
            rate_number = (TextView) itemView.findViewById(R.id.shop_rating_number);
            shopName = (TextView) itemView.findViewById(R.id.shop_name);
            shopAdd = (TextView) itemView.findViewById(R.id.shop_add);
            button = (BootstrapButton) itemView.findViewById(R.id.btn_rec);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shopsClickListener != null) {
                        shopsClickListener.onShopsClickListener(v, getPosition());
                    }
                }
            });
        }
    }
}
