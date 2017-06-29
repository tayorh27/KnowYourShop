package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Product;
import com.kys.knowyourshop.Information.SpecialProducts;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 04/02/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ProductHolder> {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<SpecialProducts> productArrayList = new ArrayList<>();
    public VolleySingleton volleySingleton;
    public ImageLoader imageLoader;
    public ShopsClickListener shopsClickListener;

    public SearchAdapter(Context context, ShopsClickListener shopsClickListener) {
        this.context = context;
        this.shopsClickListener = shopsClickListener;
        inflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void LoadRecyclerView(ArrayList<SpecialProducts> products) {
        this.productArrayList = products;
        notifyDataSetChanged();
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_layout, parent, false);
        ProductHolder productHolder = new ProductHolder(view);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        SpecialProducts current = productArrayList.get(position);
        String url = AppConfig.WEB_URL + "images/" + current.product_logo;
        if (current.product_logo.contains("http") || current.product_logo.contains("www.")) {
            url = current.product_logo;
        }
        String _url = url.replace(" ", "%20");

        holder.sName.setText(current.product_name);
        holder.sDesc.setText("Shop: " + current.shop_name);
        holder.sDesc2.setText(current.product_description);
        holder.price_stock.setText("Price: ₦" + current.product_price + " | in-stock: " + current.in_stock);
        Glide.with(context).load(_url).fitCenter().centerCrop().placeholder(R.drawable.no_logo).crossFade().error(R.drawable.no_logo).into(holder.iv);
//        imageLoader.get(_url, new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                holder.iv.setImageBitmap(response.getBitmap());
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView price_stock;
        TextView sName, sDesc, sDesc2;
        RelativeLayout relativeLayout;

        ProductHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.shop_logo);
            price_stock = (TextView) itemView.findViewById(R.id.p_price_stock);
            sName = (TextView) itemView.findViewById(R.id.shop_name);
            sDesc = (TextView) itemView.findViewById(R.id.shop_desc);
            sDesc2 = (TextView) itemView.findViewById(R.id.shop_desc2);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.root);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
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
