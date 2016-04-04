package com.humane.admin.etms.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/status/attend")
    Call<ResponseBody> statusAttend(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/major")
    Call<ResponseBody> statusMajor(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/dept")
    Call<ResponseBody> statusDept(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/hall")
    Call<ResponseBody> statusHall(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/group")
    Call<ResponseBody> statusGroup(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/examinee")
    Call<ResponseBody> statusExaminee(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/image/examinee/{fileName}")
    Call<ResponseBody> imageExaminee(@Path("fileName") String fileName);

    @GET("api/status/toolbar")
    Call<ResponseBody> statusToolbar(@Query("q") String q);
}