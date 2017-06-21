package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kys.knowyourshop.Information.Rating;
import com.kys.knowyourshop.R;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by sanniAdewale on 24/03/2017.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingHolder> {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Rating> ratingArrayList = new ArrayList<>();

    public RatingAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void LoadRecyclerView(ArrayList<Rating> ratings) {
        this.ratingArrayList = ratings;
        notifyDataSetChanged();
    }

    @Override
    public RatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = inflater.inflate(R.layout.shop_rate_layout, parent, false);
        return new RatingHolder(myView);
    }

    @Override
    public void onBindViewHolder(RatingHolder holder, int position) {
        Rating current = ratingArrayList.get(position);
        holder.ratingBar.setRating(Float.parseFloat(current.star));
        holder.tv_title.setText(current.title);
        holder.tv_date_user.setText(current.date + " | " + current.username);
        holder.tv_comment.setText(current.comment);
        holder.tv_rate_value.setText(current.star);
    }

    @Override
    public int getItemCount() {
        return ratingArrayList.size();
    }

    class RatingHolder extends RecyclerView.ViewHolder {

        RatingBar ratingBar;
        TextView tv_title, tv_date_user, tv_comment, tv_rate_value;

        RatingHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tv_title = (TextView) itemView.findViewById(R.id.rTitle);
            tv_date_user = (TextView) itemView.findViewById(R.id.rDateUser);
            tv_comment = (TextView) itemView.findViewById(R.id.rComment);
            tv_rate_value = (TextView) itemView.findViewById(R.id.rRateValue);
        }
    }
}
