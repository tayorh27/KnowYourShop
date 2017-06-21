package com.kys.knowyourshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.kys.knowyourshop.R;

public class RatingDetailsActivity extends AppCompatActivity {

    EditText title, comment, items;
    General general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (EditText) findViewById(R.id.edit_title);
        comment = (EditText) findViewById(R.id.edit_comment);
        items = (EditText) findViewById(R.id.edit_items);
        general = new General(RatingDetailsActivity.this);

        Bundle bundle = getIntent().getExtras();
        title.setText(bundle.getString("title"));
        comment.setText(bundle.getString("comment"));
        items.setText(bundle.getString("items"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_title = title.getText().toString();
                String get_comment = comment.getText().toString();
                String get_items = items.getText().toString();
                if (get_title.isEmpty() || get_comment.isEmpty()) {
                    general.error("Please all fields must be filled");
                    return;
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("rate_title", get_title);
                bundle.putString("items", get_items);
                bundle.putString("rate_comment", get_comment);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
