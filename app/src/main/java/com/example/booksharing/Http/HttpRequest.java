package com.example.booksharing.Http;

import android.util.Log;

import com.example.booksharing.MainActivity;
import com.example.booksharing.database.book_GSON;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequest {
    private static final String TAG="MainActivity";

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("https://www.baidu.com").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                   // Log.d(TAG, "run: "+responseData);
                   // showResponse(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendHttpRequest(final String address,
                                                 final HttpCallbackListener listener) {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //获取输入流
                    InputStream in = connection.getInputStream();

                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        sb.append(line);
                    }if(listener!=null){
                        //回调OnFinish()方法
                        listener.onFinish(sb.toString());
                    }
//                    MainActivity mainActivity=new MainActivity();
//                    mainActivity.showResponse(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (reader!= null ){
                        try {
                            //关闭bufferreader
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null){
                        //关闭这个HTTP连接
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

//    public static void parseJSONWithGSON(String jsonData){
//        Gson gson=new Gson();
//        book_GSON bookGson=gson.fromJson(jsonData,book_GSON.class);
//        Log.d(TAG, "ISBN is "+bookGson.getISBN());
//        Log.d(TAG, "Author is "+bookGson.getAuthor());
//    }

}
