package com.kys.knowyourshop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.kys.knowyourshop.Callbacks.VerifyAccessCodeCallback;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.UpdateUser;
import com.kys.knowyourshop.network.VerifyAccessCode;

import java.util.Random;

public class AccessCodeActivity extends AppCompatActivity implements VerifyAccessCodeCallback {

    EditText access;
    CardView cardView;
    TextView textView;
    EditText pass1, pass2;
    String _username = "";
    General general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_code);

        access = (EditText) findViewById(R.id.edit_access_code);
        cardView = (CardView) findViewById(R.id.card1);
        textView = (TextView) findViewById(R.id.tvEmail);
        pass1 = (EditText) findViewById(R.id.password1);
        pass2 = (EditText) findViewById(R.id.password2);
        general = new General(AccessCodeActivity.this);

        access.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String accessCode = access.getText().toString();
                    verify(accessCode);
                }
                return false;
            }
        });
    }

    private void verify(String accessCode) {
        VerifyAccessCode verifyAccessCode = new VerifyAccessCode(AccessCodeActivity.this, accessCode, this);
        verifyAccessCode.CheckAccessCode(cardView, textView);
    }

    public void UpdatePassword(View view) {
        String password1 = pass1.getText().toString();
        String password2 = pass2.getText().toString();
        String _email = textView.getText().toString();
        String access_code = "KYS" + _username.substring(0, 2) + _email.substring(0, 2) + new Random().nextInt(9000);

        if (password1.isEmpty() || password2.isEmpty()) {
            general.error("Please all fields must be filled");
            return;
        }
        if (!password1.contentEquals(password2)) {
            general.error("Passwords do not match");
            return;
        }
        UpdateUser updateUser = new UpdateUser(AccessCodeActivity.this, _email, password2, access_code);
        updateUser.UpdateUser(AccessCodeActivity.this);
    }

    @Override
    public void onAccessCode(String username) {
        _username = username;
    }
}
