package com.pierre.people.fragments;


import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pierre.people.R;
import com.pierre.people.data.DatabaseHelper;
import com.pierre.people.data.PeopleModel;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
    private static final String TAG = "EditFragment";
    ImageView imageBack, imageDone, imageCamera;
    EditText etName, etNumber, etEmail;
    Toolbar toolbar;
    private String mSelectedImagePath;
    PeopleModel mPeopleModel;
    private Spinner mSelectDevice;


    public static final String[] PHOTO_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.CAMERA};

    public EditFragment() {
        super();
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_frag, container, false);
        imageBack = view.findViewById(R.id.image_back_edit);
        imageDone = view.findViewById(R.id.image_done);
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        mSelectDevice = view.findViewById(R.id.selectDevice);
        etNumber = view.findViewById(R.id.et_number);
        toolbar = view.findViewById(R.id.toolbar_edit);
        mPeopleModel = getContactFromBundle();
        if (mPeopleModel != null) {
            setContent(mPeopleModel);
        }
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        imageDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: saving the edited contact.");
                if (checkStringIfNull(etName.getText().toString())) {
                    Log.d(TAG, "onClick: saving changes to the contact: " + etName.getText().toString());

                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    Cursor cursor = databaseHelper.getPeopleID(mPeopleModel);

                    int contactID = -1;
                    while (cursor.moveToNext()) {
                        contactID = cursor.getInt(0);
                    }
                    if (contactID > -1) {
                        mPeopleModel.setName(etName.getText().toString());
                        mPeopleModel.setphoneNumber(etNumber.getText().toString());
                        mPeopleModel.setDevice("Mobile");
                        mPeopleModel.setEmail(etEmail.getText().toString());

                        databaseHelper.updatePeople(mPeopleModel, contactID);
                        Toast.makeText(getActivity(), "Contact Updated", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    databaseHelper.close();
                }
            }
        });


        return view;
    }

    private void setContent(PeopleModel people) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.contact_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etName.setText(people.getName());
        etNumber.setText(people.getphoneNumber());
        etEmail.setText(people.getEmail());
    }

    private PeopleModel getContactFromBundle() {
        Log.d(TAG, "getContactFromBundle: arguments: " + getArguments());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.key));
        } else {
            return null;
        }
    }


    public void getBitmapImage(Bitmap bitmap) {
        Log.d(TAG, "getBitmapImage: got the bitmap: " + bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkStringIfNull(String string) {
        return !string.equals("");
    }

    public Bitmap compressBitmap(Bitmap bitmap, int quality) {
        Bitmap imageBitmap = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return imageBitmap;
    }
}