package cs446.budgetme.Fragement;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.DashboardActivity;
import cs446.budgetme.Model.Group;
import cs446.budgetme.Model.User;
import cs446.budgetme.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView userGroupListView;
    private TextView usernameText;
    private ArrayAdapter<String> arrayAdapter;
    private Button goalButton;
    private Button joinGroupButton;
    private Button createGroupButton;
    private User mUser;
    private AlertDialog mJoinGroupDialog;
    private APIUtils apicall = new APIUtils();
    private static final String TAG = DashboardProfileFragment.class.getName();


    private ArrayList<String> userGroupList;

    public DashboardProfileFragment() {
        // Required empty public constructor
    }

    public DashboardProfileFragment(User user) {
        mUser = user;
        // Required empty public constructor
    }

    public static DashboardProfileFragment newInstance() {
        DashboardProfileFragment fragment = new DashboardProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userGroupList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Create the views needed
        usernameText= getView().findViewById(R.id.UserName);
        usernameText.setText(mUser.getName());

        // userAvatar = getView().findViewById(R.id.UserAvatar);
        createGroupButton = getView().findViewById(R.id.CreateGroupButton);
        joinGroupButton = getView().findViewById(R.id.JoinGroupButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Join a new group. Enter its ID");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_input, null);
        final EditText newCategoryInput = viewInflated.findViewById(R.id.input_new_category);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupId = newCategoryInput.getText().toString();
                if (groupId.length() > 0) {
                    postJoinGroup(groupId, dialog);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        mJoinGroupDialog = builder.create();

        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mJoinGroupDialog.show();
            }
        });


        goalButton = getView().findViewById(R.id.CreateGoal);
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((DashboardActivity)getActivity()).startGoalSetting();
                } catch (Exception e) {

                }
            }
        });

        //create the group list
        arrayAdapter =  new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, userGroupList);
        userGroupListView = getView().findViewById(R.id.GroupList);
        updateGroupList(mUser.getGroupList());
        userGroupListView.setSelection(0);
        userGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                //when group is selected then refresh all the user value
                ((DashboardActivity)getActivity()).updateUserDefaultGroup(position);
            }
        });
    }

    private void postJoinGroup(String groupId, final DialogInterface dialog) {
        JsonObject params = new JsonObject();
        params.addProperty("groupName", groupId);
        apicall.getApiInterface().joinGroup(params, mUser.getUserAuthToken()).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    dialog.dismiss();
                    //updateGroupList(); TODO REPLACE WITH API CALL
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    private void createGroup(){

    }
    public void setDefaultGroup(String id){
        mUser.setDefaultGroupId(id);
        updateGroupList(mUser.getGroupList());
    }
    private void updateGroupList(ArrayList<Group> groups){
        userGroupList.clear();
        if(groups != null) {
            for (Group g : groups) {
                userGroupList.add(g.getGroupName());
            }
            userGroupListView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            userGroupListView.invalidate();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
