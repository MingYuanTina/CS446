package cs446.budgetme.APIClient;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;
import cs446.budgetme.Model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtils {
    private static APIUtils mApiUtils;
    private GetDataService apiInterface;
    private static final String TAG = APIUtils.class.toString();

    public interface APIUtilsCallback<E> {
        void onResponseSuccess(E e);
        void onResponseFailure();
    }

    public static APIUtils getInstance() {
        if (mApiUtils == null) {
            mApiUtils = new APIUtils();
        }
        return mApiUtils;
    }

    private APIUtils(){
        RetrofitClient retrofit = new RetrofitClient();
        apiInterface= retrofit.getRetrofitClient().create(GetDataService.class);
    }
    public GetDataService getApiInterface(){
        return apiInterface;
    }

    public void sendAuthen(String email, String password, final APIUtilsCallback<User> callback){
        //TODO: need to hash passoword
        APIUtils apicall = new APIUtils();
        String hashedP = hashPass(password);
        if(hashedP != "") password = hashedP;
        apicall.getApiInterface().getUser(email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    //startDashboardActivity(response.body());
                    callback.onResponseSuccess(response.body());
                }
                else{
                    callback.onResponseFailure();
                    //loginFailed();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API for sendAuthen.");
            }
        });
    }

    private String hashPass(String pass){
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );
            // Change this to UTF-16 if needed
            md.update( pass.getBytes( StandardCharsets.UTF_8 ) );
            byte[] digest = md.digest();
            String hex = String.format( "%064x", new BigInteger( 1, digest ) );
            return hex;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void postRegisterUser(String username, String email, String password, final APIUtilsCallback<ResponseBody> callback){
        String hashedP = hashPass(password);
        if(hashedP != "") password = hashedP;
        RegisterRequest request = new RegisterRequest(username,email,password);
        getApiInterface().registerAccount(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    callback.onResponseSuccess(response.body());
                }
                else{
                    callback.onResponseFailure();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API for register user.");
            }
        });
    }

    public class RegisterRequest{
        private String username;
        private String email;
        private String password;

        public RegisterRequest(String username, String email, String password){
            this.username= username;
            this.email= email;
            this.password= password;
        }
    }

    public void postTransaction(Transaction tran, String USER_TOKEN, String groupID, final APIUtilsCallback<JsonElement> callback) {

        APIUtils.getInstance().getApiInterface().addTransaction(tran, USER_TOKEN, groupID).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    callback.onResponseSuccess(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API for transaction.");
            }
        });
    }

    public void loadCategoryList(String USER_TOKEN, String groupID, final APIUtilsCallback<List<TransactionCategory>> callback) {
        Call<List<TransactionCategory>> call = APIUtils.getInstance().getApiInterface().getCategoryList(USER_TOKEN, groupID);
        call.enqueue(new Callback<List<TransactionCategory>>() {
            @Override
            public void onResponse(Call<List<TransactionCategory>> call, Response<List<TransactionCategory>> response) {
                if (response.isSuccessful()) {
                    callback.onResponseSuccess(response.body());
                } else {
                    System.out.println("Code: " + response.code());
                    callback.onResponseFailure();
                }
            }

            @Override
            public void onFailure(Call<List<TransactionCategory>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void postNewCategory(String categoryName, String USER_TOKEN, String groupID, final APIUtilsCallback<JsonElement> callback) {
        JsonObject params = new JsonObject();
        params.addProperty("categoryName", categoryName);
        APIUtils.getInstance().getApiInterface().addCategory(params, USER_TOKEN, groupID).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    callback.onResponseSuccess(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API for new category.");
            }
        });
    }

    public void postGoal(Goal goal, String USER_TOKEN, String groupID, final APIUtilsCallback<JsonElement> callback) {
        APIUtils.getInstance().getApiInterface().addGoal(goal, USER_TOKEN, groupID).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    callback.onResponseSuccess(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API for goal.");
            }
        });
    }

}
