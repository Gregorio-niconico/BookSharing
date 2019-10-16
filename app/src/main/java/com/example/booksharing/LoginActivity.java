package com.example.booksharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksharing.database.user_info;
import com.google.android.material.snackbar.Snackbar;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase  db;
    private EditText et_username;
    private EditText et_pwd;
    private String username,pwd;
    private List<user_info> userInfo;
    private static final String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LitePal.getDatabase();
        et_username = (EditText) findViewById(R.id.et_username);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        Button loginButton = (Button) findViewById(R.id.button_login);
        Button registerButton = (Button) findViewById(R.id.button_register);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getEditString();
        switch (v.getId()){
            case R.id.button_login:
                if(!username.isEmpty()) {
                    userInfo = LitePal.where("username=?", username)
                            .find(user_info.class);
                    if (!userInfo.get(0).getPassword().isEmpty()) {
                        Log.d(TAG, userInfo.get(0).getPassword());
                        if(!pwd.isEmpty()) {
                            if (userInfo.get(0).getPassword().equals(pwd)) {
                                cleanEditText();
                                Snackbar.make(v,"登录成功！",Snackbar.LENGTH_SHORT).show();
//                                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent("com.example.booksharing.mainactivity");
                                startActivity(intent);

                            } else {
                                cleanEditText();
                                Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                                Toast.makeText(this, "你还没有输入密码呢！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        cleanEditText();
                        Toast.makeText(this, "用户不存在！请先注册", Toast.LENGTH_SHORT).show();
                    }
                }else{
                        Toast.makeText(this, "你还没有输入用户名呢", Toast.LENGTH_SHORT).show();
                }
            break;
            case R.id.button_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    //获取输入框控件内容
    public void getEditString(){
        username=et_username.getText().toString();
        pwd=et_pwd.getText().toString();
    }

    //清空输入框
    public void cleanEditText(){
        et_username.setText("");
        et_pwd.setText("");
    }
}
