package cs446.budgetme.APIClient;

import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cs446.budgetme.Model.User;
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


}
