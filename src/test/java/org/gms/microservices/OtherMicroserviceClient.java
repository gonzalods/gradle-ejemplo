package org.gms.microservices;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.Map;

/**
 * Created by gonzalo on 05/06/2016.
 */
public interface OtherMicroserviceClient {

    @GET("/data")
    Call<Map<String, String>> getData();
}
