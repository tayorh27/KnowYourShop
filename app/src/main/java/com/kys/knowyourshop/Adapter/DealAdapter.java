package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Deal;
import com.kys.knowyourshop.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sanniAdewale on 31/03/2017.
 */

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealHolder> {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Deal> dealArrayList = new ArrayList<>();
    public ShopsClickListener shopsClickListener;

    public DealAdapter(Context context, ShopsClickListener shopsClickListener) {
        this.context = context;
        this.shopsClickListener = shopsClickListener;
        inflater = LayoutInflater.from(context);
    }

    public void LoadRecyclerView(ArrayList<Deal> deals) {
        this.dealArrayList = deals;
        notifyDataSetChanged();
    }

    @Override
    public DealHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.deals_layout, parent, false);
        DealHolder dealHolder = new DealHolder(view);
        return dealHolder;
    }

    @Override
    public void onBindViewHolder(DealHolder holder, int position) {
        Deal deal = dealArrayList.get(position);
        String ex = deal.expire;
        Date date = new Date(ex);
        long getServerDate = date.getTime();
        Date date1 = new Date();
        long getCurrentDate = date1.getTime();
        if (getCurrentDate > getServerDate) {
            holder._deal.setText(deal.deal);
            holder.expires.setText(deal.expire);
            holder.comment.setText(deal.comment);
            holder.shopName.setText(deal.shop);
            holder.info.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dealArrayList.size();
    }


    class DealHolder extends RecyclerView.ViewHolder {

        TextView _deal, expires, comment, shopName, info;

        DealHolder(View itemView) {
            super(itemView);
            _deal = (TextView) itemView.findViewById(R.id.tvDeal);
            expires = (TextView) itemView.findViewById(R.id.tvExpire);
            comment = (TextView) itemView.findViewById(R.id.tvComment);
            shopName = (TextView) itemView.findViewById(R.id.tvShopName);
            info = (TextView) itemView.findViewById(R.id.tvInfo);
            info.setOnClickListener(new View.OnClickListener() {
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
