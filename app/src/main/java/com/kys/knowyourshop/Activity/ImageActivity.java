package com.kys.knowyourshop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kys.knowyourshop.R;
import com.master.glideimageview.GlideImageView;

public class ImageActivity extends AppCompatActivity {

    GlideImageView glideImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        glideImageView = (GlideImageView) findViewById(R.id.glide_image_view);

        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(bundle.getString("name"));
        glideImageView.loadImageUrl(bundle.getString("img"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
