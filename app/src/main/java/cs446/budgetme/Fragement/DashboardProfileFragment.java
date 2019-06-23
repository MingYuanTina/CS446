package cs446.budgetme.Fragement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import cs446.budgetme.DashboardActivity;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;
import cs446.budgetme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERNAME = "username";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String username;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView userGroupListView;
    private TextView usernameText;
    private ArrayAdapter<String> arrayAdapter;
    private Button goalButton;


    private ArrayList<String> userGroupList;

    public DashboardProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardProfileFragment newInstance(String param1, String param2) {
        DashboardProfileFragment fragment = new DashboardProfileFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userGroupList = new ArrayList<String>();
        userGroupList.add("Group1");
        userGroupList.add("Group2");

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
        usernameText.setText(username);

         // userTotalAmmount = getView().findViewById(R.id.UserTotalAmount);
        // userAvatar = getView().findViewById(R.id.UserAvatar);
        // addGroupButton = getView().findViewById(R.id.AddGroupButton);

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
        updateGroupList();
    }

    private void updateGroupList(){
        arrayAdapter.notifyDataSetChanged();
        userGroupListView.setAdapter(arrayAdapter);
        userGroupListView.invalidate();
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
