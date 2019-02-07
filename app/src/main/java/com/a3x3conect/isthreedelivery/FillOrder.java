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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toast;

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

public class FillOrder extends AppCompatActivity {

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    JSONArray jsonArray;
    ListView plist;
    List<Tariff> tarif;
    ProgressDialog pd;
    TextView ratescard;
    String mMessage2,hangerprice;
    float garmentscount = 0;
    int spinerposition;
    RecyclerView mRVFishPrice,mRVFishPrice2;
    String  hangeramt;
    int deliveronhangetkey;

    private AdapterFish2 Adapter2;
    ArrayList<DataFish> filterdata=new ArrayList<DataFish>();
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();

    List<DataFish2> hangerlist=new ArrayList<DataFish2>();
    ArrayList<String> hangerprize = new ArrayList<>();
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> prize = new ArrayList<>();
    ArrayList<String> items2 = new ArrayList<>();
    ArrayList<String> prize2 = new ArrayList<>();
    ArrayList<String> fourdour = new ArrayList<>();
    ArrayList<String> rates = new ArrayList<>();
//    static String[][][] School= new String[10][10][10];


    String mMessage;
    Button pay;
    TinyDB tinyDB;
    double s=0,expresscharge=0,minimumvalue=0;;
    String expressDelivery,deliverOnHanger;
    ListView lv_languages;
    CheckBox checkBox;

    String price,type,quantity,amount,idd,serviceName;
    TextView btmamt,btmtotal;
    TableLayout tableLayout;
    String foldtype = "normal";
    Snackbar snackbar;
    Spinner spinner;
    ListView listView;
    EditText qty;
    Button add,cancel;
CheckBox chkboxhanger;
TextView hangertxt;

    BottomSheetDialog bottomSheetDialog;
    Integer spinposition;
    ArrayAdapter<String> adapter;
    final ArrayList<String> dd = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_order);
        tinyDB = new TinyDB(this);
        pay = findViewById(R.id.pay);
        btmtotal = findViewById(R.id.btmtotal);
        checkBox = findViewById(R.id.checkBox);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Form");
       expressDelivery = getIntent().getStringExtra("expressDelivery");


        serviceName = getIntent().getStringExtra("serviceName");
        deliverOnHanger = getIntent().getStringExtra("deliverOnHanger");
        expresscharge = getIntent().getDoubleExtra("expressDeliveryCharge",0);

        tinyDB.putDouble("expressDeliveryCharge",getIntent().getDoubleExtra("expressDeliveryCharge",0));
        chkboxhanger = findViewById(R.id.chkboxhanger);

        hangertxt = findViewById(R.id.hangertxt);

        if (serviceName.equalsIgnoreCase("dryCleaning")){


            chkboxhanger.setVisibility(View.GONE);
            hangertxt.setVisibility(View.GONE);
        }


            Log.e("values", String.valueOf(expressDelivery)+String.valueOf(expresscharge));
        //listView = new ListView(this);

        //  String[] rates = {"shirt","pant",};

//        if (expressDelivery.equalsIgnoreCase("1")){
//
//            //  expresscharge=tinyDB.getDouble("expresscharge",0);
//            //btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
//
//            checkBox.setChecked(true);
////            checkBox.setVisibility(View.GONE);
////            expresstxt.setVisibility(View.GONE);
//        }


        if (expressDelivery.equalsIgnoreCase("1")){
            checkBox.setChecked(true);
            //expresscharge=tinyDB.getDouble("expressDeliveryCharge",0);
            expresscharge=tinyDB.getDouble("expressDeliveryCharge",0);
            btmtotal.setText("Total " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
            //            checkBox.setVisibility(View.GONE);
            //            expresstxt.setVisibility(View.GONE);
        }

        else {

            expressDelivery = "0";
            expresscharge=0;
        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Express Delivery Enabled",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                  //  Toast.makeText(FillOrder.this, "Express Delivery Enabled", Toast.LENGTH_SHORT).show();
                   // expresscharge = getIntent().getDoubleExtra("expressDeliveryCharge",0);
                    expressDelivery = "1";
                    expresscharge=tinyDB.getDouble("expressDeliveryCharge",0);
                    // expresscharge=tinyDB.getDouble("expresscharge",0);
                    btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
                }
                else {
                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Express Delivery Disabled",Snackbar.LENGTH_SHORT);
                    snackbar.show();

                   // Toast.makeText(FillOrder.this, "Express Delivery Disabled", Toast.LENGTH_SHORT).show();

                    expressDelivery = "0";
                    expresscharge=0;
                    btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
                }
            }
        });

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
        // listView.setAdapter(adapter);
        ratescard = findViewById(R.id.rates);

        ratescard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FillOrder.this,android.R.layout.simple_list_item_1,rates);

                View view = getLayoutInflater().inflate(R.layout.farelistitemrow, null);

//                TextView textView = (TextView)view.findViewById(android.R.id.text1);
//
//                textView.setTextColor(getResources().getColor(R.color.bluee));
                lv_languages = view.findViewById(R.id.lv_languages);
                lv_languages.setAdapter(adapter);
                bottomSheetDialog = new BottomSheetDialog(FillOrder.this);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

            }
        });
        spinner  = findViewById(R.id.spinner);
        qty = findViewById(R.id.qty);
        add = findViewById(R.id.add);
        cancel = findViewById(R.id.cancel);

        mRVFishPrice2 = findViewById(R.id.fishPriceList2);
        btmamt = findViewById(R.id.btmamt);
        tableLayout = findViewById(R.id.tabl);
       // tableLayout.setVisibility(View.GONE);

        pay.setVisibility(View.VISIBLE);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog openDialog = new Dialog(FillOrder.this);
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

                openDialog.setCancelable(false);

                openDialog.show();

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterdata2.isEmpty()){

                    Toast.makeText(FillOrder.this, "Please fill the form ", Toast.LENGTH_SHORT).show();
                }

                else {

                    if (minimumvalue==0){
                        minimumordervalue();
                    }

                    else if (minimumvalue>s){
                        final Dialog openDialog = new Dialog(FillOrder.this);
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
                                Intent intent = new Intent(FillOrder.this,Dashpage.class);
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

        getjoborder();
    }

    private void minimumordervalue() {


        pd = new ProgressDialog(FillOrder.this);
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
                        final Dialog openDialog = new Dialog(FillOrder.this);
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

                mMessage = response.body().string();
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

                                    final Dialog openDialog = new Dialog(FillOrder.this);
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
                                            Intent intent = new Intent(FillOrder.this,Dashpage.class);
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

                                        final Dialog openDialog = new Dialog(FillOrder.this);
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
//                                         Intent intent = new Intent(ScheduleFillOrder.this,Pickup.class);
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
                                                Intent intent = new Intent(FillOrder.this,Dashpage.class);
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


    private void Cancelschedule() {

        pd = new ProgressDialog(FillOrder.this);
        pd.setMessage("Cancel Order");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
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
                        final Dialog openDialog = new Dialog(FillOrder.this);
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

                        openDialog.setCancelable(false);

                        openDialog.show();

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

                            Log.e("cancel",mMessage);

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);

                                if(jsonObject.getString("statusCode").equalsIgnoreCase("1")){


                                    final Dialog openDialog = new Dialog(FillOrder.this);
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
                                            Intent intent = new Intent(FillOrder.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });


                                    openDialog.setCancelable(false);
                                    openDialog.show();
                                }

                                else {

                                    final Dialog openDialog = new Dialog(FillOrder.this);
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

                                    openDialog.setCancelable(false);

                                    openDialog.show();

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

    private void getjoborder() {

        pd = new ProgressDialog(FillOrder.this);
        pd.setMessage("Getting Job Orders..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
            postdat.put("serviceName",serviceName);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdat.toString());
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
                        final Dialog openDialog = new Dialog(FillOrder.this);
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


                mMessage2 = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();
                mMessage2 = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Log.e("Resy", mMessage2);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            //   TraverseData();

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage2);

                                Double statuscode = jsonObject.optDouble("statusCode");
                                Double jobid = jsonObject.optDouble("jobid");


                                if (statuscode == 0) {


                                    //                                   Toast.makeText(Puckup.this, "Please fill the form", Toast.LENGTH_SHORT).show();
                                }

                                if (jobid > 0) {

                                    Log.e("jobid", String.valueOf(jsonObject.getDouble("jobid")));

//
//                                    Intent intent = new Intent(FillOrder.this, ExistingData.class);
//                                    startActivity(intent);

                                    //                                   Toast.makeText(Puckup.this, "Form Data Exists", Toast.LENGTH_SHORT).show();
                                }

                                else {

                                    GetFormData();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }

    private void GetFormData() {


        pd = new ProgressDialog(FillOrder.this);
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
                        final Dialog openDialog = new Dialog(FillOrder.this);
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

//                                                //                                          Toast.makeText(FillOrder.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(FillOrder.this,Dashpage.class);
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

                mMessage = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.e("result",mMessage);
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
                                JSONArray array = new JSONArray(mMessage);
                                for(int j = 0; j < array.length(); j++){

                                    JSONObject json_data = array.getJSONObject(j);

                                    rates.add(json_data.getString("category")+" :  "+getResources().getString(R.string.rupee)+json_data.getString("price"));
                                    items.add(json_data.getString("category"));
                                    prize.add(json_data.getString("price"));
                                    hangerprize.add(json_data.getString("hangerPrice"));
                                    items2.add(json_data.getString("category"));
                                    prize2.add(json_data.getString("price"));
                                    Log.e("Dta",dd.toString());
                                }


                                setspinner();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);

                                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#163053"));
                                    spinerposition = position;
//                                    price = prize.get(position);
//                                    //  type = items.get(position);
//
//                                    type = filterdata.get(position).Dcategory;
//                                    idd = filterdata.get(position).Did;


                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });




                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    quantity = qty.getText().toString();
                                    type = filterdata.get(spinerposition).Dcategory;
                                    idd = filterdata.get(spinerposition).Did;
                                    price = filterdata.get(spinerposition).Dprice;
                                    hangerprice = filterdata.get(spinerposition).Dhangerprice;


                                    if (TextUtils.isEmpty(quantity)){
                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"Please Enter Quantity",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
 //                                       Toast.makeText(FillOrder.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                                        //myHolder.qty.setError("empty");
                                    }
                                    else if (quantity.equalsIgnoreCase("0")){
                                        //Toast.makeText(FillOrder.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"Please Enter Quantity",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }
                                    else if (items.isEmpty()){
                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"List is Empty",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        //Toast.makeText(getApplicationContext(), "List is Empty", Toast.LENGTH_SHORT).show();
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
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount,idd,hangerprice);
                                            filterdata2.add(ss);
                                            Float fo3 = Float.parseFloat(hangerprice);
                                            Float xy = foo * fo3;

                                            hangeramt = Float.toString(xy);

                                            DataFish2 s2 = new DataFish2(type,quantity,hangerprice,hangeramt,idd,hangerprice);
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

                                                    DataFish2 ss = new DataFish2(type, String.valueOf(Math.round(sss)), price, String.valueOf(s),idd,hangerprice);
                                                    filterdata2.set(i,ss);

                                                    Float fo3 = Float.parseFloat(hangerprice);
                                                    //  Float dd3 = Float.parseFloat(filterdata2.get(i).noofpieces);
                                                    s = (foo1+dd2)*fo3;



                                                    DataFish2 ss2 = new DataFish2(type, String.valueOf(Math.round(sss)), hangerprice, String.valueOf(s),idd,hangerprice);
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
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount,idd,hangerprice);
                                            filterdata2.add(ss);
                                            Float fo3 = Float.parseFloat(hangerprice);
                                            Float x2 = foo * fo3;
                                            amount =Float.toString(x2);

                                            DataFish2 ss2 = new DataFish2(type, quantity, hangerprice, amount,idd,hangerprice);
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


    public class DataFish {


        public String Did;
        public String Dcategory;
        public String Dprice;
        public String Dhangerprice;


        public DataFish(String did, String dcategory, String dprice,String dhangerprice)  {
            Did = did;
            Dcategory = dcategory;
            Dprice = dprice;
            Dhangerprice = dhangerprice;
        }



    }

    public class DataFish2 {
        public String item;
        public String noofpieces;
        public String cost;
        public String amt;
        public String id;
        public String hangerpric;


        public DataFish2(String item,String noofpieces,String cost,String amt,String id,String hangerpric){

            this.item = item;
            this.noofpieces = noofpieces;
            this.cost = cost;
            this.amt = amt;
            this.id = id;
            this.hangerpric = hangerpric;
        }

    }

    private void AddtoList() {

        qty.setText("");

        qty.clearFocus();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);



        //   Log.e("ononcontains","oncontains");          // Log.e(u,u);
        if (foldtype.equalsIgnoreCase("normal")){

            Adapter2 = new AdapterFish2(FillOrder.this,filterdata2);
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

            Adapter2 = new AdapterFish2(FillOrder.this,hangerlist);

            float sum = 0;
            for (int i = 0; i < hangerlist.size(); i++) {

                Float dd = Float.parseFloat(hangerlist.get(i).amt);
                sum += dd;
            }

            //  btmamt.setText("Sub Total = " +String.valueOf(sum));

            s =  ((0.0/100) *sum)+sum;
            btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
        }
        Adapter2.setHasStableIds(false);
        mRVFishPrice2.setAdapter(Adapter2);
        mRVFishPrice2.setHasFixedSize(false);
        mRVFishPrice2.setLayoutManager(new LinearLayoutManager(FillOrder.this,LinearLayoutManager.VERTICAL,false));
//        float sum = 0;
//        for (int i = 0; i < filterdata2.size(); i++) {
//
//            Float dd = Float.parseFloat(filterdata2.get(i).amt);
//            sum += dd;
//        }
//
//        //  btmamt.setText("Sub Total = " +String.valueOf(sum));
//
//        s =  ((0.0/100) *sum)+sum;
//
//
//
//            btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge   ));

        if (expressDelivery.equalsIgnoreCase("1")){
            checkBox.setChecked(true);
          //  expresscharge=tinyDB.getDouble("expresscharge",0);
            expresscharge=tinyDB.getDouble("expressDeliveryCharge",0);
            btmtotal.setText("Total " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
            //            checkBox.setVisibility(View.GONE);
            //            expresstxt.setVisibility(View.GONE);
        }

        else {

            expressDelivery = "0";
            expresscharge=0;
        }

        pay.setVisibility(View.VISIBLE);
        btmtotal.setVisibility(View.VISIBLE);
        tableLayout.setVisibility(View.VISIBLE);
        pay.setVisibility(View.VISIBLE);


    }

    public class AdapterFish2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish2> data2 = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish2(Context context, List<DataFish2> data5) {
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

//
//                    prize.add(current.cost);
//                    items.add(current.item);
//                    setspinner();

                    filterdata2.remove(position);
                    hangerlist.remove(position);
                    fourdour.remove(current.item);

                    if (foldtype.equalsIgnoreCase("normal")){


                        Adapter2 = new AdapterFish2(FillOrder.this, filterdata2);
                        Adapter2.setHasStableIds(false);
                        mRVFishPrice2.setAdapter(Adapter2);
                        mRVFishPrice2.setHasFixedSize(false);
                        mRVFishPrice2.setLayoutManager(new LinearLayoutManager(FillOrder.this,LinearLayoutManager.VERTICAL,false));
                        float sum = 0;
                        for (int i = 0; i < filterdata2.size(); i++) {

                            Float dd = Float.parseFloat(filterdata2.get(i).amt);
                            sum += dd;
                        }
                        Log.e("rererer", String.valueOf(sum));
                        //btmamt.setText("Sub Total = " +String.valueOf(sum));

                        s =  ((0.0/100) *sum)+sum;
                        btmtotal.setText("Total  " +getResources().getString(R.string.rupee) +String.format("%.2f",s+expresscharge));
                    }

                    else {


                        Adapter2 = new AdapterFish2(FillOrder.this, hangerlist);
                        Adapter2.setHasStableIds(false);
                        mRVFishPrice2.setAdapter(Adapter2);
                        mRVFishPrice2.setHasFixedSize(false);
                        mRVFishPrice2.setLayoutManager(new LinearLayoutManager(FillOrder.this,LinearLayoutManager.VERTICAL,false));
                        float sum = 0;
                        for (int i = 0; i < hangerlist.size(); i++) {

                            Float dd = Float.parseFloat(hangerlist.get(i).amt);
                            sum += dd;
                        }
                        Log.e("rererer", String.valueOf(sum));
                        //btmamt.setText("Sub Total = " +String.valueOf(sum));

                        s =  ((0.0/100) *sum)+sum;
                        btmtotal.setText("Total  " +getResources().getString(R.string.rupee) +String.format("%.2f",s+expresscharge));
                    }


                    //  dd.add(current.item);
                    //                  tarif.add("fsd","rtt","trer");
                    //                 Adapter.notifyDataSetChanged();

//                    Adapter2 = new AdapterFish2(FillOrder.this, filterdata2);
//                    Adapter2.setHasStableIds(false);
//                    mRVFishPrice2.setAdapter(Adapter2);
//                    mRVFishPrice2.setHasFixedSize(false);
//                    mRVFishPrice2.setLayoutManager(new LinearLayoutManager(FillOrder.this,LinearLayoutManager.VERTICAL,false));
//                    float sum = 0;
//                    for (int i = 0; i < filterdata2.size(); i++) {
//
//                        Float dd = Float.parseFloat(filterdata2.get(i).amt);
//                        sum += dd;
//                    }
//                    Log.e("rererer", String.valueOf(sum));
//                    //btmamt.setText("Sub Total = " +String.valueOf(sum));
//
//                    double s =  ((0.0/100) *sum)+sum;
//                    btmtotal.setText("Total  " +getResources().getString(R.string.rupee) +String.format("%.2f",s+expresscharge));
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
                                    String YouEditTextValue = edittext.getText().toString();
                                    edittext.setInputType(InputType.TYPE_CLASS_NUMBER |
                                            InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                            InputType.TYPE_NUMBER_FLAG_SIGNED);
//                                    Toast.makeText(context, YouEditTextValue, Toast.LENGTH_SHORT).show();
                                    try {

                                        int num = Integer.parseInt(YouEditTextValue);
                                        Log.i("",num+" is a number");

                                        Float foo = Float.parseFloat(YouEditTextValue);
                                        Float fo2 = Float.parseFloat(current.cost);
                                        Float x = foo * fo2;
                                        String suu =Float.toString(x);

                                        Float fo22 = Float.parseFloat(current.hangerpric);
                                        Float x2 = foo * fo22;
                                        String suu2 =Float.toString(x2);

                                        hangerlist.set(position,new DataFish2(current.item,YouEditTextValue,current.hangerpric,suu2,current.id,current.hangerpric));



                                        filterdata2.set(position, new DataFish2(current.item,YouEditTextValue,current.cost,suu,current.id,hangerprice));

                                        AddtoList();

//                                        int num = Integer.parseInt(YouEditTextValue);
//                                        Log.i("",num+" is a number");
//
//                                        Float foo = Float.parseFloat(YouEditTextValue);
//                                        Float fo2 = Float.parseFloat(current.cost);
//                                        Float x = foo * fo2;
//                                        String suu =Float.toString(x);
//
//
//                                        filterdata2.set(position, new DataFish2(current.item,YouEditTextValue,current.cost,suu,current.id,hangerprice));
//                                        Adapter2 = new AdapterFish2(FillOrder.this, filterdata2);
//                                        Adapter2.setHasStableIds(false);
//                                        mRVFishPrice2.setAdapter(Adapter2);
//                                        mRVFishPrice2.setHasFixedSize(false);
//                                        mRVFishPrice2.setLayoutManager(new LinearLayoutManager(FillOrder.this,LinearLayoutManager.VERTICAL,false));
//                                        float sum = 0;
//                                        for (int i = 0; i < filterdata2.size(); i++) {
//
//                                            Float dd = Float.parseFloat(filterdata2.get(i).amt);
//                                            sum += dd;
//                                        }
//
//                                        //  btmamt.setText("Sub Total = " +String.valueOf(sum));
//
//                                        double s =  ((0.0/100) *sum)+sum;
//                                        btmtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expresscharge));
                                        Log.e("rererer", String.format("%.2f",s));
                                    } catch (NumberFormatException e) {

                                        View parentLayout = findViewById(android.R.id.content);
                                        snackbar = Snackbar.make(parentLayout,"Enter only numbers",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                       // Toast.makeText(context, "Enter only numbers", Toast.LENGTH_SHORT).show();
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
                //minus = (ImageButton)itemView.findViewById(R.id.minus);
                delete = itemView.findViewById(R.id.del);
                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }

    private void Paydata() {


        pd = new ProgressDialog(FillOrder.this);
        pd.setMessage("Creating your Order");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        JSONArray itemType = new JSONArray();
        JSONArray unitPrice = new JSONArray();
        JSONArray subTotal = new JSONArray();
        JSONArray quantity = new JSONArray();


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

        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        try {
            //  postdat.put("status", "FillOrder-CONFIRMED");
            postdat.put("customerId",tinyDB.getString("custid"));
            postdat.put("expressDelivery",expressDelivery);
           // postdat.put("expresscharge",expresscharge);
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
                .url(getString(R.string.baseurl)+"createJobOrder")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                pd.cancel();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(FillOrder.this);
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

                mMessage = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.e("result",mMessage);

//                Log.e("resstsy",response.body().string());
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonResponse = null;
                            try {
                                jsonResponse = new JSONObject(mMessage);

                                if (jsonResponse.getString("statusCode").equalsIgnoreCase("0")){

                                    final Dialog openDialog = new Dialog(FillOrder.this);
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

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });
                                    openDialog.setCancelable(false);
                                    openDialog.show();
                                }

                                else if (jsonResponse.getString("statusCode").equalsIgnoreCase("1")){



                                    final Dialog openDialog = new Dialog(FillOrder.this);
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

                                            //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(FillOrder.this,SummaryReport.class);
                                            startActivity(intent);
                                        }
                                    });
                                    //
                                    //  Log.e("json",sss);
                                    openDialog.setCancelable(false);
                                    openDialog.show();
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
