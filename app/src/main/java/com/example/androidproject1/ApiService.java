package com.example.androidproject1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @POST("check-username")
    Call<Void> checkUsername(@Body UsernameRequest usernameRequest);

    // POST 요청으로 사용자 정보를 서버에 전달
    @POST("register") // "/register"는 Node.js 서버의 경로
    Call<Void> registerUser(@Body User user); // POST 요청의 body에 User 객체를 포함

    @POST("login") // 서버의 로그인 엔드포인트
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("create-question")
    Call<Void> createQuestion(@Body QuestionRequest questionRequest);

    @GET("get-all-questions")
    Call<List<Question>> getAllQuestions(@Query("user_id") int userId);

}
