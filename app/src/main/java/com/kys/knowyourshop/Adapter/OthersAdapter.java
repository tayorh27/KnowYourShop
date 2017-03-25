package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Shop;
import com.kys.knowyourshop.R;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 22/03/2017.
 */

public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.OthersHolder> {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Shop> ShopArrayList = new ArrayList<>();
    public ShopsClickListener shopsClickListener;

    public OthersAdapter(Context context, ShopsClickListener shopsClickListener) {
        this.context = context;
        this.shopsClickListener = shopsClickListener;
        inflater = LayoutInflater.from(context);
    }

    public void LoadRecyclerView(ArrayList<Shop> shops) {
        this.ShopArrayList = shops;
        notifyDataSetChanged();
    }

    @Override
    public OthersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = inflater.inflate(R.layout.shops_cat_layout, parent, false);
        return new OthersHolder(myView);
    }

    @Override
    public void onBindViewHolder(OthersHolder holder, int position) {
        Shop shop = ShopArrayList.get(position);
        holder.tv.setText(shop.area);
    }

    @Override
    public int getItemCount() {
        return ShopArrayList.size();
    }

    class OthersHolder extends RecyclerView.ViewHolder {

        TextView tv;
        RelativeLayout relativeLayout;

        public OthersHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.sh_cat);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.cat_root);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shopsClickListener != null) {
                        shopsClickListener.onShopsClickListener(view, getPosition());
                    }
                }
            });
        }
    }
}
