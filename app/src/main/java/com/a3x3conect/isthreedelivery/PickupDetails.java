package com.a3x3conect.isthreedelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.a3x3conect.isthreedelivery.Models.modelPickuplist;
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

public class PickupDetails extends AppCompatActivity {

    TextView custname,address;
    Button map,call,submit,joborder;
    modelPickuplist mm;
    Spinner spinner;
    String status,mMessage;
    ProgressDialog pd;
    ArrayList<String> spinerdata = new ArrayList<>();
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_details);
        tinyDB = new TinyDB(PickupDetails.this);


        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        final Integer pos = bundle.getInt("position");

        Log.e(String.valueOf(pos),message);

        custname = (TextView)findViewById(R.id.custname);
        address = (TextView)findViewById(R.id.adressdata);
        map = (Button)findViewById(R.id.directions);
        call = (Button)findViewById(R.id.call);
        spinner = (Spinner)findViewById(R.id.spinner);
        submit = (Button) findViewById(R.id.submit);
        joborder = (Button)findViewById(R.id.joborder);




        try {
            JSONArray jj = new JSONArray(message);
            JSONObject ss =jj.getJSONObject(pos);
            Gson gson = new Gson();
            mm = gson.fromJson(String.valueOf(ss),modelPickuplist.class);
            Log.e("Adres", mm.getAddress());
            custname.setText(mm.getDisplayName());
            address.setText(mm.getAddress() + ","+mm.getLandMark()+ ","+mm.getCity()+","+mm.getState());
            tinyDB.putString("custid",mm.getCustomerId());
            tinyDB.putString("jobid",mm.getJobid());
            Log.e("custid",mm.getCustomerId());
            Log.e("job" ,mm.getJobid());
//
            if (mm.getStatus().equalsIgnoreCase("PICKUP-INITIATED")){

                spinner.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
            }

            if (mm.getStatus().equalsIgnoreCase("PICKUP-REQUESTED")){

                joborder.setVisibility(View.GONE);

            }


            Log.e(mm.getCustomerId(),mm.getJobid());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(mm.getLat(),mm.getLongi());
                String strUri = "http://maps.google.com/maps?q=" +mm.getLat() + "," +mm.getLongi() + " (" + mm.getDisplayName() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mm.getPhoneNo()));
                startActivity(intent);
            }
        });

        spinerdata.add("Select Status");
        spinerdata.add("PICKUP-INITIATED");
      //  spinerdata.add("PICKUP-CONFIRMED");
        spinerdata.add("PICKUP-CUSTOMER NOT AVAILABLE");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinerdata);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
 //               Toast.makeText(PickupDetails.this, spinerdata.get(position), Toast.LENGTH_SHORT).show();
          status = spinerdata.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        joborder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PickupDetails.this,SummaryReport.class);
                startActivity(intent);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status.equalsIgnoreCase("Select Status")){
                    final Dialog openDialog = new Dialog(PickupDetails.this);
                    openDialog.setContentView(R.layout.alert);
                    openDialog.setTitle("Select Status");
                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                    dialogTextContent.setText("Select Status Correctly");
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



                    openDialog.show();

                }


                else {
                    Submitstatus();

                }



            }
        });

    }

    private void Submitstatus() {


        pd = new ProgressDialog(PickupDetails.this);
        pd.setMessage("Updating Status..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", mm.getCustomerId());
            postdat.put("jobId", mm.getJobid());
            postdat.put("status",status);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/updateJobStatus")
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
                        final Dialog openDialog = new Dialog(PickupDetails.this);
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
//                                                Intent intent = new Intent(PickupDetails.this,SummaryReport.class);
//                                                startActivity(intent);
                            }
                        });



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

                                if (jsonObject.getString("statusCode").equalsIgnoreCase("0")){
                                    final Dialog openDialog = new Dialog(PickupDetails.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
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
                                                Intent intent = new Intent(PickupDetails.this,Dashpage.class);
                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();

                                }

                                else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                    final Dialog openDialog = new Dialog(PickupDetails.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
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
                                                Intent intent = new Intent(PickupDetails.this,SummaryReport.class);
 //                                           TinyDB tinyDB = new TinyDB(PickupDetails.this);
//                                            tinyDB.putString("customerId",mm.getCustomerId());
//                                            tinyDB.putString("jobId",mm.getJobid());
                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                }

                                else {

                                    final Dialog openDialog = new Dialog(PickupDetails.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
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
                                                Intent intent = new Intent(PickupDetails.this,SummaryReport.class);
                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
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
}
