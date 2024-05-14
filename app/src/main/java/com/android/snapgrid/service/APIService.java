package com.android.snapgrid.service;

import com.android.snapgrid.models.ChatResponse;
import com.android.snapgrid.models.Message;
import com.android.snapgrid.models.Post;
import com.android.snapgrid.models.User;
import com.google.firebase.firestore.model.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.Api;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
