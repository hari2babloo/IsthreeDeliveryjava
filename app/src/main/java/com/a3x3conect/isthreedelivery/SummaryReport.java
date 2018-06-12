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
import android.widget.ImageButton;
import android.widget.ImageView;
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
    TextView btmtotal;
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();
    private AdapterFish Adapter;
    Button home,cancelorder;
    double s;
    TinyDB tinyDB;
    String mMessage2,mMessage,radiostatus;

    JobOrder jobOrder;
    float garmentscount = 0;
    float sum = 0;

    TextView grdtotal,date,custid,status,jobidtxt;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tinyDB = new TinyDB(SummaryReport.this);
        home =  (Button)findViewById(R.id.home);
        cancelorder = (Button)findViewById(R.id.cancel);
        grdtotal = (TextView)findViewById(R.id.grdtotal);

        jobidtxt =  (TextView)findViewById(R.id.jobid);
        custid =  (TextView)findViewById(R.id.custid);
        date =  (TextView)findViewById(R.id.date);
      //  status = (TextView)findViewById(R.id.delstatus);

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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setEnabled(false);


                Invoice();


            }
        });
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        tableLayout = (TableLayout)findViewById(R.id.tabl);
        btmtotal = (TextView)findViewById(R.id.btmtotal);

     //   filterdata2 =   (ArrayList<DataFish2>)getIntent().getSerializableExtra("FILES_TO_SEND");
        
       getjoborder();
       
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

//                                                //                                          Toast.makeText(SummaryReport.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });
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
              //  home.setEnabled(true);
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

                                if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                    final Dialog openDialog = new Dialog(SummaryReport.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Select Status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Please ask your customer to fill order");
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
                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();


                                }



                            else {


                                    Gson gson = new Gson();

                                     jobOrder = gson.fromJson(mMessage2,JobOrder.class);


                                     jobidtxt.setText(jobOrder.getJobid());
                                     custid.setText(jobOrder.getCustomerId());
                                     date.setText(jobOrder.getDate());


//                                String s = jobOrder.getCustomerId();

//                                filterdata2.add(jobOrder);

                                    for (int i= 0; i<jobOrder.getCategory().size(); i++){


                                        DataFish2 ss = new DataFish2("item","qty","price","total");

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

                                    s =  ((0/100) *sum)+sum;
                                    grdtotal.setText("Total " +getResources().getString(R.string.rupee)+String.valueOf(s));
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
                item = (TextView)itemView.findViewById(R.id.item);
                noofpices = (TextView)itemView.findViewById(R.id.noofpices);
                cost = (TextView)itemView.findViewById(R.id.cost);
                amount = (TextView)itemView.findViewById(R.id.total);
                plus = (Button)itemView.findViewById(R.id.plus);
//                minus = (ImageButton)itemView.findViewById(R.id.minus);
                delete = (ImageButton)itemView.findViewById(R.id.del);

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

        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {
            postdat.put("count",String.valueOf(Math.round(garmentscount)));
            postdat.put("status", "PICKUP-CONFIRMED");
            postdat.put("customerId", jobOrder.getCustomerId());
            postdat.put("grandTotal",s);
            postdat.put("gstPercentage", jobOrder.getGSTPercentage());
            postdat.put("invoiceDateTime", timeStamp2);
            //  postdat.put("itemType",);
            postdat.put("quantity",quantity);
            postdat.put("subTotal",subTotal);
            postdat.put("unitPrice",unitPrice);
            postdat.put("itemType",itemType);
            postdat.put("jobid",jobOrder.getJobid());

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
                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();

                                    }

                                    else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                        final Dialog openDialog = new Dialog(SummaryReport.this);
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
                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                                //                                           TinyDB tinyDB = new TinyDB(PickupDetails.this);
//                                            tinyDB.putString("customerId",mm.getCustomerId());
//                                            tinyDB.putString("jobId",mm.getJobid());
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();
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
                                    final Dialog openDialog = new Dialog(SummaryReport.this);
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
                                            Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });



                                    openDialog.show();

                                }

                                else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                    final Dialog openDialog = new Dialog(SummaryReport.this);
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
                                            Intent intent = new Intent(SummaryReport.this,Dashpage.class);
                                            //                                           TinyDB tinyDB = new TinyDB(PickupDetails.this);
//                                            tinyDB.putString("customerId",mm.getCustomerId());
//                                            tinyDB.putString("jobId",mm.getJobid());
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
