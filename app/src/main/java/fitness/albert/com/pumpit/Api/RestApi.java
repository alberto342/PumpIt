package fitness.albert.com.pumpit.Api;

import fitness.albert.com.pumpit.Model.FoodListResponse;
import fitness.albert.com.pumpit.RetroRequest.FoodRequest;
import fitness.albert.com.pumpit.Utils.Global;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestApi {

    @POST(Global.NUTRIENTS)
    Call<FoodListResponse> foodList(@Header("x-app-id") String x_app_id, @Header("x-app-key") String x_app_key, @Body FoodRequest foodRequest);
}
