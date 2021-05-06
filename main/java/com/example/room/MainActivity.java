package com.example.room;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.room.databinding.ActivityMainBinding;
import com.example.room.databinding.CustomDilogBinding;
import com.example.room.adapter.MyAdapter;
import com.example.room.roomDB.User;
import com.example.room.roomDB.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyAdapter.onClickListener {

    MainActivity mContext;
    ActivityMainBinding binding;
    UserDatabase db;
    List<User> userList;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        db = UserDatabase.getDatabase(mContext);
        initui();
    }

    private void initui() {
        showData();
        onClickListener();
    }

    private void saveData() {
        String name = binding.etName.getText().toString().trim();
        String contact = binding.etContact.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        User user = new User(0, name, contact, email);
        db.userDuo().saveRecord(user);

    }

    private void showData() {
       userList = new ArrayList<>();
        userList.addAll(db.userDuo().readRecord());
        adapter = new MyAdapter(userList, mContext, this);
        binding.list.setLayoutManager(new LinearLayoutManager(mContext));
        binding.list.setAdapter(adapter);
    }

    private void onClickListener() {
        binding.btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnSave.getId()) {
            if (checkValidation()){
                saveData();
                showData();
                binding.etName.setText(null);
                binding.etContact.setText(null);
                binding.etEmail.setText(null);
            }

        }
    }

    private boolean checkValidation() {
        boolean isValid = true;
        if (binding.etName.getText().toString().trim().equals("")) {
            isValid = false;
            Toast.makeText(mContext, "Name Can Not Be Empty", Toast.LENGTH_SHORT).show();

        }else if (binding.etContact.getText().toString().trim().equals("")) {
            isValid = false;
            Toast.makeText(mContext, "Contact Can Not Be Empty", Toast.LENGTH_SHORT).show();

        }else if (binding.etContact.getText().toString().trim().length()!=10) {
            isValid = false;
            Toast.makeText(mContext, "Contact is not valid", Toast.LENGTH_SHORT).show();

        }else if (binding.etEmail.getText().toString().trim().equals("")) {
            isValid = false;
            Toast.makeText(mContext, "Email Can Not Be Empty", Toast.LENGTH_SHORT).show();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString().trim()).matches()) {
            isValid = false;
            Toast.makeText(mContext, "Email Is Not Valid", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    @Override
    public void onCardClick(int pos) {
        Dialog dialog = new Dialog(mContext);
        CustomDilogBinding dialogBinding = CustomDilogBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.setCancelable(false);
        dialog.show();
        dialogBinding.etNameDilog.setText(userList.get(pos).getName());
        dialogBinding.etContactDilog.setText(userList.get(pos).getContact());
        dialogBinding.etEmailDilog.setText(userList.get(pos).getEmail());

        dialogBinding.btnEdit.setOnClickListener(v -> {

            String name = dialogBinding.etNameDilog.getText().toString().trim();
            String contact = dialogBinding.etContactDilog.getText().toString().trim();
            String email = dialogBinding.etEmailDilog.getText().toString().trim();
            User realmUser1 = new User(userList.get(pos).getId(), name, contact, email);
            if (name.equals("")) {
                Toast.makeText(mContext, "Name Can Not Be Empty", Toast.LENGTH_SHORT).show();
            }else if (contact.equals("")){
                Toast.makeText(mContext, "Contact Can Not Be Empty", Toast.LENGTH_SHORT).show();
            }else if (contact.length()!=10){
                Toast.makeText(mContext, "Contact Not Valid", Toast.LENGTH_SHORT).show();
            }else if (email.equals("")) {
                Toast.makeText(mContext, "Email Can Not Be Empty", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(mContext, "Email Is Not Valid", Toast.LENGTH_SHORT).show();
            }else {
                db.userDuo().updateRecord(realmUser1);
                userList.clear();
                userList.addAll(db.userDuo().readRecord());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

        });

        dialogBinding.btnDelete.setOnClickListener(v -> {
            User user = userList.get(pos);
            db.userDuo().deleteRecord(user);
            userList.remove(pos);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialogBinding.btnCancle.setOnClickListener(v -> dialog.dismiss());
    }


}