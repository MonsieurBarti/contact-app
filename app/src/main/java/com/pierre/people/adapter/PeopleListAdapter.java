package com.pierre.people.adapter;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pierre.people.MainActivity;
import com.pierre.people.R;
import com.pierre.people.data.PeopleModel;
import com.sas_apps.contactdialog.ContactDialog;
import com.sas_apps.contactdialog.ContactDialogBuilder;
import com.sas_apps.contactdialog.OnDialogClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PeopleListAdapter extends ArrayAdapter<PeopleModel> {

    private static final String TAG = "PeopleListAdapter";
    private LayoutInflater mInflater;
    private List<PeopleModel> mContacts = null;
    private ArrayList<PeopleModel> arrayList;
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public PeopleListAdapter(@NonNull Context context, @LayoutRes int resource,
                             @NonNull List<PeopleModel> contacts, String append) {
        super(context, resource, contacts);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mContacts = contacts;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mContacts);
    }

    private static class ViewHolder {
        TextView name;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.name = convertView.findViewById(R.id.text_list_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String name = getItem(position).getName();
        viewHolder.name.setText(name);

        return convertView;
    }

    public void filter(String characterText) {
        characterText = characterText.toLowerCase(Locale.getDefault());
        mContacts.clear();
        if (characterText.length() == 0) {
            mContacts.addAll(arrayList);
        } else {
            mContacts.clear();
            for (PeopleModel contact : arrayList) {
                if (contact.getName().toLowerCase(Locale.getDefault()).contains(characterText)) {
                    mContacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }
}
