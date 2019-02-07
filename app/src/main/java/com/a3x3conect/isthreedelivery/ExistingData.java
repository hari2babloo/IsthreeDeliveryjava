package com.a3x3conect.isthreedelivery;



import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.a3x3conect.isthreedelivery.Models.JobOrder;
import com.a3x3conect.isthreedelivery.Models.Tariff;
import com.a3x3conect.isthreedelivery.Models.TinyDB;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExistingData extends AppCompatActivity {

    ProgressDialog pd;
    String mMessage2,foldtype,hangerpric;
    JSONObject json_data;
    private AdapterFish Adapter;
    double s=0,expresscharge=0;
    ArrayAdapter<String> adapter;
    JSONArray jsonArray;
    List<Tariff> tarif;
    float garmentscount = 0;

    ArrayList<DataFish2> filterdata2=new ArrayList<DataFish2>();
    ArrayList<DataFish2> hangerlist=new ArrayList<DataFish2>();

    ArrayList<DataFish> filterdata=new ArrayList<DataFish>();
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> prize = new ArrayList<>();
    ArrayList<String> hangerPrice = new ArrayList<>();
    final ArrayList<String> dd = new ArrayList<>();
    TableLayout tableLayout;
    String price,type,quantity,amount;
    TextView btmtotal,expresstxt;
    RecyclerView mRVFishPrice;
    Spinner spinner;
    EditText qty;
    Snackbar snackbar;
    String exprsval="0";
    CheckBox checkBox,chkboxhanger;
    TextView hangertxt;
    ListView lv_languages;
    BottomSheetDialog bottomSheetDialog;
    Button add,pay,cancel;
    String expressDelivery,serviceName,deliverOnHanger;
    ArrayList<String> fourdour = new ArrayList<>();
    ArrayList<String> rates = new ArrayList<>();
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    TinyDB tinyDB;
    TextView ratescard;
    double minimumvalue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_data);
        tinyDB = new TinyDB(ExistingData.this);
        // exprsval = tinyDB.getString("expressDelivery");
        spinner  = findViewById(R.id.spinner);
        qty = findViewById(R.id.qty);
        add = findViewById(R.id.add);
        pay = findViewById(R.id.pay);
        expresstxt = findViewById(R.id.expresstxt);
        checkBox = findViewById(R.id.checkBox);
        btmtotal = findViewById(R.id.btmtotal);
        chkboxhanger = findViewById(R.id.chkboxhanger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Form");
        //expresscharge=tinyDB.getDouble("expressDeliveryCharge",0);
        hangertxt = findViewById(R.id.hangertxt);

        expressDelivery = getIntent().getStringExtra("expressDelivery");


        serviceName = getIntent().getStringExtra("serviceName");
        deliverOnHanger = getIntent().getStringExtra("deliverOnHanger");
        expresscharge = getIntent().getDoubleExtra("expressDeliveryCharge",0);

        if (serviceName.equalsIgnoreCase("dryCleaning")){


            chkboxhanger.setVisibility(View.GONE);
            hangertxt.setVisibility(View.GONE);
        }
        Intent intent = getIntent();

        if( intent.hasExtra("expressDelivery")){
            exprsval = getIntent().getStringExtra("expressDelivery");


        }


        ratescard = findViewById(R.id.rates);
        if (exprsval.equalsIgnoreCase("1")){
            checkBox.setChecked(true);
            expresscharge=getIntent().getDoubleExtra("expressDeliveryCharge",0);
            btmtotal.setText("Total " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
            //            checkBox.setVisibility(View.GONE);
            //            expresstxt.setVisibility(View.GONE);
        }

        else {

            exprsval = "0";
            expresscharge=0;
        }

        chkboxhanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chkboxhanger.isChecked()){

                    //checkBox.setChecked(false);
                    foldtype =  "hanger";
                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Delivery on Hanger Enabled",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    AddtoList();

                    Log.e("foldtype",foldtype);
                }

                else{

                    //checkBox.setChecked(true);



                    foldtype="normal";
                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Delivery on Hanger Disabled",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    AddtoList();

                    Log.e("foldtype",foldtype);

                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){


                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Express Delivery Enabled",Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    // Toast.makeText(ExistingData.this, "Express Delivery Enabled", Toast.LENGTH_SHORT).show();
                    exprsval = "1";
                    expresscharge=getIntent().getDoubleExtra("expressDeliveryCharge",0);
                    btmtotal.setText("Total " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
                }
                else {

                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Express Delivery Disabled",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    //  Toast.makeText(ExistingData.this, "Express Delivery Disabled", Toast.LENGTH_SHORT).show();
                    exprsval = "0";
                    expresscharge=0;
                    btmtotal.setText("Total " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
                }
            }
        });

        ratescard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExistingData.this,android.R.layout.simple_list_item_1,rates);

                View view = getLayoutInflater().inflate(R.layout.farelistitemrow, null);

//                TextView textView = (TextView)view.findViewById(android.R.id.text1);
//
//                textView.setTextColor(getResources().getColor(R.color.bluee));
                lv_languages = view.findViewById(R.id.lv_languages);
                lv_languages.setAdapter(adapter);
                bottomSheetDialog = new BottomSheetDialog(ExistingData.this);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

            }
        });

        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog openDialog = new Dialog(ExistingData.this);
                openDialog.setContentView(R.layout.alert);
                openDialog.setTitle("Cancel Order");
                TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                dialogTextContent.setText("Do you really want to cancel order?");
                ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);

                dialogCloseButton.setText("Yes, I want to");

                Button dialogno = openDialog.findViewById(R.id.cancel);

                dialogno.setText("NO");


                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog.dismiss();
                        Cancelschedule();
                    }
                });
                dialogno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog.dismiss();



//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                    }
                });



                openDialog.show();
                openDialog.setCancelable(false);


            }
        });
        mRVFishPrice = findViewById(R.id.fishPriceList2);

        mRVFishPrice.smoothScrollToPosition(0);
        tableLayout = findViewById(R.id.tabl);
        //  tableLayout.setVisibility(View.GONE);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Log.e("Bolean", String.valueOf(boolfirsttime));


                if (filterdata2.isEmpty()){

                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Please fill the form",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    //Toast.makeText(Pickup.this, "Please fill the form ", Toast.LENGTH_SHORT).show();
                }

                else {

                    if (minimumvalue==0){
                        minimumordervalue();
                    }

                    else if (minimumvalue>s){
                        final Dialog openDialog = new Dialog(ExistingData.this);
                        openDialog.setContentView(R.layout.schedulealert);
                        openDialog.setTitle("Schedule");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Please make sure that your order value is not less than " +getResources().getString(R.string.rupee)+ String.valueOf(minimumvalue));

                        //  note.setText("Please note, there will be no pickup or delivery on THURSDAYS as it is a weekly holiday for our operations");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setText("OK");
                        Button dialogno = openDialog.findViewById(R.id.cancel);
                        dialogno.setText("Cancel");
                        dialogno.setVisibility(View.GONE);

                        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();
//                                         Intent intent = new Intent(SchedulePickup.this,Pickup.class);
//                                         tinyDB.putString("jobid",timeStamp);
//                                         intent.putExtra("expressDelivery",exprsval);
////                                            tinyDB.putString("expressDelivery",exprsval);
//                                         startActivity(intent);

                                // ScheduleProcess();
                            }
                        });
                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();
                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
                                startActivity(intent);
                            }
                        });
                        openDialog.setCancelable(false);
                        openDialog.show();




                    }

                    else if (s>=minimumvalue){

                        Paydata();
                    }

                }
            }
        });

        GetFormData();

    }

    private void minimumordervalue() {


        pd = new ProgressDialog(ExistingData.this);
        pd.setMessage("Getting Minimum Order Value..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("serviceName",serviceName);
            postdat.put("serviceLocation",tinyDB.getString("serviceLocation"));


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("MinSched", postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getMinimumOrderValue")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
                pd.dismiss();
                pd.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ExistingData.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
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

                final String   mMessage = response.body().string();
                Log.e("minimum ordervalue",mMessage);
                pd.dismiss();
                pd.cancel();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);

                                if (jsonObject.getString("statusCode").equalsIgnoreCase("0")){

                                    final Dialog openDialog = new Dialog(ExistingData.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Something Went Wrong");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Something Went Wrong,Please Try Again.");
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
                                            Intent intent = new Intent(ExistingData.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });

                                    openDialog.setCancelable(false);

                                    openDialog.show();



                                }

                                else  if (jsonObject.getString("statusCode").equalsIgnoreCase("1")) {


                                    minimumvalue =  jsonObject.getDouble("minimumOrderValue");


                                    if (s>=minimumvalue){

                                        Paydata();
                                    }
                                    else {

                                        final Dialog openDialog = new Dialog(ExistingData.this);
                                        openDialog.setContentView(R.layout.schedulealert);
                                        openDialog.setTitle("Schedule");
                                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("Please make sure that your order value is not less than " +getResources().getString(R.string.rupee)+ jsonObject.getString("minimumOrderValue"));

                                        //  note.setText("Please note, there will be no pickup or delivery on THURSDAYS as it is a weekly holiday for our operations");
                                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                        dialogCloseButton.setText("OK");
                                        Button dialogno = openDialog.findViewById(R.id.cancel);
                                        dialogno.setText("Cancel");
                                        dialogno.setVisibility(View.GONE);

                                        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openDialog.dismiss();
//                                         Intent intent = new Intent(SchedulePickup.this,Pickup.class);
//                                         tinyDB.putString("jobid",timeStamp);
//                                         intent.putExtra("expressDelivery",exprsval);
////                                            tinyDB.putString("expressDelivery",exprsval);
//                                         startActivity(intent);

                                                // ScheduleProcess();
                                            }
                                        });
                                        dialogno.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openDialog.dismiss();
                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });
                                        openDialog.setCancelable(false);
                                        openDialog.show();


                                    }






                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });


    }

    private void Paydata() {


        pd = new ProgressDialog(ExistingData.this);
        pd.setMessage("Updating your Order");
        pd.setCancelable(false);
        pd.show();



        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();
        JSONArray itemType = new JSONArray();
        JSONArray unitPrice = new JSONArray();
        JSONArray subTotal = new JSONArray();
        JSONArray quantity = new JSONArray();
        int deliveronhangetkey;

        if (foldtype.equalsIgnoreCase("normal")){

            deliveronhangetkey=0;
            for (int i=0;i<filterdata2.size();i++){

                itemType.put(filterdata2.get(i).item);
            }
            for (int i=0;i<filterdata2.size();i++){

                unitPrice.put(filterdata2.get(i).cost);
            }
            for (int i=0;i<filterdata2.size();i++){

                subTotal.put(filterdata2.get(i).amt);
            }

            for (int i=0;i<filterdata2.size();i++){


                float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
                garmentscount+= foo;
                quantity.put(filterdata2.get(i).noofpieces);
            }
        }

        else {

            deliveronhangetkey=1;

            for (int i=0;i<hangerlist.size();i++){

                itemType.put(hangerlist.get(i).item);
            }
            for (int i=0;i<hangerlist.size();i++){

                unitPrice.put(hangerlist.get(i).cost);
            }
            for (int i=0;i<hangerlist.size();i++){

                subTotal.put(hangerlist.get(i).amt);
            }

            for (int i=0;i<hangerlist.size();i++){
                float foo = Float.parseFloat(hangerlist.get(i).noofpieces);
                garmentscount+= foo;
                quantity.put(hangerlist.get(i).noofpieces);
            }
        }
        float sum = 0;
        for (int i = 0; i < filterdata2.size(); i++) {

            Float dd = Float.parseFloat(filterdata2.get(i).amt);
            sum += dd;
        }
        Log.e("rererer", String.valueOf(sum));
        //   btmamt.setText("Sub Total = " +String.valueOf(sum));
        double s2 =  ((0/100) *sum)+sum;
        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        try {
            //  postdat.put("status", "PICKUP-CONFIRMED");
            postdat.put("customerId",tinyDB.getString("custid"));
            postdat.put("expressDelivery",exprsval);
            postdat.put("jobId",tinyDB.getString("jobid"));
            postdat.put("jobOrderDateTime",timeStamp2);
            postdat.put("gstPercentage", "0");
            postdat.put("grandTotal",String.format("%.2f",s+expresscharge));
            postdat.put("garmentsCount",garmentscount);
            postdat.put("itemType",itemType);
            postdat.put("unitPrice",unitPrice);
            postdat.put("quantity",quantity);
            postdat.put("subTotal",subTotal);
            postdat.put("serviceName",serviceName);
            postdat.put("deliverOnHanger",deliveronhangetkey);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        Log.e("array", String.valueOf(postdat));
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"updateJobOrder")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                pd.dismiss();
                pd.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ExistingData.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = openDialog.findViewById(R.id.cancel);

                        dialogno.setText("OK");


                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });



                        openDialog.show();
                        openDialog.setCancelable(false);

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String   mMessage = response.body().string();

                Log.e("result",mMessage);
                pd.dismiss();
                pd.cancel();

//                Log.e("resstsy",response.body().string());
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonResponse = null;
                            try {
                                jsonResponse = new JSONObject(mMessage);

                                if (jsonResponse.getString("statusCode").equalsIgnoreCase("0")){

                                    final Dialog openDialog = new Dialog(ExistingData.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Error");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonResponse.getString("status"));
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
                                    dialogno.setVisibility(View.GONE);

                                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });

                                    openDialog.show();
                                    openDialog.setCancelable(false);
                                }

                                else if (jsonResponse.getString("statusCode").equalsIgnoreCase("1")){



                                    final Dialog openDialog = new Dialog(ExistingData.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Success");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonResponse.getString("status"));
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
                                    dialogno.setVisibility(View.GONE);

                                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

                                            //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ExistingData.this,SummaryReport.class);
                                           // if (exprsval.equalsIgnoreCase("1")){

                                                //intent.putExtra("expressDeliveryCharge",tinyDB.getDouble("expressDeliveryCharge",0));
                                           // }

                                          //  else {

                                               // intent.putExtra("expressDeliveryCharge",0);
                                           // }
//
                                            //                     tinyDB.putListString("filterdata",filterdata2);
//                                            intent.putExtra("filterdata",filterdata2);
                                            startActivity(intent);
                                        }
                                    });
                                    //
                                    //  Log.e("json",sss);

                                    openDialog.show();
                                    openDialog.setCancelable(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });


    }

    private void Cancelschedule() {

        pd = new ProgressDialog(ExistingData.this);
        pd.setMessage("Cancel Order");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
//                        postdat.put("customerId", "C0016");
//            postdat.put("jobId", "2000309051221");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("Postdata",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"cancelJobOrder")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ExistingData.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = openDialog.findViewById(R.id.cancel);

                        dialogno.setText("OK");


                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });



                        openDialog.show();
                        openDialog.setCancelable(false);

                    }
                });


                String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();
                final String mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //tinyDB.remove("expressDelivery");

                            Log.e("cancel",mMessage);

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);

                                if(jsonObject.getString("statusCode").equalsIgnoreCase("1")){


                                    final Dialog openDialog = new Dialog(ExistingData.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ExistingData.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                    openDialog.setCancelable(false);
                                }

                                else {

                                    final Dialog openDialog = new Dialog(ExistingData.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                    openDialog.setCancelable(false);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();


                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });

    }

    private void GetFormData() {

        pd = new ProgressDialog(ExistingData.this);
        pd.setMessage("Getting Form Data");
        pd.setCancelable(false);
        pd.show();

        final   OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("serviceName",serviceName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"alltariff")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
                pd.dismiss();
                pd.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ExistingData.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = openDialog.findViewById(R.id.cancel);

                        dialogno.setText("OK");



                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(Pickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Pickup.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });



                        openDialog.show();
                        openDialog.setCancelable(false);

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String mMessage = response.body().string();

                Log.e("result",mMessage);
                pd.dismiss();
                pd.cancel();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Tariff>>(){}.getType();
                            tarif = gson.fromJson(mMessage,listType);

                            for(int j = 0; j < tarif.size(); j++) {


                                DataFish dataFish = new DataFish(tarif.get(j).getId(),tarif.get(j).getType(),tarif.get(j).getPrice(),tarif.get(j).getHangerPrice());
                                filterdata.add(dataFish);

                            }

                            try {
                                jsonArray = new JSONArray(mMessage);
                                for(int j = 0; j < jsonArray.length(); j++){
                                    json_data = jsonArray.getJSONObject(j);
                                    rates.add(json_data.getString("category")+" :  "+getResources().getString(R.string.rupee)+json_data.getString("price"));
                                    items.add(json_data.getString("category"));
                                    prize.add(json_data.getString("price"));
                                    hangerPrice.add(json_data.getString("hangerPrice"));

                                    Log.e("Dta",dd.toString());
                                }

                                setspinner();

                                getjoborder();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                    price = prize.get(position);
                                    type = items.get(position);
                                    type = filterdata.get(position).Dcategory;

                                    hangerpric = hangerPrice.get(position);

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    quantity = qty.getText().toString();

                                    if (TextUtils.isEmpty(quantity)){

                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"Please Enter Quantity",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        //  Toast.makeText(ExistingData.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                                        //myHolder.qty.setError("empty");
                                    }
                                    else if (quantity.equalsIgnoreCase("0")){

                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"Please Enter Quantity",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
//                                        Toast.makeText(ExistingData.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();

                                    }

                                    else if (items.isEmpty()){

                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"List is Empty",Snackbar.LENGTH_SHORT);
                                        snackbar.show();

                                        //                                      Toast.makeText(getApplicationContext(), "List is Empty", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {

                                        if (fourdour.isEmpty()){

                                            fourdour.add(type);

                                            Float foo = Float.parseFloat(quantity);
                                            Float fo2 = Float.parseFloat(price);
                                            Float x = foo * fo2;
                                            amount =Float.toString(x);
                                            Log.e(type,quantity+price+amount);
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount,hangerpric);
                                            filterdata2.add(ss);


                                            Float fo3 = Float.parseFloat(hangerpric);
                                            Float xy = foo * fo3;

                                            String hangeramt = Float.toString(xy);

                                            DataFish2 s2 = new DataFish2(type,quantity,hangerpric,hangeramt,hangerpric);
                                            hangerlist.add(s2);

                                        }

                                        else if (fourdour.contains(type)){

                                            Log.e("exist", "exist");


                                            for (int i=0;i<filterdata2.size();i++){


                                                if (filterdata2.get(i).item.equalsIgnoreCase(type)){


                                                    Float foo1 = Float.parseFloat(quantity);
                                                    Float fo1 = Float.parseFloat(price);
                                                    Float dd = Float.parseFloat(filterdata2.get(i).amt);
                                                    Float dd2 = Float.parseFloat(filterdata2.get(i).noofpieces);

                                                    s = (foo1+dd2)*fo1;

                                                    float sss = foo1+dd2;


                                                    DataFish2 ss = new DataFish2(type, String.valueOf(Math.round(sss)), price, String.valueOf(s),hangerpric);
                                                    filterdata2.set(i,ss);


                                                    Float fo3 = Float.parseFloat(hangerpric);
                                                    //  Float dd3 = Float.parseFloat(filterdata2.get(i).noofpieces);
                                                    s = (foo1+dd2)*fo3;



                                                    DataFish2 ss2 = new DataFish2(type, String.valueOf(Math.round(sss)), hangerpric, String.valueOf(s),hangerpric);
                                                    hangerlist.set(i,ss2);
                                                    break;

                                                }
                                            }

                                        }

                                        else {

                                            fourdour.add(type);

                                            Log.e("noexist", "noexist");

                                            Float foo = Float.parseFloat(quantity);
                                            Float fo2 = Float.parseFloat(price);
                                            Float x = foo * fo2;
                                            amount =Float.toString(x);
                                            Log.e(type,quantity+price+amount);
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount,hangerpric);
                                            filterdata2.add(ss);
                                            Float fo3 = Float.parseFloat(hangerpric);
                                            Float x2 = foo * fo3;
                                            amount =Float.toString(x2);

                                            DataFish2 ss2 = new DataFish2(type, quantity, hangerpric, amount,hangerpric);
                                            hangerlist.add(ss2);
                                            AddtoList();

                                        }

                                        AddtoList();

                                    }


                                }
                            });

                            //  Displaylist();

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });



    }

    private void setspinner() {
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);
    }


    private void AddtoList() {


        qty.setText("");

        if (foldtype.equalsIgnoreCase("normal")){

            Adapter = new AdapterFish(ExistingData.this,filterdata2);
            float sum = 0;
            for (int i = 0; i < filterdata2.size(); i++) {

                Float dd = Float.parseFloat(filterdata2.get(i).amt);
                sum += dd;
            }

            //  btmamt.setText("Sub Total = " +String.valueOf(sum));

            s =  ((0.0/100) *sum)+sum;
            btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
        }
        else {

            Adapter = new AdapterFish(ExistingData.this,hangerlist);

            float sum = 0;
            for (int i = 0; i < hangerlist.size(); i++) {

                Float dd = Float.parseFloat(hangerlist.get(i).amt);
                sum += dd;
            }

            //  btmamt.setText("Sub Total = " +String.valueOf(sum));

            s =  ((0.0/100) *sum)+sum;
            btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
        }



        //Adapter = new AdapterFish(ExistingData.this,filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,false));
//        float sum = 0;
//        for (int i = 0; i < filterdata2.size(); i++) {
//
//            Float dd = Float.parseFloat(filterdata2.get(i).amt);
//            sum += dd;
//        }
//        //  btmamt.setText("Sub Total = " +String.valueOf(sum));
//        s =  ((0/100) *sum)+sum;
//        btmtotal.setText("Total " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));

        pay.setVisibility(View.VISIBLE);

        if (exprsval.equalsIgnoreCase("1")){
            checkBox.setChecked(true);
            expresscharge=getIntent().getDoubleExtra("expressDeliveryCharge",0);
            btmtotal.setText("Total " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));

//            checkBox.setVisibility(View.GONE);
//            expresstxt.setVisibility(View.GONE);
        }

        else {

            exprsval = "0";
        }
    }

    private void getjoborder() {

        pd = new ProgressDialog(ExistingData.this);
        pd.setMessage("Getting Job Orders..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
            postdat.put("serviceName",serviceName);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getJobOrder")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ExistingData.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = openDialog.findViewById(R.id.cancel);
                        dialogno.setText("OK");
                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });
                        openDialog.show();
                        openDialog.setCancelable(false);
                    }
                });


                mMessage2 = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();
                mMessage2 = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Log.e("Resy",mMessage2);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            //   TraverseData();

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage2);

                                Double statuscode = jsonObject.optDouble("statusCode");
                                Double jobid = jsonObject.optDouble("jobid");

                                int fold = jsonObject.getInt("deliverOnHanger");

                                Gson gson = new Gson();

                                JobOrder jobOrder = gson.fromJson(mMessage2,JobOrder.class);

                                Log.e("foldkey", String.valueOf(fold));
                                if (fold==0){


                                    chkboxhanger.setChecked(false);
                                    chkboxhanger.jumpDrawablesToCurrentState();

                                    foldtype = "normal";

                                    for (int i= 0; i<jobOrder.getCategory().size(); i++){



                                        fourdour.add(jobOrder.getCategory().get(i));
                                        DataFish2 ss = new DataFish2("item","qty","price","total","hangerprice");

                                        Float ss2 = Float.parseFloat(jobOrder.getPrice().get(i));

                                        Float ss3 =  Float.parseFloat(jobOrder.getQuantity().get(i));
                                        Float ss4 = ss2 * ss3;
                                        DataFish2 sds = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),jobOrder.getPrice().get(i),String.valueOf(ss4),jobOrder.getHangerPrice());
                                        filterdata2.add(sds);



                                        for (int k= 0; k<items.size(); k++){

                                            if (jobOrder.getCategory().get(i).equalsIgnoreCase(items.get(k))){


                                                Log.e("filterresult",jobOrder.getCategory().get(i));



                                                Float sssss =  Float.parseFloat(hangerPrice.get(k));

                                                DataFish2 sds2 = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),hangerPrice.get(k),String.valueOf(sssss*ss3),jobOrder.getHangerPrice());
                                                hangerlist.add(sds2);

                                            }


                                        }
                                    }


                                }
                                else {

                                    chkboxhanger.setChecked(true);
                                    chkboxhanger.jumpDrawablesToCurrentState();

                                    foldtype = "hanger";

                                    for (int i= 0; i<jobOrder.getCategory().size(); i++){



                                        fourdour.add(jobOrder.getCategory().get(i));
                                        DataFish2 ss = new DataFish2("item","qty","price","total","hangerprice");

                                        Float ss2 = Float.parseFloat(jobOrder.getPrice().get(i));

                                        Float ss3 =  Float.parseFloat(jobOrder.getQuantity().get(i));
                                        Float ss4 = ss2 * ss3;
                                        DataFish2 sds = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),jobOrder.getPrice().get(i),String.valueOf(ss4),jobOrder.getHangerPrice());
                                        hangerlist.add(sds);



                                        for (int k= 0; k<items.size(); k++){

                                            if (jobOrder.getCategory().get(i).equalsIgnoreCase(items.get(k))){


                                                Log.e("filterresult",jobOrder.getCategory().get(i));



                                                Float sssss =  Float.parseFloat(prize.get(k));

                                                DataFish2 sds2 = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),prize.get(k),String.valueOf(sssss*ss3),jobOrder.getHangerPrice());
                                                filterdata2.add(sds2);

                                            }


                                        }
                                    }
                                }







//                                String s = jobOrder.getCustomerId();

//                                filterdata2.add(jobOrder);



                                AddtoList();




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            Adapter = new AdapterFish(ExistingData.this, filterdata2);
//                            Adapter.setHasStableIds(false);
//                            mRVFishPrice.setAdapter(Adapter);
//                            mRVFishPrice.setHasFixedSize(false);
//                            //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//                            //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
//                            mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,true));
//
//
//
//                            float sum = 0;
//                            for (int i = 0; i < filterdata2.size(); i++) {
//
//                                Float dd = Float.parseFloat(filterdata2.get(i).amt);
//                                sum += dd;
//                            }
//
//                            //  btmamt.setText("Sub Total = " +String.valueOf(sum));
//
//                            s =  ((0/100) *sum)+sum;
//                            btmtotal.setText("Total = " +getResources().getString(R.string.rupee)+String.valueOf(s));
//
//                            pay.setVisibility(View.VISIBLE);

                        }
                    });




                }

                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }


    public class DataFish2 {
        public String item;
        public String noofpieces;
        public String cost;
        public String amt;
        public String hangerprice;


        public DataFish2(String item,String noofpieces,String cost,String amt,String hangerprice){

            this.item = item;
            this.noofpieces = noofpieces;
            this.cost = cost;
            this.amt = amt;
            this.hangerprice = hangerprice;
        }

    }

    public class DataFish {


        public String Did;
        public String Dcategory;
        public String Dprice;
        public String Dhangerprice;


        public DataFish(String did, String dcategory, String dprice,String Dhangerprice) {
            Did = did;
            Dcategory = dcategory;
            Dprice = dprice;
            Dhangerprice = Dhangerprice;
        }



    }

    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish2> data2 = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish2> data5) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data2 = data5;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.rowform, parent, false);
            final MyHolder holder = new MyHolder(view);
            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final MyHolder myHolder = (MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);



            myHolder.item.setText(current.item);
            myHolder.noofpices.setText(current.noofpieces);
            myHolder.cost.setText(current.cost);
            myHolder.amount.setText(current.amt);

            myHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                   Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();


                    fourdour.remove(current.item);
                    filterdata2.remove(position);
                    hangerlist.remove(position);

                    AddtoList();

                    //  dd.add(current.item);
                    //                  tarif.add("fsd","rtt","trer");
                    //                 Adapter.notifyDataSetChanged();

//                    Adapter = new AdapterFish(ExistingData.this, filterdata2);
//                    Adapter.setHasStableIds(false);
//                    mRVFishPrice.setAdapter(Adapter);
//                    mRVFishPrice.setHasFixedSize(false);
//                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,false));
//                    float sum = 0;
//                    for (int i = 0; i < filterdata2.size(); i++) {
//
//                        Float dd = Float.parseFloat(filterdata2.get(i).amt);
//                        sum += dd;
//                    }
//
//                    //  btmamt.setText("Sub Total = " +String.valueOf(sum));
//
//                    s =  ((0/100) *sum)+sum;
//                    btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.valueOf(s+expresscharge));

                }
            });

            myHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //                   Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final EditText edittext = new EditText(context);
                    builder.setView(edittext);
                    builder.setMessage("Update Quantity")
                            .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    String YouEditTextValue = edittext.getText().toString();
                                    //                                    Toast.makeText(context, YouEditTextValue, Toast.LENGTH_SHORT).show();
                                    try {
                                        // int num = Integer.parseInt(YouEditTextValue);
                                        // Log.i("",num+" is a number");

                                        String normalrate = null;
                                        String hangerrate = null;
                                        for (int k = 0; k < items.size(); k++) {

                                            if (items.get(k).equalsIgnoreCase(current.item)) {


                                                hangerrate = hangerPrice.get(k);
                                                normalrate = prize.get(k);
                                            }
                                        }

                                        Float foo = Float.parseFloat(YouEditTextValue);
                                        Float fo2 = Float.parseFloat(normalrate);


                                        //  Log.e("first item update", current.cost + current.hangerprice);
                                        Float x = foo * fo2;
                                        String suu = Float.toString(x);


                                        Float fo22 = Float.parseFloat(hangerrate);
                                        Float x2 = foo * fo22;
                                        String su = Float.toString(x2);


                                        hangerlist.set(position, new DataFish2(current.item, YouEditTextValue, hangerrate, su, hangerrate));

                                        filterdata2.set(position, new DataFish2(current.item, YouEditTextValue, normalrate, suu, hangerrate));

                                        AddtoList();
                                        //Adapter = new AdapterFish(ExistingData.this, filterdata2);
//                                        Adapter.setHasStableIds(false);
//                                        mRVFishPrice.setAdapter(Adapter);
//                                        mRVFishPrice.setHasFixedSize(false);
//                                        mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,false));
//                                        float sum = 0;
//                                        for (int i = 0; i < filterdata2.size(); i++) {
//
//                                            Float dd = Float.parseFloat(filterdata2.get(i).amt);
//                                            sum += dd;
//                                        }
//
//                                        //  btmamt.setText("Sub Total = " +String.valueOf(sum));
//
//                                        s =  ((0.0/100) *sum)+sum;
//                                        btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.valueOf(s+expresscharge));
                                        Log.e("rererer", String.valueOf(s));
                                    } catch (NumberFormatException e) {
                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"Enter only numbers",Snackbar.LENGTH_SHORT);
                                        snackbar.show();

                                        //  Toast.makeText(context, "Enter only numbers", Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });
                    builder.show();
                    // filterdata2.set(position, new DataFish2(current.item,"XX","XYZ"));


                }
            });



        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView item;
            TextView noofpices;
            TextView cost;
            TextView amount;
            Button plus;
            ImageButton minus;
            ImageButton delete;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.item);
                noofpices = itemView.findViewById(R.id.noofpices);
                cost = itemView.findViewById(R.id.cost);
                amount = itemView.findViewById(R.id.total);
                plus = itemView.findViewById(R.id.plus);
               // minus = itemView.findViewById(R.id.minus);
                delete = itemView.findViewById(R.id.del);
                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExistingData.this,Dashpage.class);
        startActivity(intent);
    }
}

