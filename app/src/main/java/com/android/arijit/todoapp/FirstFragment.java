package com.android.arijit.todoapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.arijit.todoapp.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;

public class FirstFragment extends Fragment {
    String TAG = "First Fragment";
    private FragmentFirstBinding binding;
    private DataAccess da;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        da = new DataAccess(getContext(),VersionControl.DATABASE_VERSION);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        Log.i(TAG, "onCreateView: end");
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddTask.setOnClickListener((v -> {

            String s = binding.etNewTask.getText().toString().trim();
            if(s.equals("")){
                Snackbar.make(v, "Cannot add empty task", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            closeKeyboard();

            Log.i(TAG, "onClick: " + s);
            ToDo obj = new ToDo();
            obj.setTask(s);
            Log.i(TAG, "onViewCreated: "+obj.getCdate());
            da.addNewTodo(obj);
//            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
            Snackbar.make(v, "Added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            binding.etNewTask.setText("");
        }));
        /************
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
         ***********/

    }

    private void closeKeyboard() {
        View view = FirstFragment.this.getActivity().getCurrentFocus();
        if (view!=null){
            //getActivity() is needed since this a single fragment class and not a activity
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}