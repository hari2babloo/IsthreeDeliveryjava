package com.a3x3conect.isthreedelivery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a3x3conect.isthreedelivery.Models.JobOrder;
import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.google.gson.Gson;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SummaryReport extends AppCompatActivity {


    ProgressDialog pd;
    RecyclerView mRVFishPrice;
    TableLayout tableLayout;
    EditText weight;
    TextView editorder;
    TextView btmtotal,expresmsg;
    String serviceName;
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();
    private AdapterFish Adapter;
    double expressDeliveryCharge;
    Button home,cancelorder;
    TextView weighttxt,weightperkg;
    double s;
    Button washbtn;
    TinyDB tinyDB;
    String deliveronhangerkey;
    String mMessage2,mMessage,radiostatus,grandtotaltxt;


    Float fweight,frate, amountforweight;

    TextView washingcost,hangerstatus,deliverontxt;
    String expressDelivery;
    JobOrder jobOrder;
    LinearLayout layoutweight;
    TableLayout layouttxt;
    float garmentscount = 0;
    float sum = 0;
    String rate,s2="0";

    TextView grdtotal,date,custid,status,jobidtxt;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tinyDB = new TinyDB(SummaryReport.this);
        home = findViewById(R.id.home);
        cancelorder = findViewById(R.id.cancel);
        grdtotal = findViewById(R.id.grdtotal);
        jobidtxt = findViewById(R.id.jobid);
        custid = findViewById(R.id.custid);
        date = findViewById(R.id.date);
        washbtn = findViewById(R.id.washbtn);
        weight = findViewById(R.id.weight2);
        editorder = findViewById(R.id.editorder);
        //weight.setText("0");
        weighttxt = findViewById(R.id.weighttxt);
        washingcost = findViewById(R.id.washingcost);
        hangerstatus = findViewById(R.id.hangerstatus);
        deliverontxt = findViewById(R.id.deliverontxt);
        layoutweight = findViewById(R.id.weight);
        layouttxt = findViewById(R.id.layouttxt);
        layouttxt.setVisibility(View.GONE);
        weightperkg = findViewById(R.id.weightperkg);
        //  status = (TextView)findViewById(R.id.delstatus);
        expresmsg = findViewById(R.id.expresmsg);

        editorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SummaryReport.this,ExistingData.class);

                intent.putExtra("expressDelivery",expressDelivery);
                intent.putExtra("expressDeliveryCharge",expressDeliveryCharge);
                intent.putExtra("serviceName",serviceName);
                intent.putExtra("deliverOnHanger",deliveronhangerkey);
                Log.e("expressDelivery",expressDelivery);

                startActivity(intent);
            }
        });
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"Address does not exist","Phone number not reachable","Customer does not exist","Customer not at home","Issue not listed"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(SummaryReport.this);//ERROR ShowDialog cannot be resolved to a type
                builder.setTitle("Select a Status");
                builder.setSingleChoiceItems(items, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                radiostatus = items[item];
                                //  Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dialog.cancel();

                    }
                });
                builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (radiostatus!=null && !radiostatus.isEmpty()){
                            Submitstatus();
                            dialog.dismiss();
                            dialog.cancel();
                        }
                        else {

                            builder.show();
                            Toast.makeText(SummaryReport.this, "Select a Status", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                builder.show();
            }
        });


        washbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (weight.getText().toString().isEmpty() || weight.getText().toString()==null){


                    Toast.makeText(SummaryReport.this, "Enter Weight", Toast.LENGTH_SHORT).show();
                }

                else{

                getwashrates();

                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // home.setEnabled(false);




                if (jobOrder.getServiceName().equalsIgnoreCase("washAndPress")){

                    if (weight.getText().toString().isEmpty() || weight.getText().toString()==null){


                        Toast.makeText(SummaryReport.this, "Enter Weight and Update", Toast.LENGTH_SHORT).show();
                    }
                    else if (s2.equalsIgnoreCase("0")){

                        Toast.makeText(SummaryReport.this, "Enter Weight and Update", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        Invoice();

                    }

                }


                else {

                    Invoice();
                }







            }
        });
        mRVFishPrice = findViewById(R.id.fishPriceList);
        tableLayout = findViewById(R.id.tabl);
        btmtotal = findViewById(R.id.btmtotal);

        //   filterdata2 =   (ArrayList<DataFish2>)getIntent().getSerializableExtra("FILES_TO_SEND");

        getjoborder();
    }
    private void getwashrates() {

        pd = new ProgressDialog(SummaryReport.this);
        pd.setMessage("Getting Wash rates..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("postdata",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getWashingCharge")
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {


                pd.cancel();
                pd.dismiss();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(SummaryReport.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Something Went Wrong");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = openDialog.findViewById(R.id.cancel);
                        dialogno.setText("OK");
                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(SummaryReport.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
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

                pd.cancel();
                pd.dismiss();

                  rate = response.body().string();

                if (response.isSuccessful()){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {






                            //String s =  rate.replaceAll("^\"|\"$", "");

                            s2 =             rate.substring(1,rate.length()-1);
//                            rate.replace("\"", "");
                           // Log.e("rate",s);
                            Log.e("rates",s2);


                            weightperkg.setText("Weight per Kg : " +getResources().getString(R.string.rupee)+s2);
                            weight.clearFocus();
                            weight.clearComposingText();

                           frate = Float.parseFloat(s2);//.parseInt(rate);
                           fweight = Float.parseFloat(weight.getText().toString());
                            Log.e("rates", String.valueOf(frate));

                           weighttxt.setText("Total weight : " +weight.getText().toString() + " Kg(s)");



                           amountforweight = frate * fweight;

                           washingcost.setText("Washing Charges : " + getResources().getString(R.string.rupee) +String.valueOf(amountforweight) );

                           s=0;
                            s =  ((0/100) *sum)+sum;

                            if (jobOrder.getExpressDelivery().equalsIgnoreCase("1")){




                                s=amountforweight+ s+Double.valueOf(jobOrder.getExpressDeliveryCharge());

                                  Log.e("total", String.valueOf(s));
                                grdtotal.setText("Total " +getResources().getString(R.string.rupee)+String.valueOf(s));
                            }
                            else {

                                s=amountforweight+s;
                                grdtotal.setText("Total " +getResources().getString(R.string.rupee)+String.valueOf(s));
                                Log.e("total", String.valueOf(s));
                            }

                            layouttxt.setVisibility(View.VISIBLE);



                            updatejoborder();



                        }
                    });
                }


            }
        });
    }
    private void updatejoborder () {
        pd = new ProgressDialog(SummaryReport.this);
        pd.setMessage("updating your Order");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        JSONArray itemType = new JSONArray();
        JSONArray unitPrice = new JSONArray();
        JSONArray subTotal = new JSONArray();
        JSONArray quantity = new JSONArray();
        for (int i=0;i<filterdata2.size();i++){
            itemType.put(filterdata2.get(i).item);
        }
        for (int i=0;i<filterdata2.size();i++){
            unitPrice.put(filterdata2.get(i).cost);
        }
        for (int i=0;i<filterdata2.size();i++){
            subTotal.put(filterdata2.get(i).amt);
        }
        float garmentscount = 0;
        for (int i=0;i<filterdata2.size();i++){
            float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
            garmentscount+= foo;
            quantity.put(filterdata2.get(i).noofpieces);
        }
//        if (expressDelivery.equalsIgnoreCase("1")){
//            s=s+expressDeliveryCharge;
//        }
        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        try {
            //  postdat.put("status", "FillOrder-CONFIRMED");
            postdat.put("customerId",tinyDB.getString("custid"));
            postdat.put("expressDelivery",expressDelivery);
            // postdat.put("expressDeliveryCharge",expressDeliveryCharge);
            postdat.put("jobId",tinyDB.getString("jobid"));
            postdat.put("jobOrderDateTime",timeStamp2);
            postdat.put("gstPercentage", "0");
            postdat.put("grandTotal",String.valueOf(s));
            postdat.put("garmentsCount",garmentscount);
            postdat.put("itemType",itemType);
            postdat.put("unitPrice",unitPrice);
            postdat.put("quantity",quantity);
            postdat.put("subTotal",subTotal);
            if (jobOrder.getServiceName()!=null && !jobOrder.getServiceName().isEmpty()){
                postdat.put("serviceName",jobOrder.getServiceName());
            }
            else {
                postdat.put("serviceName","ironing");
            }
            postdat.put("deliverOnHanger",jobOrder.getDeliverOnHanger());
            postdat.put("washQuantity",fweight);
            postdat.put("washServiceCharge",amountforweight);
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
                pd.cancel();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(SummaryReport.this);
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
                                    final Dialog openDialog = new Dialog(SummaryReport.this);
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
                                            weight.clearComposingText();
                                            weight.clearFocus();
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });
                                    openDialog.setCancelable(false);
                                    openDialog.show();
                                }
                                else if (jsonResponse.getString("statusCode").equalsIgnoreCase("1")){



                                    final Dialog openDialog = new Dialog(SummaryReport.this);
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
                                            weight.clearComposingText();
                                            weight.clearFocus();

                                            //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(SummaryReport.this,SummaryReport.class);
//                                            startActivity(intent);
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
    private void getjoborder() {

        pd = new ProgressDialog(SummaryReport.this);
        pd.setMessage("Getting Job Orders..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        Log.e("postdata",postdat.toString());
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
                        final Dialog openDialog = new Dialog(SummaryReport.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Something Went Wrong");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = openDialog.findViewById(R.id.cancel);
                        dialogno.setText("OK");
                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(SummaryReport.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
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
                //  home.setEnabled(true);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Log.e("Resy",mMessage2);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            //   TraverseData();

                            try {
                                final JSONObject jsonObject = new JSONObject(mMessage2);

                                //   Double statuscode = jsonObject.optDouble("statusCode");
                                Double jobid = jsonObject.optDouble("jobid");

                                //tinyDB.putString("custId",jsonObject.getString());
                                // tinyDB.putString("custId",jobOrder.getCustomerId());
                                //tinyDB.putString("jobid",jobOrder.getJobid());

                                expressDelivery = jsonObject.getString("expressDelivery");
                                expressDeliveryCharge = jsonObject.getDouble("expressDeliveryCharge");
                                serviceName = jsonObject.getString("serviceName");
                                deliveronhangerkey = jsonObject.getString("deliverOnHanger");

                                Log.e("deliveronhangetkey",serviceName);

                                if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){


                                    final Dialog openDialog = new Dialog(SummaryReport.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Fill Order");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Please ask your customer to fill order");
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    //  dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
                                    dialogno.setText("OK");
                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });

                                    dialogCloseButton.setText("Fill Order");
                                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(SummaryReport.this,FillOrder.class);
                                            intent.putExtra("expressDelivery",expressDelivery);
                                            intent.putExtra("expressDeliveryCharge",expressDeliveryCharge);
                                            intent.putExtra("serviceName",serviceName);
                                            intent.putExtra("deliverOnHanger",deliveronhangerkey);
                                            Log.e("expressDelivery",expressDelivery);
                                            startActivity(intent);
                                        }
                                    });

                                    openDialog.show();
                                    openDialog.setCancelable(false);

                                }
//                                else if (serviceName.equalsIgnoreCase("washAndPress")) {
//                                    Intent intent = new Intent(SummaryReport.this,FillOrder.class);
//                                    intent.putExtra("expressDelivery",expressDelivery);
//                                    intent.putExtra("expressDeliveryCharge",expressDeliveryCharge);
//                                    intent.putExtra("serviceName",serviceName);
//                                    intent.putExtra("deliverOnHanger",deliveronhangerkey);
//                                    Log.e("expressDelivery",expressDelivery);
//                                    startActivity(intent);
//                                    }





                                    else {
                                    Gson gson = new Gson();

                                    jobOrder = gson.fromJson(mMessage2,JobOrder.class);


                                    jobidtxt.setText(jobOrder.getJobid());
                                    custid.setText(jobOrder.getCustomerId());
                                    date.setText(jobOrder.getDate());


                                    if (jobOrder.getServiceName().equalsIgnoreCase("washAndPress")){

                                        layoutweight.setVisibility(View.VISIBLE);




                                    }

                                    else {

                                        layouttxt.setVisibility(View.GONE);

                                        layoutweight.setVisibility(View.GONE);
                                    }


                                    if (jobOrder.getDeliverOnHanger().equalsIgnoreCase("0")){
                                        hangerstatus.setVisibility(View.GONE);
                                        deliverontxt.setVisibility(View.GONE);

                                    }

                                    else {

                                        hangerstatus.setText("YES");
                                    }



                                    if (jobOrder.getExpressDelivery().equalsIgnoreCase("1")){

                                        expresmsg.setText("Express Delivery Charges of " +getResources().getString(R.string.rupee)+" "+jobOrder.getExpressDeliveryCharge() + " applied.");

                                    }
                                    else {

                                        expresmsg.setVisibility(View.GONE);
                                    }
//                                String s = jobOrder.getCustomerId();

//                                filterdata2.add(jobOrder);

                                    for (int i= 0; i<jobOrder.getCategory().size(); i++){


                                      //  DataFish2 ss = new DataFish2("item","qty","price","total");

                                        Float ss2 = Float.parseFloat(jobOrder.getPrice().get(i));

                                        Float ss3 =  Float.parseFloat(jobOrder.getQuantity().get(i));
                                        Float ss4 = ss2 * ss3;
                                        DataFish2 sds = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),jobOrder.getPrice().get(i),String.valueOf(ss4));
                                        filterdata2.add(sds);
                                    }
                                    for (int i=0;i<filterdata2.size();i++){
                                        float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
                                        float foo3 = Float.parseFloat(filterdata2.get(i).amt);
                                        garmentscount+= foo;
                                        sum+=foo3;
                                        //   quantity.put(filterdata2.get(i).noofpieces);
                                    }

                                    btmtotal.setText(String.valueOf(Math.round(garmentscount)));

                                    grandtotaltxt = jobOrder.getGrandTotal();
                                    s =  ((0/100) *sum)+sum;
                                    if (jobOrder.getExpressDelivery().equalsIgnoreCase("1")){

                                     //  s= s+Double.valueOf(jobOrder.getExpressDeliveryCharge());

                                        grdtotal.setText("Total " +getResources().getString(R.string.rupee)+String.valueOf(s+Double.valueOf(jobOrder.getExpressDeliveryCharge())));
                                    }
                                    else {

                                        grdtotal.setText("Total " +getResources().getString(R.string.rupee)+String.valueOf(s));
                                    }

                                    Adapter = new AdapterFish(SummaryReport.this, filterdata2);
                                    Adapter.setHasStableIds(false);
                                    mRVFishPrice.setAdapter(Adapter);
                                    mRVFishPrice.setHasFixedSize(false);
                                    //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                                    //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(SummaryReport.this, LinearLayoutManager.VERTICAL,true));

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
    public class DataFish2 {
        public String item;
        public String noofpieces;
        public String cost;
        public String amt;



        public DataFish2(String item,String noofpieces,String cost,String amt){

            this.item = item;
            this.noofpieces = noofpieces;
            this.cost = cost;
            this.amt = amt;
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
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);


            myHolder.item.setText(current.item);
            myHolder.noofpices.setText(current.noofpieces);
            myHolder.cost.setText(current.cost);
            myHolder.amount.setText(current.amt);
            myHolder.plus.setVisibility(View.GONE);
//            myHolder.minus.setVisibility(View.GONE);
            myHolder.delete.setVisibility(View.GONE);




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
//                minus = (ImageButton)itemView.findViewById(R.id.minus);
                delete = itemView.findViewById(R.id.del);

                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }
    private void Invoice() {


        pd = new ProgressDialog(SummaryReport.this);
        pd.setMessage("Updating Status..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        JSONArray itemType = new JSONArray(jobOrder.getCategory());
        JSONArray unitPrice = new JSONArray(jobOrder.getPrice());
        JSONArray subTotal = new JSONArray(jobOrder.getSubTotal());
        JSONArray quantity = new JSONArray(jobOrder.getQuantity());

//        if (jobOrder.getExpressDelivery().equalsIgnoreCase("1")){
//
//            s=s+Double.valueOf(jobOrder.getExpressDeliveryCharge());
//        }

        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {
            postdat.put("count",String.valueOf(Math.round(garmentscount)));
            postdat.put("status", "PICKUP-CONFIRMED");
            postdat.put("customerId", jobOrder.getCustomerId());
            postdat.put("grandTotal",jobOrder.getGrandTotal());
            postdat.put("gstPercentage", jobOrder.getGSTPercentage());
            postdat.put("invoiceDateTime", timeStamp2);
            //  postdat.put("itemType",);
            postdat.put("expressDelivery",jobOrder.getExpressDelivery());
            postdat.put("expressDeliveryCharge",jobOrder.getExpressDeliveryCharge());
            postdat.put("quantity",quantity);
            postdat.put("subTotal",subTotal);
            postdat.put("unitPrice",unitPrice);
            postdat.put("itemType",itemType);
            postdat.put("jobid",jobOrder.getJobid());
            postdat.put("deliverOnHanger",jobOrder.getDeliverOnHanger());
            if (jobOrder.getServiceName()!=null && !jobOrder.getServiceName().isEmpty()){
                postdat.put("serviceName",jobOrder.getServiceName());
            }
            else {
                postdat.put("serviceName","ironing");
            }
//            postdat.put("deliverOnHanger",jobOrder.getDeliverOnHanger());
           // postdat.put("deliverOnHanger",jobOrder.getDeliverOnHanger());
            postdat.put("washQuantity",fweight);
            postdat.put("washServiceCharge",amountforweight);

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        Log.e("putdata",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"deliveryInvoice")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();

                //               home.setEnabled(true);
                String mMessage = e.getMessage().toString();
                Log.e("resyul reer",mMessage);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(SummaryReport.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Something Went Wrong");
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
//                                                Intent intent = new Intent(PickupDetails.this,SummaryReport.class);
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


                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
//                home.setEnabled(true);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy",mMessage);

                            try {

                                JSONArray array = new JSONArray(mMessage);

                                for(int k = 0; k < array.length(); k++) {
                                    JSONObject jsonObject = new JSONObject(array.get(k).toString());

                                    if (jsonObject.getString("statusCode").equalsIgnoreCase("0")){
                                        final Dialog openDialog = new Dialog(SummaryReport.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("status");
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

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();
                                        openDialog.setCancelable(false);

                                    }

                                    else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                        final Dialog openDialog = new Dialog(SummaryReport.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("status");
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

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                                //                                           TinyDB tinyDB = new TinyDB(PickupDetails.this);
//                                            tinyDB.putString("customerId",mm.getCustomerId());
//                                            tinyDB.putString("jobId",mm.getJobid());
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();
                                        openDialog.setCancelable(false);
                                    }


                                }

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


                    }
                });
            }
        });
    }
    private void Submitstatus() {


        pd = new ProgressDialog(SummaryReport.this);
        pd.setMessage("Updating Status..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", jobOrder.getCustomerId());
            postdat.put("jobId", jobOrder.getJobid());
            postdat.put("status",radiostatus);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"updateJobStatus")
                .post(body)
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
                        final Dialog openDialog = new Dialog(SummaryReport.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Something Went Wrong");
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
//                                                Intent intent = new Intent(PickupDetails.this,SummaryReport.class);
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

                                if (jsonObject.getString("statusCode").equalsIgnoreCase("0")){
                                    final Dialog openDialog = new Dialog(SummaryReport.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
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

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                    openDialog.setCancelable(false);

                                }

                                else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                    final Dialog openDialog = new Dialog(SummaryReport.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
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

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                            //                                           TinyDB tinyDB = new TinyDB(PickupDetails.this);
//                                            tinyDB.putString("customerId",mm.getCustomerId());
//                                            tinyDB.putString("jobId",mm.getJobid());
                                            startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                    openDialog.setCancelable(false);
                                }




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