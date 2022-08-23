package com.example.online_auction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class sign_up_page extends AppCompatActivity {
    EditText name, email, pno, pwd, r_pwd;
    Button sign_up_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        pno = findViewById(R.id.signup_pno);
        pwd = findViewById(R.id.signup_pwd);
        r_pwd = findViewById(R.id.signup_re_pwd);
        sign_up_btn = findViewById(R.id.sign_up_btn);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pwd.getText().toString().equals(r_pwd.getText().toString()))
                {push_to_sheet();}
                else
                    Toast.makeText(sign_up_page.this, "Password mismatch", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void push_to_sheet()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://script.google.com/macros/s/AKfycbw4yzWKRFxXDaLbFhmj8on3BjwHX3RxKxImXZlRbiZJHOClpmvH-jCMSq-PrJPLlhuoxA/exec",
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
                params.put("action","sign_up");
                params.put("email", email.getText().toString());
                params.put("name", name.getText().toString());
                params.put("pno", pno.getText().toString());
                params.put("pwd", pwd.getText().toString());
//                        System.out.println("bbbbbbbbbb " + f_image);
                return params;
            }

        };

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(sign_up_page.this);
        requestQueue.add(stringRequest);


    }

}