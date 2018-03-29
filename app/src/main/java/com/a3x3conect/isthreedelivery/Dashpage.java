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
//        Bundle bundle = getIntent().getExtras();
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




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_item_one) {
//
//
//            Intent intent = new Intent(Dashpage.this,Signin.class);
//            startActivity(intent);
//
//
//            // Do something
//            return true;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }


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

            //           tinydb.putString("custid","");

            tinydb.clear();
            startActivity(intent);


            // Do something
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        final Dialog openDialog = new Dialog(Dashpage.this);
        openDialog.setContentView(R.layout.alert);
        openDialog.setTitle("Exit app");
        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
        dialogTextContent.setText("Do you want to close the app?");
        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            //    Dashpage.this.finish();
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
            }
        });

        dialogno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        openDialog.show();
    }

    }


