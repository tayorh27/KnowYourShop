package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapAlert;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.MainActivity;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.PostShopRating;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RatingActivity extends AppCompatActivity {

    TextView shopName, shopDetails;
    AwesomeTextView visited;
    RatingBar ratingBar;
    AppData data;
    String shop_name = "", rate_title = "", rate_comment = "", items = "";
    float rate_value = 0;
    General general;
    private static final int RATING_DETAILS_REQUEST = 2727;
    Set<String> getShopNames, getShopIds;
    int user_id;
    boolean showDialog = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        data = new AppData(RatingActivity.this);
        general = new General(RatingActivity.this);
        shopName = (TextView) findViewById(R.id.rate_shop_name);
        shopDetails = (TextView) findViewById(R.id.rate_shop_details);
        visited = (AwesomeTextView) findViewById(R.id.tvVisited);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setNumStars(5);
        ratingBar.setMax(5);
        ratingBar.setStepSize(0.5f);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate_value = rating;
            }
        });
        visited.setText(data.getVisited());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String shp = bundle.getString("shop_name");
            shopName.setText("Selected shop: " + shp);
            shop_name = shp;
            showDialog = false;
            visited.setVisibility(View.GONE);
        }
    }

    public void ShopNameClick(View view) {
        if (!showDialog) {
            return;
        }
        getShopNames = data.getShopsAvailable();
        getShopIds = data.getShopsUserIdAvailable();
        final String[] myIDs = new String[getShopIds.size()];
        final Iterator<String> ids = getShopIds.iterator();
        for (int i = 0; i < getShopNames.size(); i++) {
            if (ids.hasNext()) {
                myIDs[i] = ids.next();
            }
        }
        if (getShopNames.isEmpty()) {
            //Toast.makeText(RatingActivity.this, "", Toast.LENGTH_SHORT).show();
            return;
        }

        new MaterialDialog.Builder(RatingActivity.this)
                .title("Select the shop you visited")
                .items(getShopNames)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        shop_name = String.valueOf(text);
                        shopName.setText("Selected shop: " + text);
                        user_id = Integer.parseInt(myIDs[which]);
                        return true;
                    }
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positiveText("Choose")
                .show();

    }

    public void ShopRateDetails(View view) {
        Intent intent = new Intent(RatingActivity.this, RatingDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", rate_title);
        bundle.putString("items", items);
        bundle.putString("comment", rate_comment);
        intent.putExtras(bundle);
        startActivityForResult(intent, RATING_DETAILS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RATING_DETAILS_REQUEST) {
            Bundle bundle = data.getExtras();
            rate_title = bundle.getString("rate_title");
            rate_comment = bundle.getString("rate_comment");
            items = bundle.getString("items");
            shopDetails.setText("Title: " + rate_title + "\nItems: " + items + "\nComment: " + rate_comment);
        }
    }

    public void SubmitRating(View view) {
        if (rate_value == 0) {
            general.error("Please give this shop a rating");
            return;
        }
        if (shop_name.isEmpty()) {
            general.error("Please all details must be filled");
            return;
        }
        if (rate_title.isEmpty() || rate_comment.isEmpty()) {
            general.error("Please all details must be filled");
            return;
        }
        if (!data.getLoggedIn()) {
            startActivity(new Intent(RatingActivity.this, LoginActivity.class));
            return;
        }
        String username = data.getUser().username;
        String rv = String.valueOf(rate_value);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String date = day + "." + mths[month] + "." + year;
        PostShopRating postShopRating = new PostShopRating(RatingActivity.this, shop_name, username, rate_title, rate_comment, rv, items, date, user_id);
        postShopRating.PostRating(getShopNames, RatingActivity.this);
    }

    public void NotClick(View view) {
        data.setRatingAvailable(false);
        data.setVisited("");
        startActivity(new Intent(RatingActivity.this, HomeActivity.class));
        finish();
    }

    public void PlacementClick(View view) {
        data.setRatingAvailable(false);
        data.setVisited("");
        data.setPlacement(true);
        startActivity(new Intent(RatingActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
