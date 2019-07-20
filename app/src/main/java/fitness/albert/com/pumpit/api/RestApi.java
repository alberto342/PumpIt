package fitness.albert.com.pumpit.api;

import fitness.albert.com.pumpit.model.CommonListResponse;
import fitness.albert.com.pumpit.model.FoodListResponse;
import fitness.albert.com.pumpit.retro_request.FoodRequest;
import fitness.albert.com.pumpit.utils.Global;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestApi {

    @POST(Global.NUTRIENTS)
    Call<FoodListResponse> foodList(@Header("x-app-id") String x_app_id, @Header("x-app-key") String x_app_key, @Body FoodRequest foodRequest);

    @GET(Global.SEARCH_INSTANT)
    Call<CommonListResponse> foodListInstant(@Header("x-app-id") String x_app_id, @Header("x-app-key") String x_app_key, @Body FoodRequest foodRequest);


}