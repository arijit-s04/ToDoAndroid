package com.android.arijit.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ToDoAdapter extends ArrayAdapter<ToDo> {
    private Context context;
    private ArrayList<ToDo> todoList;
    private LayoutInflater mInflater;

    public ToDoAdapter(@NonNull Context context, ArrayList<ToDo> list) {
        super(context, R.layout.row_layout, list);
        this.context = context;
        this.todoList = list;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToDo todoobj = todoList.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.row_layout, null);
        }
        ((TextView)(convertView.findViewById(R.id.tv_lvtask))).setText(todoobj.getTask());
        if(todoobj.isCompleted()){
            ((TextView) convertView.findViewById(R.id.tv_lvtask))
                    .setPaintFlags(((TextView) convertView.findViewById(R.id.tv_lvtask)).getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        ((CheckBox)(convertView.findViewById(R.id.cb_lvcompleted))).setChecked(todoobj.isCompleted());
        ((TextView)(convertView.findViewById(R.id.tv_lvdate))).setText(todoobj.getCdate());

        View finalConvertView = convertView;
        ((CheckBox)(convertView.findViewById(R.id.cb_lvcompleted)))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            ((TextView) finalConvertView.findViewById(R.id.tv_lvtask))
                                    .setPaintFlags(((TextView) finalConvertView.findViewById(R.id.tv_lvtask)).getPaintFlags()
                                            | Paint.STRIKE_THRU_TEXT_FLAG);
                        }
                        else {
                            ((TextView) finalConvertView.findViewById(R.id.tv_lvtask))
                                    .setPaintFlags(((TextView) finalConvertView.findViewById(R.id.tv_lvtask)).getPaintFlags()
                                            & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        }
                        DataAccess da = new DataAccess(getContext(), VersionControl.DATABASE_VERSION);
                        da.mDaCompleteUpdate(todoList.get(position), isChecked);
                    }
                });

        return convertView;
    }

}
