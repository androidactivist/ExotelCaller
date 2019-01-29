package com.example.android.exotelcaller.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.android.exotelcaller.R;
import com.example.android.exotelcaller.adapter.SectionsPagerAdapter;


import im.dlg.dialer.DialpadActivity;
import im.dlg.dialer.DialpadFragment;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;


import static android.Manifest.permission.CALL_PHONE;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static im.dlg.dialer.DialpadActivity.EXTRA_RESULT_FORMATTED;
import static im.dlg.dialer.DialpadActivity.EXTRA_RESULT_RAW;
import static im.dlg.dialer.DialpadActivity.EXTRA_CURSOR_VISIBLE;
import static im.dlg.dialer.DialpadActivity.EXTRA_ENABLE_PLUS;
import static im.dlg.dialer.DialpadActivity.EXTRA_ENABLE_POUND;
import static im.dlg.dialer.DialpadActivity.EXTRA_ENABLE_STAR;
import static im.dlg.dialer.DialpadActivity.EXTRA_FORMAT_AS_YOU_TYPE;
import static im.dlg.dialer.DialpadActivity.EXTRA_REGION_CODE;


public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;


    private final String TAG = MainActivity.class.getSimpleName();

    private static final int MY_PERMISSIONS_REQUEST_CONTACT_READ = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;


    private final static String EXTRA_RESULT_FORMATTED = "EXTRA_RESULT_FORMATTED";
    private final static String EXTRA_RESULT_RAW = "EXTRA_RESULT_RAW";
    private final static String EXTRA_REGION_CODE = "EXTRA_REGION_CODE";
    private final static String EXTRA_FORMAT_AS_YOU_TYPE = "EXTRA_FORMAT_AS_YOU_TYPE";
    private final static String EXTRA_ENABLE_STAR = "EXTRA_ENABLE_STAR";
    private final static String EXTRA_ENABLE_POUND = "EXTRA_ENABLE_POUND";
    private final static String EXTRA_ENABLE_PLUS = "EXTRA_ENABLE_PLUS";
    private final static String EXTRA_CURSOR_VISIBLE = "EXTRA_CURSOR_VISIBLE";

    private final static String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";

    private final static String DEFAULT_REGION_CODE = "IN"; //India


    private String regionCode = DEFAULT_REGION_CODE;
    private boolean formatAsYouType = true;
    private boolean enableStar = true;
    private boolean enablePound = true;
    private boolean enablePlus = true;
    private boolean cursorVisible = true;
    private String raw = "123";

    private static final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingSearchView searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        checkDefaultDialer();
        checkForContactPermission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, DialpadActivity.class);
                intent.putExtra(EXTRA_RESULT_RAW, raw);
                intent.putExtra(EXTRA_REGION_CODE, DEFAULT_REGION_CODE);
                startActivityForResult(intent, 100); // any result request code is ok
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100:
                Log.d(TAG, "100: onAcivityResult");

                if (resultCode == Activity.RESULT_OK) {
                    //String formatted = data.getStringExtra(DialpadActivity.EXTRA_REGION_CODE);
                    String raw = data.getStringExtra(DialpadActivity.EXTRA_RESULT_RAW);
                    Log.d(TAG, raw);
                    callNumber(raw);

                }
                break;
            case 123:
                this.checkSetDefaultDialerResult(resultCode);
            default:
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void checkForContactPermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)

                + ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)

                + ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)

                + ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "PERMISSION NOT GRANTED");

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_CALL_LOG
            }, 1);

        } else {
            //set up view
            setUpView();
            Log.d(TAG, "PERMISSION GRANTED");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if permission is granted or not for the request.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CONTACT_READ: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        setUpView();

                    } else {
                        // Permission denied.
                        Log.d(TAG, "Failure to obtain permission!");
                        Toast.makeText(this, "Failure to obtain permission!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        }


    }

    private void setUpView() {
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        CallLogFragment callLogFragment = new CallLogFragment();

        ContactFragment contactFragment = new ContactFragment();

        mSectionsPagerAdapter.addFragment(callLogFragment);
        mSectionsPagerAdapter.addFragment(contactFragment);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_access_time_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_supervisor_account_black_24dp);
    }


    private final void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT >= 23) {

            Object obj = this.getSystemService(TELECOM_SERVICE);

            if (obj == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.telecom.TelecomManager");
            } else {
                TelecomManager telecomManager = (TelecomManager) obj;
                if (this.getPackageName() != telecomManager.getDefaultDialerPackage()) {
                    Intent intent = (new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER"))
                            .putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME",
                                    this.getPackageName());
                    this.startActivityForResult(intent, 123);
                }
            }
        }
    }

    private final void checkSetDefaultDialerResult(int resultCode) {
        String str;
        switch (resultCode) {
            case -1:
                str = "User accepted request to become default dialer";
                break;
            case 0:
                str = "User declined request to become default dialer";
                break;
            default:
                str = "Unexpected result code " + resultCode;
        }

        String message = str;
        Toast.makeText((Context) this, (CharSequence) message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }


    public void callNumber(String number) {
        if(number.isEmpty()){
            Toast.makeText(this,"Phone number empty", Toast.LENGTH_SHORT).show();
        } else {
            // Use format with "tel:" and phone number to create phoneNumber.
            String phoneNumber = String.format("tel: %s", number);

            // Log the concatenated phone number for dialing.
            Log.d(TAG, "Phone Status: DIALING: " + phoneNumber);

            Toast.makeText(this,
                    "Phone Status: DIALING: " + phoneNumber,
                    Toast.LENGTH_LONG).show();

            // Create the intent.
            Intent callIntent = new Intent(Intent.ACTION_CALL);

            // Set the data for the intent as the phone number.
            callIntent.setData(Uri.parse(phoneNumber));

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "PERMISSION NOT GRANTED");

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CALL_PHONE,
                }, 2);

            } else {
                //set up view
                startActivity(callIntent);
                Log.d(TAG, "PERMISSION GRANTED");

            }
        }



    }

}