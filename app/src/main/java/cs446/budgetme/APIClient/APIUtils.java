package cs446.budgetme.APIClient;

import android.util.Log;

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


    public APIUtils(){
        RetrofitClient retrofit = new RetrofitClient();
        apiInterface= retrofit.getRetrofitClient().create(GetDataService.class);
    }
    public GetDataService getApiInterface(){
        return apiInterface;
    }
    public void postTrans(Transaction tran) {

        apiInterface.addTransaction(tran, USER_TOKEN).enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void postGoal(Goal goal){

    }


}
