package com.a3x3conect.isthreedelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3x3conect.isthreedelivery.Models.Modelmanagertransactions;
import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.a3x3conect.isthreedelivery.Models.getPickupDeliveryOrders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ManagerWalletTransactions extends AppCompatActivity {


    ProgressDialog pd;
    TinyDB tinyDB;
    String mMessage;
    private AdapterFish Adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;
    RecyclerView mRVFishPrice;
    TextView walletbalancetxt;

    String walletbalancestring;
    List<Modelmanagertransactions> tarif;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_wallet_transactions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manager Transactions");
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        tinyDB = new TinyDB(this);
        gson = new Gson();
        walletbalancetxt  = (TextView)findViewById(R.id.wallet);


        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        getdata();
    }

    private void getdata() {


        pd = new ProgressDialog(ManagerWalletTransactions.this);
        pd.setMessage("Getting Details");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            //  postdat.put("customerId", tinyDB.getString("custid"));

          //  postdat.put("userId","C0111");
            postdat.put("userId", tinyDB.getString("partnerid"));
           // postdat.put("userId", "c0049");
          //  postdat.put("userId", "c0023");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//
 RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        // Log.e("postdata",postdat.toString());

        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getManagerWalletTransactions")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                pd.dismiss();
                pd.cancel();

                mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                mMessage = response.body().string();

                pd.dismiss();
                pd.cancel();

                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Getlocations();

                            Type listType = new TypeToken<List<Modelmanagertransactions>>(){}.getType();
                            tarif = (List<Modelmanagertransactions>)  gson.fromJson(mMessage,listType);

                            try {
                                JSONArray jj = new JSONArray(mMessage);

                                for (int i=0;i<jj.length();i++){

                                    final JSONObject jsonObject =jj.getJSONObject(i);


                                    walletbalancetxt.setText("Wallet Balance : " +jsonObject.getString("managerWalletBalance").toString());



                                    if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                        final Dialog openDialog = new Dialog(ManagerWalletTransactions.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("No Pickups");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("Transaction List is Empty.");
                                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                        dialogCloseButton.setVisibility(View.GONE);
                                        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
                                        dialogno.setText("OK");
                                        dialogno.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                               // openDialog.dismiss();

                                                openDialog.dismiss();
                                                openDialog.cancel();


//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ManagerWalletTransactions.this,Dashpage.class);
//                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();

                                        openDialog.setCancelable(false);
                                        Log.e("No Pickup","No Pickuip");

                                    }

                                    else {

                                        TraverseData();
                                    }



                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });




                }

                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });
                }
            }

        });



    }


    private void TraverseData() {

        Adapter = new AdapterFish(ManagerWalletTransactions.this, tarif);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.scrollToPosition(0);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(ManagerWalletTransactions.this,LinearLayoutManager.VERTICAL,false));
    }
    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Modelmanagertransactions> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<Modelmanagertransactions> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.pickupdeliverylistitem, parent, false);
            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);

            return holder;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final Modelmanagertransactions current = data.get(position);


            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    Intent intent = new Intent(PickupDeliverylist.this,SummaryPickupDelivery.class);
//                    intent.putExtra("jobid",current.getJobid());
//                    intent.putExtra("custid",current.getCustomerId());
//                    startActivity(intent);
//
//
//                    Toast.makeText(context, current.getJobid()+current.getCustomerId(), Toast.LENGTH_SHORT).show();
                }
            });
            // final modelPickuplist current = data.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);

            myHolder.custid.setText("Customer Id :  " +current.getCustomerId());
            myHolder.custid.setTextSize(12);

            myHolder.misdate.setText( "Sent on:   " +current.getTransactionTime());
            myHolder.createdate.setText("Amount :  " +getResources().getString(R.string.rupee) +current.getTransactionAmount());

            myHolder.status.setVisibility(View.GONE);
            myHolder.expresimg.setVisibility(View.GONE);
          //  myHolder.misdate.setText(current.getTransactionAmount());




        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView custid,status,createdate,misdate;
            ImageView expresimg;
            View line;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                custid = (TextView) itemView.findViewById(R.id.custid);
                status = (TextView)itemView.findViewById(R.id.status);
                createdate = (TextView) itemView.findViewById(R.id.createdate);
                misdate = (TextView)itemView.findViewById(R.id.misdate);
                expresimg = (ImageView)itemView.findViewById(R.id.expresimg);



            }

        }

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
