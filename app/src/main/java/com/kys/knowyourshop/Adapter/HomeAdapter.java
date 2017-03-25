package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Shop;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.VolleySingleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sanniAdewale on 31/01/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Shop> shopArrayList = new ArrayList<>();
    public VolleySingleton volleySingleton;
    public ImageLoader imageLoader;
    public ShopsClickListener shopsClickListener;

    public HomeAdapter(Context context, ShopsClickListener shopsClickListener) {
        this.context = context;
        this.shopsClickListener = shopsClickListener;
        inflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void LoadRecyclerView(ArrayList<Shop> shops) {
        this.shopArrayList = shops;
        notifyDataSetChanged();
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = inflater.inflate(R.layout.shops_layout, parent, false);
        return new HomeHolder(myView);
    }

    @Override
    public void onBindViewHolder(final HomeHolder holder, int position) {

        Shop current = shopArrayList.get(position);
        String url = AppConfig.WEB_URL + "images/" + current.logo;
        String _url = url.replace(" ", "%20");
        holder.sName.setText(current.name);
        holder.sDesc.setText(current.desc);
        holder.ratingBar.setRating(Float.parseFloat(current.ratingStar));
        holder.ratingCount.setText("(" + current.ratingCount + ")");
        imageLoader.get(_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.iv.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String nowDate = mths[month] + " " + day + ", " + year + " ";//Mar 24, 2017 6:00:00 AM
        Date date1 = new Date(nowDate + current.open);
        Date date2 = new Date(nowDate + current.close);
        Date date = new Date();
        long getTime = date.getTime();
        long getTime1 = date1.getTime();
        long getTime2 = date2.getTime();

        if (getTime < getTime1 || getTime > getTime2) {
            holder.button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return shopArrayList.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        RatingBar ratingBar;
        TextView ratingCount;
        TextView sName, sDesc;
        RelativeLayout relativeLayout;
        BootstrapButton button;

        HomeHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.shop_logo);
            ratingBar = (RatingBar) itemView.findViewById(R.id.shop_rating);
            ratingCount = (TextView) itemView.findViewById(R.id.shop_rating_number);
            sName = (TextView) itemView.findViewById(R.id.shop_name);
            sDesc = (TextView) itemView.findViewById(R.id.shop_desc);
            button = (BootstrapButton) itemView.findViewById(R.id.btnClosed);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.root);
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
