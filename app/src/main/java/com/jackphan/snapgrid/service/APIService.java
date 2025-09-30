package com.jackphan.snapgrid.service;

import com.jackphan.snapgrid.models.Message;
import com.jackphan.snapgrid.models.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @GET("Chats.json")
    Call<Map<String, Message>> getMessages();

    @POST("Chats.json")
    Call<Void> createMessage(@Body Message message);

    @GET("User")
    Call<Map<String, User>> getUsers();

    @POST("Users.json")
    Call<Void> addUser(@Body User user);

    @PATCH("Users/{userId}.json")
    Call<Void> updateUser(@Path("userId") String userId, @Body User updatedUser);

}
