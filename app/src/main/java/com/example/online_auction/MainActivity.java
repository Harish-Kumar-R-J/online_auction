package com.example.online_auction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText main_email, main_pwd;
    Button main_btn, main_create_acc;
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> password = new ArrayList<>();

    public static final String url= "https://script.google.com/macros/s/AKfycbw4yzWKRFxXDaLbFhmj8on3BjwHX3RxKxImXZlRbiZJHOClpmvH-jCMSq-PrJPLlhuoxA/exec?";
    public static com.squareup.okhttp.Response response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("bider", MODE_PRIVATE);
        main_email = findViewById(R.id.main_email);
        main_pwd = findViewById(R.id.main_pwd);
        main_btn = findViewById(R.id.main_btn);
        main_create_acc = findViewById(R.id.main_create_acc);
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject db;
                JSONArray array = null;
                int found = 0;
                try {
                    db = readAllData("Sheet1");

                    System.out.println("ppppppp " + db);
                    assert db != null;
                    array = db.getJSONArray("records");
                }catch (IOException | JSONException e) {
                        System.out.println(e + "llooooooggggggg");
                    }
                    for(int i=0;i<array.length();i++)
                    {try
                    {if (array.getJSONObject(i).get("email").toString().equals(main_email.getText().toString()) && array.getJSONObject(i).get("password").toString().equals(main_pwd.getText().toString()))
                    {SharedPreferences.Editor editor = sharedPreferences.edit();
//                    System.out.println(main_email.getText().toString() + " main_email.getText().toString()");
                    editor.putString("bider_email", main_email.getText().toString());
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, inside_app.class));
                    found = 1;
                    }}

                    catch (JSONException e) {
                    System.out.println(e + "llooooooggggggg");
                }}
                    if(found==0)
                    {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();}
                }

        });

        main_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, sign_up_page.class));
            }
        });

    }

    void do_func()
    {StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://script.google.com/macros/s/AKfycbw4yzWKRFxXDaLbFhmj8on3BjwHX3RxKxImXZlRbiZJHOClpmvH-jCMSq-PrJPLlhuoxA/exec",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                            loading.dismiss();
                    System.out.println("ssssssss " + response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("ssssssss " + error);
                }
            }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("action","main_edit");
            params.put("main_edit", main_email.getText().toString());
//                        System.out.println("bbbbbbbbbb " + f_image);
            return params;
        }

    };

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }


    public static JSONObject readAllData(String sheet_name) throws IOException {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        try {
            OkHttpClient client = new OkHttpClient();
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(url+"opt=readAll" + "&sheetname=" + sheet_name)
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            System.out.println("messed up " + e);
            System.out.println(url+"opt=readAll");
        }
        return null;
    }

}