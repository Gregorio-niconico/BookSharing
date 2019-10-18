package com.example.booksharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksharing.Http.HttpCallbackListener;
import com.example.booksharing.Http.HttpRequest;
import com.example.booksharing.database.book_GSON;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG="MainActivity";
    private DrawerLayout mDrawerLayout;
    private Button sendRequest;
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_register);
        LitePal.getDatabase();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar=getSupportActionBar();
        navigationView.setCheckedItem(R.id.nav_collection);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.nav_decode){
                    onScanBarcode(menuItem);
                }
                return true;

            }
        });
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.users);
        }
        responseText = findViewById(R.id.response_text);
        sendRequest = findViewById(R.id.send_request);
    }
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id){
//            case R.id.send_request:
////                sendRequestWithOkHttp();
//                HttpRequest httpRequest=new HttpRequest();
//                httpRequest.sendHttpRequest(address, new HttpCallbackListener() {
//                    @Override
//                    public void onFinish(String response) {
//                        Log.d(TAG, "onFinish: ");
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + result.getContents()
                        , Toast.LENGTH_LONG).show();
                final  String url="https://isbn.szmesoft.com/isbn/query?isbn="+result.getContents();
                sendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpRequest httpRequest=new HttpRequest();
                        httpRequest.sendHttpRequest(url, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                Log.d(TAG, "url"+response);
                                Log.d(TAG, "onFinish: "+response);
                                parseJSONWithJSONObject(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.d(TAG, "onError: "+e);
                            }
                        });
                    }
                });
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private  void parseJSONWithGSON(String jsonData){
        Gson gson=new Gson();
        book_GSON bookGson=gson.fromJson(jsonData,book_GSON.class);
        Log.d(TAG, "ISBN is "+bookGson.getISBN());
        Log.d(TAG, "Author is "+bookGson.getAuthor());
    }

    private void parseJSONWithJSONObject(String jsonData){
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(jsonData);
            String ISBN=jsonObject.getString("ISBN");
            Log.d(TAG, "parseJSONWithJSONObject: "+ISBN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onScanBarcode(MenuItem menuItem){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("扫描条形码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public   void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在这里做UI操作，将结果显示到界面上
                responseText.setText(response);
            }
        });
    }


    //加载toolbar.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //处理按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeIcon:
                Toast.makeText(this,"This is Home Button",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"This is Home Button",Toast.LENGTH_SHORT).show();
                break;
            //HomeAsUp按钮默认值都是android.R.id.home
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
