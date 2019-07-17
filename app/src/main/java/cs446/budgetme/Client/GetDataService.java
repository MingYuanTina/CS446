package cs446.budgetme.Client;

import java.util.List;

import cs446.budgetme.Model.Transaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetDataService {
    @GET("/trans/{user_token}")
    Call<List<Transaction>> getUserTrans(@Path("user_token") String token);

    @POST("/trans/{user_token}")
    Call<Transaction> addTransaction(@Body Transaction trans,@Path("user_token") String token );
}
