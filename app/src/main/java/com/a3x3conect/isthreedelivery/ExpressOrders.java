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
    TextView ipickeup,wpickup,dpickup,ipending,wpending,dpending,icanceled,wcanceled,dcanceled,dipending,dwpending,ddpending,didelivered,dwdelivered,dddelivered;
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
        ipickeup = (TextView)findViewById(R.id.ipickup);
        ipending = (TextView)findViewById(R.id.ipending);
        icanceled = (TextView)findViewById(R.id.icancel);
        wpickup = (TextView)findViewById(R.id.wpickup);
        wpending = (TextView)findViewById(R.id.wpending);
        wcanceled = (TextView)findViewById(R.id.wcancel);
        dpickup = (TextView)findViewById(R.id.dpickup);
        dpending = (TextView)findViewById(R.id.dpendind);
        dcanceled = (TextView)findViewById(R.id.dcancel);
        dipending = (TextView)findViewById(R.id.dipending);
        didelivered = (TextView)findViewById(R.id.dideliverd);
        dwpending = (TextView)findViewById(R.id.dwpending);
        dwdelivered = (TextView)findViewById(R.id.dwdelivered);
        ddpending = (TextView)findViewById(R.id.ddpending);
        dddelivered = (TextView)findViewById(R.id.dddelivered);

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
                                ipickeup.setText(jsonObject.optString("ironingExpressPickupsConfirmedCount"));
                                wpickup.setText(jsonObject.optString("washAndPressExpressPickupsConfirmedCount"));
                                dpickup.setText(jsonObject.optString("dryCleaningExpressPickupsConfirmedCount"));

                                ipending.setText(jsonObject.optString("ironingExpressRequestsCount"));
                                wpending.setText(jsonObject.optString("washAndPressExpressRequestsCount"));
                                dpending.setText(jsonObject.optString("dryCleaningExpressRequestsCount"));

                                icanceled.setText(jsonObject.optString("ironingExpressPickupsCancelledCount"));
                                wcanceled.setText(jsonObject.optString("washAndPressExpressPickupsCancelledCount"));
                                dcanceled.setText(jsonObject.optString("dryCleaningExpressPickupsCancelledCount"));

                                dipending.setText(jsonObject.optString("ironingPendingExpressDeliveryOrdersCount"));
                                dwpending.setText(jsonObject.optString("washAndPressPendingExpressDeliveryOrdersCount"));
                                ddpending.setText(jsonObject.optString("dryCleaningPendingExpressDeliveryOrdersCount"));

                                didelivered.setText(jsonObject.optString("ironingExpressDeliveryOrdersCount"));
                                dwdelivered.setText(jsonObject.optString("washAndPressExpressDeliveryOrdersCount"));
                                dddelivered.setText(jsonObject.optString("dryCleaningExpressDeliveryOrdersCount"));
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
