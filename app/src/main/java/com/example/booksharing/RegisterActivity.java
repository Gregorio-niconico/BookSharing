package com.example.booksharing;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.booksharing.database.user_info;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksharing.R;

import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_username;
    private EditText edit_pwd;
    private EditText edit_checkpwd;
    private String username,pwd,checkpwd;
    private static final String TAG="RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        LitePal.getDatabase();
        user_info u=new user_info();
        Button returnLogin=(Button)findViewById(R.id.return_login);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        edit_checkpwd = (EditText) findViewById(R.id.edit_checkpwd);
        Button confirmButton = (Button) findViewById(R.id.button_confirm);
        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        returnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        getEditString();
        switch (v.getId()) {
            case R.id.button_confirm:
                if(!username.isEmpty()) {
                    List<user_info> userInfo = LitePal.where("username like ?", username)
                            .find(user_info.class);
                    //Log.d("RegisterActivity", userInfo.get(0).getUsername());
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
            case R.id.return_login:
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    //获取输入框控件内容
    public void getEditString(){
        username=edit_username.getText().toString();
        pwd=edit_pwd.getText().toString();
        checkpwd=edit_checkpwd.getText().toString();
    }

    //清空输入框
    public void cleanEditText(){
        edit_username.setText("");
        edit_pwd.setText("");
        edit_checkpwd.setText("");
    }
}