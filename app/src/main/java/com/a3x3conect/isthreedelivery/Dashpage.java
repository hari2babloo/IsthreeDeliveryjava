package com.a3x3conect.isthreedelivery;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.squareup.okhttp.Callback;
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

    ImageButton pick,delivery,myorders,expressorders;
    ProgressDialog pd;
    TextView pickupcount,deliverycount,pickupspendingcount,deliverypendingcount,pickupcancelcount,deliverycancelcount;
    String mMessage;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

        pickupcount = (TextView)findViewById(R.id.pickupcount);
        deliverycount = (TextView)findViewById(R.id.finishcount);
        pickupspendingcount = (TextView)findViewById(R.id.pickuppendingcount);
        deliverypendingcount = (TextView)findViewById(R.id.deliverypendingcount);
        pickupcancelcount = (TextView)findViewById(R.id.pickupcanceledcount);
        myorders = (ImageButton) findViewById(R.id.myorders);
        expressorders=(ImageButton)findViewById(R.id.expressorders);

        // deliverycancelcount = (TextView)findViewById(R.id.deliverycancelcount);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) Dashpage.this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
     //   Toast.makeText(Dashpage.this, "Worked!!!!", Toast.LENGTH_SHORT).show();
        Getcount();
        swipeRefreshLayout.setRefreshing(false);
    }
});


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Bundle bundle = getIntent().getExtras();
        tinydb = new TinyDB(this);
        pick = (ImageButton)findViewById(R.id.pickup);
        delivery = (ImageButton)findViewById(R.id.delivery);
        expressorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Dashpage.this,ExpressOrders.class);
                startActivity(in);
            }
        });
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Dashpage.this, Pickuplist.class);
                tinydb.putString("keypickup","getPickupRequests");
                //intent.putExtra("keypickup","getPickupRequests");
                startActivity(intent);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Dashpage.this, Deliverylist.class);
                tinydb.putString("keydelivery","getDeliveryOrders");
              //  intent.putExtra("keydelivery","getDeliveryOrders");
                startActivity(intent);
            }
        });
        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashpage.this, PickupDeliverylist.class);
                startActivity(intent);
            }
        });
        Getcount();
    }



    private void Getcount() {
        pd = new ProgressDialog(Dashpage.this);
        pd.setMessage("Getting Pickups and Deliveries Count..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getPickupDeliveriesCount")
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();
                String mMessage = e.getMessage().toString();
                Log.e("resyul reer",mMessage);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(Dashpage.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Something Went Wrong");
                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
                        dialogno.setText("OK");
                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();


//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });
                        openDialog.setCancelable(false);
                        openDialog.show();
                    }
                });



            }

            @Override
            public void onResponse(Response response) throws IOException {
                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Resy",mMessage);
                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);
                                pickupcount.setText("PICKEDUP: "+jsonObject.optString("currentDatePickupsConfirmedCount"));
                                pickupspendingcount.setText("PENDING: "+jsonObject.optString("pickupRequestsCount"));
                                pickupcancelcount.setText("CANCELED: "+jsonObject.optString("currentDatePickupsCancelledCount"));
                                deliverycount.setText("DELIVERED: "+jsonObject.optString("currentDateDeliveryOrdersCount"));
                                deliverypendingcount.setText("PENDING: "+jsonObject.optString("pendingDeliveryOrdersCount"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                           // TraverseData();

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.cancel();
                        pd.dismiss();
                        Toast.makeText(Dashpage.this, "Failed to Fetch Data", Toast.LENGTH_SHORT).show();

                    }
                });
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

            tinydb.clear();
            Intent intent = new Intent(Dashpage.this,Signin.class);

            startActivity(intent);


            // Do something
            return true;
        }


        return super.onOptionsItemSelected(item);
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
//
//            //           tinydb.putString("custid","");
//
//            tinydb.clear();
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
//                Dashpage.this.finish();
//                System.exit(0);
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
//
//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory( Intent.CATEGORY_HOME );
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
//                Dashpage.this.finish();

                moveTaskToBack(true);
//                System.exit(0);
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
        openDialog.setCancelable(false);
    }



}


