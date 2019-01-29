package com.example.android.exotelcaller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.exotelcaller.R;
import com.example.android.exotelcaller.model.ContactModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private LayoutInflater mLayoutInfalter;
    private List<ContactModel> mListContacts;
    private Context mContext;
    private OnContactClickListener mCallback;

    public ContactAdapter(Context context, List<ContactModel> listContacts, OnContactClickListener listener) {
        mListContacts = listContacts;
        mContext = context;
        mCallback = listener;

    }

    public interface OnContactClickListener {
        void onContactSelected(String number);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mLayoutInfalter = LayoutInflater.from(mContext);

        View view = mLayoutInfalter.inflate(R.layout.contact_row, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        TextView contact_name, contact_phone_number;

        contact_name = viewHolder.contact_name;
        contact_phone_number = viewHolder.contact_phone_number;

        contact_name.setText(mListContacts.get(position).getName());
        contact_phone_number.setText(mListContacts.get(position).getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return mListContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView contact_name, contact_phone_number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contact_name = itemView.findViewById(R.id.contact_name);
            contact_phone_number = itemView.findViewById(R.id.contact_phone_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String number = mListContacts.get(getAdapterPosition()).getPhoneNumber();
            mCallback.onContactSelected(number);

        }
    }



}
