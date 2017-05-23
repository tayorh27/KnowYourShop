package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.User;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.ForgotPasswordServer;

import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    General general;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        general = new General(ForgotPasswordActivity.this);
        email = (EditText) findViewById(R.id.edit_email);
    }

    public void RequestPassword(View view) {
        String email_add = email.getText().toString();
        if (email_add.isEmpty()) {
            general.error("Please fill email address");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_add).matches()) {
            general.error("Invalid email address");
            return;
        }
        ForgotPasswordServer forgotPasswordServer = new ForgotPasswordServer(ForgotPasswordActivity.this, email_add);
        forgotPasswordServer.CheckUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void AccessCode(View view) {
        startActivity(new Intent(ForgotPasswordActivity.this, AccessCodeActivity.class));
    }
}
