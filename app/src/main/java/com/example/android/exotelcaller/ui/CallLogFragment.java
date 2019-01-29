package com.example.android.exotelcaller.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.exotelcaller.R;
import com.example.android.exotelcaller.adapter.CallAdapter;
import com.example.android.exotelcaller.adapter.ContactAdapter;
import com.example.android.exotelcaller.model.CallModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;


public class CallLogFragment extends Fragment implements CallAdapter.OnCallLogClickListener{

    private View rootView;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final String[] PROJECTION = {
            CallLog.Calls._ID,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.TYPE,
            CallLog.Calls.DURATION
    };

    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_NUMBER = 2;
    private static final int COLUMN_DATE = 3;
    private static final int COLUMN_TYPE = 4;
    private static final int COLUMN_DURATION = 5;



    public CallLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_call_log, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.call_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);


        CallAdapter callAdapter = new CallAdapter(getContext(), getCallLogs(), this);
        mRecyclerView.setAdapter(callAdapter);



        return rootView;
    }

    private ArrayList<CallModel> getCallLogs() {

        ArrayList<CallModel> callModelList = new ArrayList<>();

        //checkForContactPermission();
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CALL_LOG)

                + ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.WRITE_CALL_LOG)

                != PackageManager.PERMISSION_GRANTED) {

            Log.d("CallFragment", "PERMISSION NOT GRANTED");

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG}, 1);
        }

        Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                PROJECTION,
                null,
                null,
                CallLog.Calls.DEFAULT_SORT_ORDER );


        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            //Date date1 = new Date(Long.valueOf(cursor.getString(date)));
            callModelList.add(new CallModel(
                    cursor.getString(COLUMN_NAME),
                    cursor.getInt(COLUMN_DURATION),
                    cursor.getLong(COLUMN_DATE),
                    cursor.getString(COLUMN_NUMBER),
                    cursor.getInt(COLUMN_TYPE)));
        }

        return callModelList;
    }


    @Override
    public void onCallLogSelected(String number) {
        Log.d("CallFragment", "onImageSelected" + number);
        ((MainActivity) getActivity()).callNumber(number);
    }
}