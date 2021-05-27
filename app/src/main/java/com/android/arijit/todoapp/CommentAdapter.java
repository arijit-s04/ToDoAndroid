package com.android.arijit.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private String TAG = "CommentAdapter";
    private Context context;
    private ArrayList<Comment> cmlist;
    private LayoutInflater mInflater;

    public CommentAdapter(@NonNull Context context, ArrayList<Comment> list) {
        super(context, R.layout.row_comment_layout, list);
        this.context = context;
        this.cmlist = list;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Comment obj = cmlist.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.row_comment_layout, null);
        }

        ((TextView)convertView.findViewById(R.id.tv_row_comment)).setText(obj.getCmtext());
        ((TextView)convertView.findViewById(R.id.tv_cmdate_row)).setText(obj.getCmdate());

        return convertView;
    }
}
