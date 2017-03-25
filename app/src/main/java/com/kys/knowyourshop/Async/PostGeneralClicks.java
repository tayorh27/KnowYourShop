package com.kys.knowyourshop.Async;

import android.content.Context;
import android.os.AsyncTask;

import com.kys.knowyourshop.network.GeneralPostingServer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanniAdewale on 22/03/2017.
 */

public class PostGeneralClicks extends AsyncTask<Void, Void, Void> {

    Context context;
    String url_name;
    Map<String, String> _params = new HashMap<>();

    public PostGeneralClicks(Context context, String url_name, Map<String, String> _params) {
        this.context = context;
        this.url_name = url_name;
        this._params = _params;
    }

    @Override
    protected Void doInBackground(Void... params) {
        GeneralPostingServer generalPostingServer = new GeneralPostingServer(context, url_name, _params);
        generalPostingServer.PostMe();
        return null;
    }
}
