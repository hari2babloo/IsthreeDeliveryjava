package com.a3x3conect.isthreedelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ExpressOrders extends AppCompatActivity {
    TinyDB tinydb;
    ImageButton expresspickup,expressdeliverirs;
    TextView pickupcount,deliverycount,pickupspendingcount,deliverypendingcount,pickupcancelcount,deliverycancelcount;
    String mMessage;
    ProgressDialog pd;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.express_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Express Orders");
        expressdeliverirs = (ImageButton)findViewById(R.id.expressdeliveries);
        expresspickup = (ImageButton)findViewById(R.id.exprespickups);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

        pickupcount = (TextView)findViewById(R.id.pickupcount);
        deliverycount = (TextView)findViewById(R.id.finishcount);
        pickupspendingcount = (TextView)findViewById(R.id.pickuppendingcount);
        deliverypendingcount = (TextView)findViewById(R.id.deliverypendingcount);
        pickupcancelcount = (TextView)findViewById(R.id.pickupcanceledcount);

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
        tinydb = new TinyDB(this);
        expresspickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExpressOrders.this, Pickuplist.class);

                tinydb.putString("keypickup","getExpressPickupRequests");

              //  intent.putExtra("keypickup","getExpressPickupRequests");
                startActivity(intent);

            }
        });

        expressdeliverirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpressOrders.this, Deliverylist.class);
                tinydb.putString("keydelivery","getExpressDeliveryOrders");
                //intent.putExtra("keydelivery","getExpressDeliveryOrders");
                startActivity(intent);

            }
        });


                Getcount();
    }


    private void Getcount() {
        pd = new ProgressDialog(ExpressOrders.this);
        pd.setMessage("Getting Pickups and Deliveries Count..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getExpressPickupDeliveriesCount")
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
                        final Dialog openDialog = new Dialog(ExpressOrders.this);
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
                            Log.e("Express values",mMessage);
                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);
                                pickupcount.setText("PICKEDUP: "+jsonObject.optString("currentDateExpressPickupsConfirmedCount"));
                                pickupspendingcount.setText("PENDING: "+jsonObject.optString("expressPickupRequestsCount"));
                                pickupcancelcount.setText("CANCELED: "+jsonObject.optString("currentDateExpressPickupsCancelledCount"));
                                deliverycount.setText("DELIVERED: "+jsonObject.optString("currentDateExpressDeliveryOrdersCount"));
                                deliverypendingcount.setText("PENDING: "+jsonObject.optString("pendingExpressDeliveryOrdersCount"));
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
                        Toast.makeText(ExpressOrders.this, "Failed to Fetch Data", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
