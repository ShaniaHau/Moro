package com.example.moro.Fragments.Login;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moro.Data.DAO.ProfileDAO;
import com.example.moro.Data.DTO.ProfileDTO;
import com.example.moro.Fragments.CustomFragment;
import com.example.moro.Fragments.HomeFragment;
import com.example.moro.Fragments.MainActivity;
import com.example.moro.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author s195477, Shania Hau
 */

public class MyProfile extends CustomFragment implements AdapterView.OnItemSelectedListener {

    ProfileDTO dto;
    Context ctx = Context.getInstance();
    EditText name;
    EditText bday;
    EditText email;
    TextView changeProfileInfo;
    Spinner spinner;
    ProfileDAO dao = new ProfileDAO();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button logoutProfil, deleteMyProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_min_profil, container, false);
        dto = MainActivity.getUserProfile();
        spinner = myView.findViewById(R.id.minProfilSpinner);

        name = myView.findViewById(R.id.minProfilNavn);
        bday = myView.findViewById(R.id.minProfilFoeds);
        email = myView.findViewById(R.id.minProfilEmail);

        name.setText(dto.getProfileUsername());
        bday.setText(dto.getProfileDateBorn());
        email.setText(dto.getProfileEmail());

        changeProfileInfo = myView.findViewById(R.id.changeProfileInfo);
        changeProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hvad hvis internet fejler?
                editInfo(name.getText().toString(), spinner.getSelectedItem().toString(), email.getText().toString(), bday.getText().toString());
            }
        });
        deleteMyProfile = myView.findViewById(R.id.deleteMyProfile);
        deleteMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProfileDAO().deleteUser();
                ctx.setState(new NotLoginState());
                replaceFragment(new HomeFragment());
            }
        });

        /**
         * @author s174130, Jacob Christensen
         */
        logoutProfil = myView.findViewById(R.id.logoutProfil);
        logoutProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                ctx.setState(new NotLoginState());
                replaceFragment(new HomeFragment());
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.genderList, android.R.layout.simple_spinner_item);

        //Apply adapter to the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(adapter.getPosition(dto.getProfileGender()));

        return myView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String genderTxt = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * @author s195477, Shania Hau
     */
    public void editInfo(String name, String gender, String mail, String bday) {
        //gets the user from context
        try {
            dto.setProfileUsername(name);
            dto.setProfileGender(gender);
            dto.setProfileEmail(mail);
            dto.setProfileDateBorn(bday);
            dao.updateUser(mAuth.getUid(), dto);
            Toast.makeText(getContext(), "Gemt", Toast.LENGTH_SHORT).show();

            //Update database
            //profileDAO.saveProfile(profileDTO);
        }catch (Exception exception){
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}