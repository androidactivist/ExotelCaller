package com.example.android.exotelcaller.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.exotelcaller.R;
import com.example.android.exotelcaller.adapter.ContactAdapter;
import com.example.android.exotelcaller.model.CallModel;
import com.example.android.exotelcaller.model.ContactModel;


import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment implements ContactAdapter.OnContactClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int MY_PERMISSIONS_REQUEST_CONTACT_READ = 1;
    private final String TAG = "ContactFragment";
    private List<ContactModel> mListContact;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.contact_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ContactAdapter contactAdapter = new ContactAdapter(getContext(), getContacts(), this);
        mRecyclerView.setAdapter(contactAdapter);



        return rootView;
    }

    private List<ContactModel> getContacts(){

        List<ContactModel> list = new ArrayList<>();

        Cursor cursor = getContext().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,null,null,
                        ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        cursor.moveToFirst();

        while(cursor.moveToNext()) {

            list.add(new ContactModel(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
        }
        return list;
    }


    @Override
    public void onContactSelected(String number) {
        Log.d("CallFragment", "onImageSelected" + number);
        ((MainActivity) getActivity()).callNumber(number);
    }
}