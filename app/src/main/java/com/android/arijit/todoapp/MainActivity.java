package com.android.arijit.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ToDoAdapter toDoAdapter;
    private ArrayList<ToDo> list;
    private ListView lvtasks;

    private void setList(){
        list.add(new ToDo(1,"Task 1", true));
        list.add(new ToDo(2,"Complete H/W"));
        list.add(new ToDo(3, "Cal her"));
        list.add(new ToDo(4,"Eat Pizza", true));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<ToDo>();
        setList();
        toDoAdapter = new ToDoAdapter(this, list);
        lvtasks = (ListView) findViewById(R.id.lvtask);
        lvtasks.setAdapter(toDoAdapter);
    }
}