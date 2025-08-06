package com.luciano.tesisplanificatuviaje.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DialogflowService {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer TU_TOKEN"
    })
    @POST("detectIntent")
    Call<MessageResponse> sendMessage(@Body MessageRequest request);
}

