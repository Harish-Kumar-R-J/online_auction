package com.example.online_auction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.okhttp.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class inside_app extends AppCompatActivity {
ImageButton inside_add, refresh_btn;
    public static final String url= "https://script.google.com/macros/s/AKfycbw4yzWKRFxXDaLbFhmj8on3BjwHX3RxKxImXZlRbiZJHOClpmvH-jCMSq-PrJPLlhuoxA/exec?";
    public static com.squareup.okhttp.Response response;
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> p_name = new ArrayList<>();
    ArrayList<String> p_desc = new ArrayList<>();
    ArrayList<String> bid = new ArrayList<>();
    ArrayList<String> uid = new ArrayList<>();
    ArrayList<String> bider_email = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_app);
        inside_add = findViewById(R.id.inside_add);
        refresh_btn = findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh_func();
            }
        });
        inside_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(inside_app.this, add_item.class));
            }
        });

        JSONObject db;
        JSONArray array = null;
        try {
            db = readAllData("Sheet2");
            assert db != null;
            array = db.getJSONArray("records");
            System.out.println("ppppppp " + db);
        } catch (IOException | JSONException e) {

        }


        for(int i=0;i<array.length();i++)
        {
            try {
                img.add(array.getJSONObject(i).get("image").toString());
                    p_name.add(array.getJSONObject(i).get("prod_name").toString());
                    p_desc.add(array.getJSONObject(i).get("desc").toString());
                    bid.add(array.getJSONObject(i).get("bid").toString());
                    uid.add(array.getJSONObject(i).get("uid").toString());
                    bider_email.add(array.getJSONObject(i).get("email").toString());

            } catch (JSONException e) {
                System.out.println(e + "llooooooggggggg");
            }
        }
        recycler();
    }

    void refresh_func()
    {
        img.clear();
        p_name.clear();
        p_desc.clear();
        bid.clear();
        uid.clear();
        bider_email.clear();
        JSONObject db;
        JSONArray array = null;
        try {
            db = readAllData("Sheet2");
            assert db != null;
            array = db.getJSONArray("records");
            System.out.println("ppppppp " + db);
        } catch (IOException | JSONException e) {

        }


        for(int i=0;i<array.length();i++)
        {
            try {
                img.add(array.getJSONObject(i).get("image").toString());
                p_name.add(array.getJSONObject(i).get("prod_name").toString());
                p_desc.add(array.getJSONObject(i).get("desc").toString());
                bid.add(array.getJSONObject(i).get("bid").toString());
                uid.add(array.getJSONObject(i).get("uid").toString());
                bider_email.add(array.getJSONObject(i).get("email").toString());

            } catch (JSONException e) {
                System.out.println(e + "llooooooggggggg");
            }
        }
        recycler();}

    public void recycler()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(inside_app.this, LinearLayoutManager.VERTICAL, false);
        RecyclerView employee_recycler = findViewById(R.id.inside_recycler);
        employee_recycler.setLayoutManager(layoutManager);
        employee_recycler adapter = new employee_recycler(inside_app.this, p_name, img, p_desc, bid, uid, bider_email);
        employee_recycler.setAdapter(adapter);}

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


class employee_recycler extends RecyclerView.Adapter<employee_recycler.ViewHolder> {

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> desc = new ArrayList<>();
    private ArrayList<String> bid = new ArrayList<>();
    private ArrayList<String> uid = new ArrayList<>();
    private ArrayList<String> bider_mail = new ArrayList<>();
    private Context mContext;

    public employee_recycler(Context mContext, ArrayList<String> names, ArrayList<String> images, ArrayList<String>desc, ArrayList<String>bid, ArrayList<String>uid, ArrayList<String>bider_mail) {
        this.names = names;
        this.images = images;
        this.desc = desc;
        this.bid = bid;
        this.uid = uid;
        this.bider_mail = bider_mail;
        this.mContext = mContext;
    }

    @Override
    public @NotNull
    employee_recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
        return new employee_recycler.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(employee_recycler.ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("bider", Context.MODE_PRIVATE);

        byte[] bytes = Base64.decode(images.get(position), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.image.setImageBitmap(bitmap);
        holder.name.setText(names.get(position));
        holder.bid.setText(bid.get(position));

//        holder.name.setBackgroundResource(R.color.white);
//        holder.name.setTextColor(R.color.white);
//        holder.price.setTextColor(R.color.white);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, expand_view.class);
                intent.putExtra("name", names.get(position));
                intent.putExtra("price", bid.get(position));
                intent.putExtra("img", images.get(position));
                intent.putExtra("desc", desc.get(position));
                intent.putExtra("uid", uid.get(position));
                intent.putExtra("bider_name", bider_mail.get(position));
                mContext.startActivity(intent);
            }
        });
//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(mContext, names.get(position), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, emp_order.class);
//                intent.putExtra("name", names.get(position));
//                intent.putExtra("price", price.get(position));
//                intent.putExtra("desc", bdesc.get(position));
//                intent.putExtra("img", images.get(position));
//                intent.putExtra("time", "breakfast");
//                mContext.startActivity(intent);
////             new employee_menu().to_menu_expand();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;
        TextView bid;
        RelativeLayout layout;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ll_p_image);
            name = itemView.findViewById(R.id.ll_p_name);
            bid = itemView.findViewById(R.id.ll_bid);
            layout = itemView.findViewById(R.id.list_item_id);
        }
    }



}