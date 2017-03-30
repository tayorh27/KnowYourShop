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

import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.PostRegisterUser;

import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etMobile, etPassword;
    General general;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        general = new General(RegisterActivity.this);

        etUsername = (EditText) findViewById(R.id.edit_username);
        etEmail = (EditText) findViewById(R.id.edit_email);
        etMobile = (EditText) findViewById(R.id.edit_mobile);
        etPassword = (EditText) findViewById(R.id.edit_password);
    }

    public void AlreadyHaveAccount(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    public void RegisterUser(View view) {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
            general.error("Please all fields must be filled");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            general.error("Invalid email address");
            return;
        }
        String access_code = "KYS" + username.substring(0, 2) + email.substring(0, 2) + new Random().nextInt(1000);
        PostRegisterUser postRegisterUser = new PostRegisterUser(RegisterActivity.this, username, email, mobile, password, access_code);
        postRegisterUser.Register(RegisterActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
