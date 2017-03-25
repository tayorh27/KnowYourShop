package com.kys.knowyourshop.Activity;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kys.knowyourshop.R;

/**
 * Created by sanniAdewale on 23/03/2017.
 */

public class General {

    Context context;
    MaterialDialog materialDialog;

    public General(Context context) {
        this.context = context;
    }

    public void displayDialog(String text) {
        materialDialog = new MaterialDialog.Builder(context)
                .content(text)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    public void dismissDialog() {
        materialDialog.dismiss();
    }

    public void error(String text) {
        new MaterialDialog.Builder(context)
                .title("Error")
                .content(text)
                .positiveText("OK")
                .show();
    }

    public void success(String text) {
        new MaterialDialog.Builder(context)
                .title("Success")
                .titleColor(context.getResources().getColor(R.color.bg_screen3))
                .content(text)
                .contentColor(context.getResources().getColor(R.color.bg_screen3))
                .positiveText("OK")
                .positiveColor(context.getResources().getColor(R.color.bg_screen3))
                .show();
    }
}
