package com.example.booksharing;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.booksharing.database.user_info;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksharing.R;

import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_pwd;
    private EditText et_checkpwd;
    private SQLiteDatabase db;
    private List<user_info> userInfo;
    private String username,pwd,checkpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = LitePal.getDatabase();
        et_username = (EditText) findViewById(R.id.edit_username);
        et_pwd = (EditText) findViewById(R.id.edit_pwd);
        et_checkpwd = (EditText) findViewById(R.id.edit_checkpwd);
        Button confirmButton = (Button) findViewById(R.id.button_confirm);
        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        getEditString();
        switch (v.getId()) {
            case R.id.button_confirm:
                if(!username.isEmpty()) {
                    userInfo = LitePal.where("username=?", username)
                            .find(user_info.class);
                    if (userInfo.isEmpty()) {
                         if (!pwd.isEmpty()&&!checkpwd.isEmpty()) {
                             if (!pwd.equals(checkpwd)) {
                                 cleanEditText();
                                 Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                             } else {
                                 user_info u = new user_info();
                                 u.setUsername(username);
                                 u.setPassword(pwd);
                                 u.save();
                                 cleanEditText();
                                 Toast.makeText(this, "注册成功！:)", Toast.LENGTH_SHORT).show();
                             }
                         }else{
                             cleanEditText();
                             Toast.makeText(this,"密码不能为空哦!",Toast.LENGTH_SHORT).show();
                         }
                    } else {
                        cleanEditText();
                        Toast.makeText(this, "用户名已经存在:(", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"用户名不能为空哦0.0",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_cancel:
                cleanEditText();
                break;
        }

    }

    //获取输入框控件内容
    private void getEditString(){
        username=et_username.getText().toString();
        pwd=et_pwd.getText().toString();
        checkpwd=et_checkpwd.getText().toString();
    }

    //清空输入框
    public void cleanEditText(){
        et_username.setText("");
        et_pwd.setText("");
        et_checkpwd.setText("");
    }
}