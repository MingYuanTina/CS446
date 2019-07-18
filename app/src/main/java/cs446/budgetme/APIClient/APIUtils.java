package cs446.budgetme.APIClient;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Transaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtils {
    private GetDataService apiInterface;
    private static final String TAG = APIUtils.class.getName();
    final String USER_TOKEN= "5d2e9e1059613a39f2e27a43";
    final String groupID = "adc";


    public APIUtils(){
        RetrofitClient retrofit = new RetrofitClient();
        apiInterface= retrofit.getRetrofitClient().create(GetDataService.class);
    }
    public GetDataService getApiInterface(){
        return apiInterface;
    }
    public void postTrans(Transaction tran) {

        apiInterface.addTransaction(tran, USER_TOKEN).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    JsonObject obj = response.body().getAsJsonObject();

                    Log.i(TAG, "id value returned is " + obj.get("TransId").getAsString());
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void postGoal(Goal goal){
        apiInterface.addGoal(goal, USER_TOKEN, groupID).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {

                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });

    }


}
