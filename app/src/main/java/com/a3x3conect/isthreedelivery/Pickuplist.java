package com.a3x3conect.isthreedelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
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
import android.widget.Toast;


import com.a3x3conect.isthreedelivery.Models.modelPickuplist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
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

public class Pickuplist extends AppCompatActivity {


    ProgressDialog pd;

    Gson gson;
    String mMessage;
    RecyclerView mRVFishPrice;
    private AdapterFish Adapter;
    ArrayList<prizes> dataModels = new ArrayList<>();

    List<modelPickuplist> filterdata=new ArrayList<modelPickuplist>();

    modelPickuplist data = new modelPickuplist();
    final ArrayList<String> dd = new ArrayList<>();
    List<modelPickuplist> tarif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickuplist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);

        gson = new Gson();

        getdata();
    }

    private void getdata() {


        pd = new ProgressDialog(Pickuplist.this);
        pd.setMessage("Getting Details");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/getPickupRequests")
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
                            Type listType = new TypeToken<List<modelPickuplist>>(){}.getType();
                            tarif = (List<modelPickuplist>)  gson.fromJson(mMessage,listType);

                            try {
                                JSONArray jj = new JSONArray(mMessage);

                                for (int i=0;i<jj.length();i++){

                                    JSONObject jsonObject =jj.getJSONObject(i);

                                    if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                        final Dialog openDialog = new Dialog(Pickuplist.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("No Pickups");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("No Pickups Available at this moment");
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
                                                Intent intent = new Intent(Pickuplist.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();

                                        Log.e("No Pickup","No Pickuip");

                                    }



                                }

                                TraverseData();

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

        for(int k = 0; k < tarif.size(); k++){

            filterdata.add(tarif.get(k));

            Log.e("eee", String.valueOf(filterdata.get(k)));

            Adapter = new AdapterFish(Pickuplist.this, filterdata);
            Adapter.setHasStableIds(false);
            mRVFishPrice.setAdapter(Adapter);
            mRVFishPrice.setHasFixedSize(false);
            mRVFishPrice.setLayoutManager(new LinearLayoutManager(Pickuplist.this,LinearLayoutManager.VERTICAL,true));
        }
    }


    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<modelPickuplist> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<modelPickuplist> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.listitems, parent, false);
            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);

            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final modelPickuplist current = data.get(position);
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(Pickuplist.this,PickupDetails.class);

                    intent.putExtra("message",mMessage);
                    intent.putExtra("position",position);
                    startActivity(intent);


//                    Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                }
            });
           // final modelPickuplist current = data.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);
            myHolder.one.setText(current.getCustomerId());
            myHolder.two.setText(current.getDisplayName());
//            myHolder.three.setText("City"+current.getCity() +" " +current.getCountry());
//            myHolder.four.setText("Name"+current.getDisplayName());
//            myHolder.five.setText("Phone"+current.getPhoneNo());
//            myHolder.six.setText("JOBID"+current.getJobid());
//            myHolder.seven.setText("LAT LONG"+current.getLat()+ " , "+current.getLongi());



        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView one,two;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                one = (TextView) itemView.findViewById(R.id.one);
                two = (TextView)itemView.findViewById(R.id.two);


            }

        }

    }


    public class prizes {
        public String Address;
        public String city;
        public String country;
        public String createdAt;
        public String customerId;
        public String displayName;
        public String email;
        public String id;
        public String jobid;
        public String landMark;
        public String lat;
        public String longi;
        public String phoneNo;
        public String pic;
        public String pincode;
        public String state;
        public String status;


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
