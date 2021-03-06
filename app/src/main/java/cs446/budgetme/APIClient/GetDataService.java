package cs446.budgetme.APIClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Group;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;
import cs446.budgetme.Model.User;
import cs446.budgetme.RegistrationActivity;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetDataService {

    //-------------------------TRANSACTION-----------------------------------
    //TODO:need to change the api url
    @GET("/trans/{user_token}/{group_id}")
    Call<List<Transaction>> getTransactionList(@Path("user_token") String token, @Path("group_id") String groupId );

    @POST("/trans/{user_token}/{group_id}")
    Call<JsonElement> addTransaction(@Body Transaction trans, @Path("user_token") String token, @Path("group_id") String groupId );

    @POST("/pretrans/{user_token}/{group_id}/{trans_id}")
    Call<Transaction> preTransaction(@Body Transaction trans, @Path("user_token") String token, @Path("group_id") String groupId, @Path("trans_id") String transId);

    @DELETE("/trans/{user_token}/{group_id}/{transId}")
    Call<ResponseBody> deleteTransaction(@Path("user_token") String token, @Path("group_id") String group_id, @Path("transId") String transId );

    //-------------------------GOAL-----------------------------------------
    @GET("/goal/{user_token}/{group_id}")
    Call<List<Goal>> getGoalList(@Path("user_token") String token,  @Path("group_id") String groupId );

    @POST("/goal/{user_token}/{group_id}")
    Call<JsonElement> addGoal(@Body Goal mgoal, @Path("user_token") String token,  @Path("group_id") String groupId );

    @DELETE("/goal/{user_token}/{group_id}/{goal_id}")
    Call<ResponseBody> deleteGoal(@Path("user_token") String token, @Path("group_id") String groupId,  @Path("goal_id") String goalId );

    //---------------------CATEGORY------------------------------------
    @GET ("/category/{user_token}/{group_id}")
    Call<List<TransactionCategory>> getCategoryList(@Path("user_token") String token,  @Path("group_id") String groupId );

    @POST("/category/{user_token}/{group_id}")
    Call<JsonElement> addCategory(@Body JsonObject categoryObject, @Path("user_token") String token, @Path("group_id") String groupId );

    @DELETE("/category/{user_token}/{group_id}/{categoryUserId}")
    Call<ResponseBody> deleteCategory(@Path("user_token") String token, @Path("group_id") String groupId,  @Path("categoryUserId") String categoryId );

    //----------------------USER--------------------------------------

    @GET("/user/{user_email}/{password}")
    Call<User> getUser(@Path("user_email") String email,@Path("password") String password);

    @POST("/user")
    Call<ResponseBody> registerAccount(@Body APIUtils.RegisterRequest request);

    //----------------------GROUP----------------------------------------------
    @POST("/group/{user_token}")
    Call<JsonElement> createGroup(@Body APIUtils.CreateGroupRequest request, @Path("user_token") String token);

    @POST("/group/{user_token}")
    Call<Void> joinGroup(@Body JsonObject object, @Path("user_token") String token);

    @GET("/group/{user_token}")
    Call<List<Group>> getGroupList(@Path("user_token") String token);

}
