package com.a3x3conect.isthreedelivery;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class Dashpage extends AppCompatActivity {

    ImageButton pick,delivery;
    String mMessage;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);
        Bundle bundle = getIntent().getExtras();
        tinydb = new TinyDB(this);

        pick = (ImageButton)findViewById(R.id.pickup);
        delivery = (ImageButton)findViewById(R.id.delivery);


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Dashpage.this, Pickuplist.class);
                startActivity(intent);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Dashpage.this, Deliverylist.class);
                startActivity(intent);
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_one) {


            Intent intent = new Intent(Dashpage.this,Signin.class);
            startActivity(intent);


            // Do something
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
