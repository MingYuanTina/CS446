package cs446.budgetme.APIClient;

public class APIUtils {
    private GetDataService apiInterface;
    public APIUtils(){
        RetrofitClient retrofit = new RetrofitClient();
        apiInterface= retrofit.getRetrofitClient().create(GetDataService.class);
    }
    public GetDataService getApiInterface(){
        return apiInterface;
    }


}
