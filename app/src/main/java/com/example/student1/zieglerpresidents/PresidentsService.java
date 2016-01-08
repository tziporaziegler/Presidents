package com.example.student1.zieglerpresidents;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PresidentsService {

    @GET("/hitch17/sample-data/blob/master/presidents.json")
    Call<List<President>> listPresidents();
}
