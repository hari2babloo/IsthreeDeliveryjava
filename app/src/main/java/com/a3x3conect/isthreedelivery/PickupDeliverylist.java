package com.a3x3conect.isthreedelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.a3x3conect.isthreedelivery.Models.getPickupDeliveryOrders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PickupDeliverylist extends AppCompatActivity {

    
    ProgressDialog pd;
    TinyDB tinyDB;
    String mMessage;
    private AdapterFish Adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;
    RecyclerView mRVFishPrice;
    List<getPickupDeliveryOrders> filterdata=new ArrayList<getPickupDeliveryOrders>();
    List<getPickupDeliveryOrders> filterdata2=new ArrayList<getPickupDeliveryOrders>();

    //getPickupDeliveryOrders data = new getPickupDeliveryOrders();
    final ArrayList<String> dd = new ArrayList<>();
    List<getPickupDeliveryOrders> tarif;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_deliverylist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Today's Orders");

        swipeRefreshLayout = findViewById(R.id.swipe_container);
        mRVFishPrice = findViewById(R.id.fishPriceList);
        tinyDB = new TinyDB(this);
        gson = new Gson();


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


        pd = new ProgressDialog(PickupDeliverylist.this);
        pd.setMessage("Getting Details");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdat = new JSONObject();

//        try {
//            //  postdat.put("customerId", tinyDB.getString("custid"));
//            postdat.put("agentId", tinyDB.getString("partnerid"));
//        } catch(JSONException e){
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

       // Log.e("postdata",postdat.toString());

        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getPickupDeliveryOrders")
                .get()
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

                            Type listType = new TypeToken<List<getPickupDeliveryOrders>>(){}.getType();
                            tarif = gson.fromJson(mMessage,listType);

                            try {
                                JSONArray jj = new JSONArray(mMessage);

                                for (int i=0;i<jj.length();i++){

                                    JSONObject jsonObject =jj.getJSONObject(i);

                                    if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                        final Dialog openDialog = new Dialog(PickupDeliverylist.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("No Pickups");
                                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("No List Available at this moment");
                                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                        dialogCloseButton.setVisibility(View.GONE);
                                        Button dialogno = openDialog.findViewById(R.id.cancel);
                                        dialogno.setText("OK");
                                        dialogno.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(PickupDeliverylist.this,Dashpage.class);
                                                startActivity(intent);
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

        Adapter = new AdapterFish(PickupDeliverylist.this, tarif);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.scrollToPosition(0);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(PickupDeliverylist.this,LinearLayoutManager.VERTICAL,false));
    }


    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<getPickupDeliveryOrders> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<getPickupDeliveryOrders> data) {
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
            final getPickupDeliveryOrders current = data.get(position);


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


            if(current.getServiceName() != null && !current.getServiceName().isEmpty()) {

                if (current.getServiceName().equalsIgnoreCase("ironing")){

                   // myHolder.servicename.setImageResource(R.drawable.iconironing);

                    myHolder.servicetypetxt.setText("I");
                   // myHolder.servicename.setText("Ironing");
                }
                else if (current.getServiceName().equalsIgnoreCase("washAndPress")){
                   // myHolder.servicename.setText("Wash and Press");
                   // myHolder.servicename.setImageResource(R.drawable.trash);
                    myHolder.servicetypetxt.setText("W");
                }
                else if (current.getServiceName().equalsIgnoreCase("dryCleaning")){

                    //myHolder.servicename.setText("Dry Cleaning");
                    myHolder.servicetypetxt.setText("D");
//                    myHolder.servicename.setImageResource(R.drawable.icondry);
                    // myHolder.serviceimg.setColorFilter(R.color.colorAccent);
                }



            }
            else {

               // myHolder.servicename.setText("Ironing");
              //  myHolder.servicename.setImageResource(R.drawable.iconironing);
                myHolder.servicetypetxt.setText("I");
            }
            myHolder.custid.setText(current.getCustomerId());
            myHolder.createdate.setText( "Created on: "+current.getCreatedAt());
                            myHolder.status.setText(current.getStatus());


                            if (current.getExpressDelivery().equalsIgnoreCase("0")){

                                myHolder.expresimg.setVisibility(View.GONE);
                            }
            switch (current.getStatus()){


                case "PICKUP-INITIATED":
                    myHolder.status.setTextColor(Color.parseColor("#d20670"));
                    myHolder.status.setText("PICKUP-SCHEDULED");
                    myHolder.misdate.setText("Initiated on: "+ current.getPickupScheduledAt());
                    break;
                case "PICKUP-REQUESTED":
                    myHolder.status.setTextColor(Color.parseColor("#d20670"));
                    myHolder.status.setText("PICKUP-SCHEDULED");
                    myHolder.misdate.setText("Requested on: "+ current.getPickupScheduledAt());
                    break;
                case "PICKUP-CONFIRMED":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("PICKED-UP");
                    myHolder.misdate.setText("Confirmed on: " +current.getPickupConfirmedDate());
                    break;
                case "PICKUP-CANCELLED":
                    myHolder.status.setTextColor(Color.parseColor("#cc0000"));
                    myHolder.misdate.setText("Cancelled on: "+current.getPickupCancelledDate());
                    break;

                case "PICKUP-INPROCESS":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("IN-PROCESS");
                    myHolder.misdate.setText("Processed on: " +current.getOrderProcessedDate());


                    break;
                case "PICKUP-PROCESSED":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("OUT FOR DELIVERY");
                    myHolder.misdate.setText("Processed on: " +current.getOrderProcessedDate());
                    if (current.getOrderProcessedDate().toString().contains("1970")){
                        myHolder.misdate.setText("Date not available ");
                    }

                    break;
                case  "JOB-FINISHED":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("DELIVERED");
                    myHolder.misdate.setText("Job Finished on: "+current.getJobFinishedDate());
                    break;
                case  "Address does not exist":
                case  "Phone number not reachable":
                case  "Customer does not exist":
                case  "Customer not at home":
                    case  "Issue not listed":
                    myHolder.status.setTextColor(Color.parseColor("#cc0000"));
                    myHolder.misdate.setText("Cancelled on: "+current.getPickupCancelledDate());
                    break;
            }


        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView custid,status,createdate,misdate,servicetypetxt;
            ImageView expresimg,servicename;
            View line;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                custid = itemView.findViewById(R.id.custid);
                status = itemView.findViewById(R.id.status);
                createdate = itemView.findViewById(R.id.createdate);
                misdate = itemView.findViewById(R.id.misdate);
                expresimg = itemView.findViewById(R.id.expresimg);
              //  servicename = (ImageView)itemView.findViewById(R.id.servicename);
                servicetypetxt = itemView.findViewById(R.id.servicetypetxt);



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
