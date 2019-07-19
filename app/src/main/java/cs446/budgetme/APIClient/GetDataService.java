package cs446.budgetme.APIClient;

import com.google.gson.JsonElement;

import java.util.List;

import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;
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

    @DELETE("/trans/{user_token}/{transaction_token}/{group_id}")
    Call<ResponseBody> deleteTransaction(@Path("user_token") String token, @Path("group_id") String groupId,  @Path("goal_Id") String goalId );

    //-------------------------GOAL-----------------------------------------
    @GET("/goal/{user_token}/{group_id}")
    Call<List<Goal>> getGoalList(@Path("user_token") String token,  @Path("group_id") String groupId );

    @POST("/goal/{user_token}/{group_id}")
    Call<JsonElement> addGoal(@Body Goal mgoal, @Path("user_token") String token,  @Path("group_id") String groupId );

    @DELETE("/goal/{user_token}/{group_id}/{goal_Id}")
    Call<ResponseBody> deleteGoal(@Path("user_token") String token, @Path("group_id") String groupId,  @Path("goal_Id") String goalId );

    //---------------------CATEGORY------------------------------------
    @GET ("/category/{user_token}/{group_id}")
    Call<List<TransactionCategory>> getCategoryList(@Path("user_token") String token,  @Path("group_id") String groupId );

    @POST("/category/{user_token}/{group_id}")
    Call<Goal> addCategoryList(@Body TransactionCategory mCategory, @Path("user_token") String token,  @Path("group_id") String groupId );

    @DELETE("/category/{user_token}/{group_id}/{categoryUserId}")
    Call<ResponseBody> deleteCategory(@Path("user_token") String token, @Path("group_id") String groupId,  @Path("goal_Id") String goalId );

    //----------------------USER--------------------------------------



    //----------------------GROUP----------------------------------------------


}
