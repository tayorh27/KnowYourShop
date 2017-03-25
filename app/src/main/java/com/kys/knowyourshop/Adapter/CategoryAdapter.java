package com.kys.knowyourshop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Category;
import com.kys.knowyourshop.R;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 01/02/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatHolder>{

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Category> categoryArrayList = new ArrayList<>();
    public ShopsClickListener shopsClickListener;

    public CategoryAdapter(Context context, ShopsClickListener shopsClickListener) {
        this.context = context;
        this.shopsClickListener = shopsClickListener;
        inflater = LayoutInflater.from(context);
    }

    public void LoadRecyclerView(ArrayList<Category> categories) {
        this.categoryArrayList = categories;
        notifyDataSetChanged();
    }

    @Override
    public CatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = inflater.inflate(R.layout.shops_cat_layout, parent, false);
        return new CatHolder(myView);
    }

    @Override
    public void onBindViewHolder(CatHolder holder, int position) {
        Category category = categoryArrayList.get(position);
        holder.tv.setText(category.category);
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class CatHolder extends RecyclerView.ViewHolder{

        TextView tv;
        RelativeLayout relativeLayout;

        public CatHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.sh_cat);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.cat_root);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shopsClickListener != null){
                        shopsClickListener.onShopsClickListener(view, getPosition());
                    }
                }
            });
        }
    }
}
