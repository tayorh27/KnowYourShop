package com.kys.knowyourshop.Service;


import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.kys.knowyourshop.UserLocationService;

/**
 * Created by sanniAdewale on 23/03/2017.
 */

public class MyLocationService extends GcmTaskService {


    @Override
    public int onRunTask(TaskParams taskParams) {

        new UserLocationService();

        return GcmNetworkManager.RESULT_RESCHEDULE;
    }


}
