package com.example.online_auction;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class expand_view extends AppCompatActivity {
    TextView p_name, b_email, desc;
    EditText bid;
    ImageButton img;
    String for_img, uid;
    Button bid_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_view);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        p_name = findViewById(R.id.p_name_txt);
        b_email = findViewById(R.id.bider_name_txt);
        bid = findViewById(R.id.bid_amt);
        img = findViewById(R.id.expand_image);
        desc = findViewById(R.id.desc_name_txt);
        bid_btn = findViewById(R.id.bid_btn);
        p_name.setText(b.getString("name"));
        b_email.setText(b.getString("bider_name"));
        bid.setText(b.getString("price"));
        desc.setText(b.getString("desc"));
        System.out.println("b.getString(desc) " + b.getString("desc"));
        uid = b.getString("uid");

        for_img = b.getString("img");
        byte[] bytes = Base64.decode(for_img, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(bitmap);

        bid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("charSequence  " + charSequence);
                if(charSequence.toString().length()!=0)
                {if(Float.parseFloat(b.getString("price"))<Float.parseFloat(charSequence.toString()))
                {bid_btn.setClickable(true);
                bid_btn.setEnabled(true);
                    System.out.println("clickableeeeeeee");
                }
                else
                {bid_btn.setClickable(false);
                bid_btn.setEnabled(false);
                System.out.println("!clickableeeeeeee");}}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bid_btn.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sharedPreferences = getSharedPreferences("bider", MODE_PRIVATE);
            @Override
            public void onClick(View view) {
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
                        params.put("action","update");
                        params.put("email", sharedPreferences.getString("bider_email", ""));
                        params.put("uid", b.getString("uid"));
                        params.put("bid", bid.getText().toString());
//                        System.out.println("bbbbbbbbbb " + f_image);
                        return params;
                    }

                };

                int socketTimeout = 30000; // 30 seconds. You can change it
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(expand_view.this);
                requestQueue.add(stringRequest);
            }
        });

//        bid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }
}