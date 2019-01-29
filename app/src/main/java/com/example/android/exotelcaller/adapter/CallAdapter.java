package com.example.android.exotelcaller.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.exotelcaller.R;
import com.example.android.exotelcaller.model.CallModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

//somesh@exotel.in


public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    private LayoutInflater mLayoutInfalter;
    private List<CallModel> mListCall;
    private Context mContext;


    private OnCallLogClickListener mCallback;

    public CallAdapter(Context context, List<CallModel> listCalls, OnCallLogClickListener listener) {
        mContext = context;
        mListCall = listCalls;
        mCallback = listener;

    }


    public interface OnCallLogClickListener {
        void onCallLogSelected(String number);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mLayoutInfalter = LayoutInflater.from(mContext);

        View view = mLayoutInfalter.inflate(R.layout.call_row, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        TextView numberTextView, durationTextView, dateTextView, nameTextView;
        ImageView type;

        numberTextView = viewHolder.number;
        durationTextView = viewHolder.duration;
        dateTextView = viewHolder.date;
        type = viewHolder.callType;
        nameTextView = viewHolder.name;

        String Name = mListCall.get(i).getName();
        String phoneNumber = mListCall.get(i).getNumber();
        int dur = mListCall.get(i).getDuration();
        long dates = mListCall.get(i).getDate();
        int callTypeID = mListCall.get(i).getCallType();

        Log.d("CALL", callTypeID+"");

        /*if (phoneNumber == null) {
            phoneNumber = "";
        }*/
        String formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber,"IN");
       /* if (!TextUtils.isEmpty(Name)) {

            nameTextView.setText(Name);
            numberTextView.setText(formattedNumber);

        } else {

            nameTextView.setText("Unknown number");
            numberTextView.setText(formattedNumber);

        }*/
        nameTextView.setText(Name);
        numberTextView.setText(formattedNumber);

        dateTextView.setText(DateUtils.formatSameDayTime(dates, System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.SHORT));
        durationTextView.setText(formatDuration(dur));


        int callTypeDrawableId = 0;
        switch (callTypeID) {
            case CallLog.Calls.INCOMING_TYPE:
                callTypeDrawableId = R.drawable.ic_call_received_black_24dp;
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                callTypeDrawableId = R.drawable.ic_call_made_black_24dp;
                break;
            case CallLog.Calls.MISSED_TYPE:
                callTypeDrawableId = R.drawable.ic_call_missed_black_24dp;
                break;
        }
        if (callTypeDrawableId != 0) {
            type.setImageDrawable(mContext.getResources().getDrawable(callTypeDrawableId, mContext.getTheme()));
        }


    }

    @Override
    public int getItemCount() {
        return mListCall.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView number, duration, date, name;
        ImageView callType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.contact_name);
            number = itemView.findViewById(R.id.phone_number);
            duration = itemView.findViewById(R.id.call_duration);
            date = itemView.findViewById(R.id.call_date);
            callType = itemView.findViewById(R.id.call_type_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String number = mListCall.get(getAdapterPosition()).getNumber();
            mCallback.onCallLogSelected(number);
        }
    }

    private String formatDuration(int sec) {
        Date d = new Date(sec * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(d);
        return time;
    }
}
