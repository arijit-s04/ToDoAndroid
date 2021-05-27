package com.android.arijit.todoapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.arijit.todoapp.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private String TAG = "Second Fragment";
    //copied from MainActivity
    //******************************
    private DataAccess da;
    private ToDoAdapter toDoAdapter;
    private ArrayList<ToDo> list;
    private ListView lvtasks;
    //******************************
    //up to here

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        da = new DataAccess(getContext(), VersionControl.DATABASE_VERSION);

        //copied from MainActivity
        //******************
        list = new ArrayList<ToDo>();
        list = da.getAllTodo();
        lvtasks = binding.lvFragmentSecond;
        //******************
        //up to here

        binding.fab.getScaleY();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(list != null && list.size()>0) {
            binding.tvNoTaskSecondFragment.setVisibility(View.INVISIBLE);
            toDoAdapter = new ToDoAdapter(getContext(), list);
            lvtasks.setAdapter(toDoAdapter);
        }
//        else {
//            Snackbar.make(view, "No task to show", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });


        binding.lvFragmentSecond.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //scrolling down --> show
                if(lastFirstVisibleItem > firstVisibleItem && !binding.fab.isShown()){
                    binding.fab.show();
                }
                //scroll up --> hide
                if(lastFirstVisibleItem < firstVisibleItem && binding.fab.isShown()) {
                    binding.fab.hide();
                }

                lastFirstVisibleItem = firstVisibleItem;
            }
        });
        

        binding.lvFragmentSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bdl = new Bundle();
                bdl.putInt("tid", list.get(position).getId());

                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment, bdl);
            }
        });

        registerForContextMenu(binding.lvFragmentSecond);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.listview_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.lv_menu_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                ToDo delTodo = (ToDo) toDoAdapter.getItem(info.position);
                new AlertDialog.Builder(getActivity())
                        .setMessage("Delete this task?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                da.deleteTask(delTodo.getId());
                                toDoAdapter.remove(delTodo);
                                if(toDoAdapter.isEmpty())
                                    binding.tvNoTaskSecondFragment.setVisibility(View.VISIBLE);
                                Snackbar.make(getView(), "Deleted", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            }
                        })
                        .create().show();
                return true;
            case R.id.lv_menu_copy:
                ClipboardManager clipboardManager = (ClipboardManager)getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                String clip = toDoAdapter.getItem(
                        ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo())
                                .position).getTask();
                ClipData clipData = ClipData.newPlainText("task", clip);
                clipboardManager.setPrimaryClip(clipData);
                Snackbar.make(getView(), "Copied", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}