package com.localbay.anuragsingh.buyer;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.localbay.anuragsingh.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerEditProfileFragment extends Fragment {

    Button update_buyer_profile_button;
    EditText fullnameInput;
    String fullname;
    String gender;
    RadioGroup genderWrapper;
    RadioButton selectedGender;

    RadioButton male;
    RadioButton female;

    View view;
    ProgressBar progressBar;

    ParseUser buyer;
    Fragment fragment;
    FragmentManager fm;

    public BuyerEditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buyer_edit_profile, container, false);
        buyer = ParseUser.getCurrentUser();

        fullnameInput = (EditText) view.findViewById(R.id.user_name);
        genderWrapper = (RadioGroup) view.findViewById(R.id.genderWrapper);

        male = (RadioButton) view.findViewById(R.id.maleGender);
        female = (RadioButton) view.findViewById(R.id.femaleGender);

        /**
         * SETTING SAVED VALUES
         */
        fullnameInput.setText(buyer.getString("fullname"));
        if (buyer.get("gender") != null && buyer.get("gender").equals("Female")) {
            female.setChecked(true);
            male.setChecked(false);
        }
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        fm = getFragmentManager();


        /**
         * UPDATE BUYER PROFILE
         * FULLNAME AND GENDER
         */

        update_buyer_profile_button = (Button) view.findViewById(R.id.update_buyer_profile_button);
        update_buyer_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = fullnameInput.getText().toString();
                selectedGender = (RadioButton) view.findViewById(genderWrapper.getCheckedRadioButtonId());
                gender = selectedGender.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                Boolean flag = false;
                if (gender == null) flag = true;

                if (!flag) {

                    buyer.put("fullname", fullname);
                    buyer.put("gender", gender);
                    System.out.println("GENDER " + gender);
                    buyer.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            progressBar.setVisibility(View.GONE);
                            if (e == null) {
                                Toast.makeText(getActivity().getBaseContext(), "Saved", Toast.LENGTH_LONG);

                                fragment = new BuyerAccountFragment();
                                fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("Buyer Account").commit();
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "Error, Please Try Again", Toast.LENGTH_LONG);
                            }
                        }
                    });

                }

            }
        });


        return view;
    }

}
