package com.android.arijit.todoapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.arijit.todoapp.databinding.FragmentSecondBinding;
import com.android.arijit.todoapp.databinding.FragmentThirdBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ThirdFragment extends Fragment {

    private @NonNull FragmentThirdBinding binding;
    private String TAG = "Third Fragment";
    private int tid;
    private ListView lv_comments;
    private DataAccess da;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> cmlists;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        tid = getArguments().getInt("tid");
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        da = new DataAccess(getContext(), VersionControl.DATABASE_VERSION);
//        cmlists = new ArrayList<>();
        cmlists = da.getAllComment(tid);
        lv_comments = binding.lvThirdFragment;

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvHeaderFragmentThird.setText(da.fetchParticularTask(tid));
        if(cmlists!= null && cmlists.size()>0){
            binding.tvNoCommentThirdFragment.setVisibility(View.INVISIBLE);
            commentAdapter = new CommentAdapter(getContext(), cmlists);
            lv_comments.setAdapter(commentAdapter);
        }
//        else
//            Snackbar.make(view, "No comments", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

        binding.fabCommentThird.setOnClickListener((v)->{
            String s = binding.etCommentThirdFragment.getText().toString().trim();
            binding.etCommentThirdFragment.setText("");
            if(s.equals("")){
                Snackbar.make(v, "Cannot be empty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            closeKeyboard();
            Comment cmObj = new Comment();
            cmObj.setCmtext(s);
            cmObj.setCid(da.addNewComments(tid, cmObj));
            cmlists.add(cmObj);

            if(commentAdapter==null) {
                binding.tvNoCommentThirdFragment.setVisibility(View.INVISIBLE);
                commentAdapter = new CommentAdapter(getContext(), cmlists);
                lv_comments.setAdapter(commentAdapter);
            }
            else
                commentAdapter.notifyDataSetChanged();
            Snackbar.make(v, "Comment Added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        registerForContextMenu(binding.lvThirdFragment);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable  ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.listview_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.lv_menu_delete:
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Comment delCmnt = commentAdapter.getItem(info.position);
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
                                da.deleteComment(delCmnt.getCid());
                                commentAdapter.remove(delCmnt);
                                if(commentAdapter.isEmpty())
                                    binding.tvNoCommentThirdFragment.setVisibility(View.VISIBLE);
                                Snackbar.make(getView(), "Deleted", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        })
                        .create().show();
                return true;
            case R.id.lv_menu_copy:
                ClipboardManager clipboardManager = (ClipboardManager)getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                String clip = commentAdapter.getItem(
                        ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo())
                                .position).getCmtext();
                ClipData clipData = ClipData.newPlainText("comment", clip);
                clipboardManager.setPrimaryClip(clipData);
                Snackbar.make(getView(), "Copied", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void closeKeyboard() {
        View view = ThirdFragment.this.getActivity().getCurrentFocus();
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