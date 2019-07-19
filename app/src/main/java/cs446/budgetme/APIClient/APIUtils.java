package cs446.budgetme.APIClient;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Transaction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtils {
    private GetDataService apiInterface;
    private static final String TAG = APIUtils.class.getName();
    final String USER_TOKEN= "5d30ff4e6397c4000427fabe";
    final String groupID = "5d30ff4e6397c4000427fabd";


    public APIUtils(){
        RetrofitClient retrofit = new RetrofitClient();
        apiInterface= retrofit.getRetrofitClient().create(GetDataService.class);
    }
    public GetDataService getApiInterface(){
        return apiInterface;
    }

    public void deleteTrans(Transaction tran) {

        apiInterface.deleteTransaction(USER_TOKEN, tran.getId(), groupID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
