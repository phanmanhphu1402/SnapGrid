package com.android.snapgrid.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAPFxchkg:APA91bFEUdq2umjc1q0KnG2sVE_PWICJbb3YR19u7F3yfgNKtz1u3zVj0-cmxSmv5FQcW2HmqqTt5Yondql3BdXZNhgxRVc1RBBneSk9YBKdRMJTMNA9Hqkbt0twKm_DOwH2lL3wvRy0"
    })

    @POST("fom/send")
    Call<Response> sendNotification(@Body sender body);
}
