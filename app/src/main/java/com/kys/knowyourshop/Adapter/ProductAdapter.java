package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Product;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 04/02/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Product> productArrayList = new ArrayList<>();
    public VolleySingleton volleySingleton;
    public ImageLoader imageLoader;
    public ShopsClickListener shopsClickListener;

    public ProductAdapter(Context context) {
        this.context = context;
        //this.shopsClickListener = shopsClickListener;
        inflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void LoadRecyclerView(ArrayList<Product> products) {
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
        Product current = productArrayList.get(position);
        String url = AppConfig.WEB_URL + "images/" + current.product_logo;
        String _url = url.replace(" ","%20");

        holder.sName.setText(current.product_name);
        holder.sDesc.setText(current.product_description);
        holder.price_stock.setText("Price: ₦"+current.product_price+" | in-stock: "+current.in_stock);
        imageLoader.get(_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.iv.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView price_stock;
        TextView sName, sDesc;

        ProductHolder(View itemView){
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.shop_logo);
            price_stock = (TextView) itemView.findViewById(R.id.p_price_stock);
            sName = (TextView) itemView.findViewById(R.id.shop_name);
            sDesc = (TextView) itemView.findViewById(R.id.shop_desc);
        }
    }
}
