package com.pierre.people.fragments;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pierre.people.R;
import com.pierre.people.data.DatabaseHelper;
import com.pierre.people.data.PeopleModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewContactFragment extends Fragment {


    private static final String TAG = "NewContactFragment";
    private EditText mPhoneNumber, mName, mEmail;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    TextView heading;
    private String mSelectedImagePath;
    private int mPreviousKeyStroke;
    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public NewContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_contact_frag, container, false);


        mPhoneNumber = view.findViewById(R.id.etContactPhone);
        mName = view.findViewById(R.id.etContactName);
        mEmail = view.findViewById(R.id.etContactEmail);
        mSelectDevice = view.findViewById(R.id.selectDevice);
        toolbar = view.findViewById(R.id.editContactToolbar);
        Log.d(TAG, "onCreateView: started.");
        heading =  view.findViewById(R.id.textContactToolbar);
        heading.setText("Add new contact");
        mSelectedImagePath = null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        ImageView ivBackArrow =  view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow.");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ImageView confirmNewContact =  view.findViewById(R.id.ivCheckMark);
        confirmNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save new contact.");
                if(isTextNotEmpty(mName.getText().toString())){
                    Log.d(TAG, "onClick: saving new contact. " + mName.getText().toString() );

                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    PeopleModel contact = new PeopleModel(mName.getText().toString(),
                            mPhoneNumber.getText().toString(),
                            mSelectDevice.getSelectedItem().toString(),
                            mEmail.getText().toString());
                    if(databaseHelper.addPeople(contact)){
                        Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getActivity(), R.string.save_error, Toast.LENGTH_SHORT).show();
                    }
                    databaseHelper.close();
                }
            }
        });
//        -----------------     Phone number formant
        mPhoneNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                mPreviousKeyStroke = keyCode;
                return false;
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting contact.");
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isTextNotEmpty(String string) {
        return !string.equals("");
    }

}
