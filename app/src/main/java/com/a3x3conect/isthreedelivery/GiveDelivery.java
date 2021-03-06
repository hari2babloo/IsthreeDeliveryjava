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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a3x3conect.isthreedelivery.Models.JobOrder;
import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.a3x3conect.isthreedelivery.Models.getJobSummary;
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

public class GiveDelivery extends AppCompatActivity {


    ProgressDialog pd;
    RecyclerView mRVFishPrice;
    TableLayout tableLayout;
    TextView btmtotal,grdtotal,baltopay,amtpaid,date,custid,status,jobidtxt,expresscharges;
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();
    private AdapterFish Adapter;
    Button home,cancel;
    String radiostatus,paymentmode;
    double s,amountpayable,amountcollected,expresschargesamt;
    TinyDB tinyDB;
    String mMessage2,mMessage;
    JobOrder jobOrder;
    float garmentscount = 0;

    String addmoneyamount;
    float sum = 0;


    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_delivery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tinyDB = new TinyDB(GiveDelivery.this);
        home = findViewById(R.id.home);
        grdtotal = findViewById(R.id.grdtotal);
        baltopay = findViewById(R.id.baltopay);
        amtpaid = findViewById(R.id.amountpaid);
        jobidtxt = findViewById(R.id.jobid);
        custid = findViewById(R.id.custid);
        date = findViewById(R.id.date);
        expresscharges = findViewById(R.id.expresscharges);

        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] items = {"Customer not at home","Phone not reachable","Payment not initiated","Issue not listed"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(GiveDelivery.this);//ERROR ShowDialog cannot be resolved to a type
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


                            Toast.makeText(GiveDelivery.this, "Select a Status", Toast.LENGTH_SHORT).show();


                        }


                    }
                });

                builder.show();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Invoice();

      //          radiostatus = "JOB-FINISHED";

                if(home.getText().toString().equalsIgnoreCase("FINISH"))
                {
                    finishjobstatus();
                }

                else {


                    final Dialog openDialog = new Dialog(GiveDelivery.this);
                    openDialog.setContentView(R.layout.addmoney);
                    openDialog.setTitle("Enter Money");
                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                   // dialogTextContent.setText("Something Went Wrong");
                    final EditText editText = openDialog.findViewById(R.id.editText);
                    editText.setText(String.valueOf(amountpayable));
                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                    dialogCloseButton.setText("PROCEED");
  //                  dialogCloseButton.setVisibility(View.GONE);
                    Button dialogno = openDialog.findViewById(R.id.cancel);
                    //dialogno.setText("OK");
                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (editText.getText().toString().isEmpty()){

                                Toast.makeText(GiveDelivery.this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                            }


                            else {


                                amountcollected = Double.parseDouble(editText.getText().toString());
                                openDialog.dismiss();
                                if (amountcollected < amountpayable) {

                                    final Dialog openDialog = new Dialog(GiveDelivery.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Entered Amount is less than Balance to Pay.");
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    dialogImage.setBackgroundResource(R.drawable.failure);
                                    dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.failure));
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);

                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
                                    dialogno.setText("OK");
                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
//                                            startActivity(intent);
                                        }
                                    });


                                    openDialog.show();
                                    openDialog.setCancelable(false);


                                } else {

                                    openDialog.dismiss();

                                    ConfirmDialog();
                                }


                            }

                        // finishjobstatus();

//                                                //                                          Toast.makeText(GiveDelivery.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
//                                                startActivity(intent);
                        }
                    });

                    dialogno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog.dismiss();
                        }
                    });


                    openDialog.show();
                    openDialog.setCancelable(false);


                }





            }
        });
        mRVFishPrice = findViewById(R.id.fishPriceList);
        tableLayout = findViewById(R.id.tabl);
        btmtotal = findViewById(R.id.btmtotal);

        //   filterdata2 =   (ArrayList<DataFish2>)getIntent().getSerializableExtra("FILES_TO_SEND");

        getjoborder();

    }

    private void ConfirmDialog() {


        final Dialog openDialog = new Dialog(GiveDelivery.this);
        openDialog.setContentView(R.layout.addmoney);
        openDialog.setTitle("Confirm Amount Received");
        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);


        dialogTextContent.setText("Received Amount of  "+getResources().getString(R.string.rupee)+String.valueOf(amountcollected)+ "  from the Customer?");
        // dialogTextContent.setText("Something Went Wrong");
        final EditText editText = openDialog.findViewById(R.id.editText);
        editText.setText(String.valueOf(amountpayable));
        editText.setVisibility(View.GONE);
        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);

        TextView msg = openDialog.findViewById(R.id.msg);
        msg.setVisibility(View.GONE);
        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
        dialogCloseButton.setText("CONFIRM");
        //                  dialogCloseButton.setVisibility(View.GONE);
        Button dialogno = openDialog.findViewById(R.id.cancel);
        //dialogno.setText("OK");
        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // amountcollected = Double.parseDouble(editText.getText().toString());


                finishjobstatus();
                openDialog.dismiss();

                //ConfirmDialog();
//                                                //                                          Toast.makeText(GiveDelivery.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
//                                                startActivity(intent);
            }
        });

        dialogno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });


        openDialog.show();
        openDialog.setCancelable(false);
    }

    private void getjoborder() {

        pd = new ProgressDialog(GiveDelivery.this);
        pd.setMessage("Getting Job Orders..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
          //  postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("postdata",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getJobSummary")
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
                        final Dialog openDialog = new Dialog(GiveDelivery.this);
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

//                                                //                                          Toast.makeText(GiveDelivery.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });
                        openDialog.show();
                        openDialog.setCancelable(false);
                    }
                });

                mMessage2 = e.getMessage().toString();
                Log.e("error",mMessage2);
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


                            Log.e("Resy22",mMessage2);


                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<getJobSummary>>(){}.getType();
                            ArrayList<getJobSummary> modelsignin = gson.fromJson(mMessage2,listType);


//                            jobidtxt.setText(jobOrder.getJobid());
//                            custid.setText(jobOrder.getCustomerId());
//                            date.setText(jobOrder.getDate());



                            for(int j = 0; j < modelsignin.size(); j++) {

                                String s = modelsignin.get(j).getBalanceAmountToPay();



                                baltopay.setText(getResources().getString(R.string.rupee)+modelsignin.get(j).getPayableAmount());
                                amtpaid.setText(getResources().getString(R.string.rupee)+modelsignin.get(j).getAmountPaid());

                                jobidtxt.setText(modelsignin.get(j).getJobid());
                                custid.setText(modelsignin.get(j).getCustId());

                                paymentmode = modelsignin.get(j).getPaymentMode();


 //                               Log.e("Paymentmode",paymentmode);

//                                if (paymentmode == null) {
//
//                                   // Toast.makeText(GiveDelivery.this, "Empty", Toast.LENGTH_SHORT).show();
//
//                                    home.setVisibility(View.GONE);
//
//
//                                    final Dialog openDialog = new Dialog(GiveDelivery.this);
//                                    openDialog.setContentView(R.layout.alert);
//                                    openDialog.setTitle("Payment");
//                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
//                                    dialogTextContent.setText("Please request your customer to initiate payment");
//                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
//                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
//                                    dialogCloseButton.setVisibility(View.GONE);
//                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
//                                    dialogno.setText("OK");
//                                    dialogno.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            openDialog.dismiss();
//
////                                                //                                          Toast.makeText(GiveDelivery.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(GiveDelivery.this,Deliverylist.class);
//                                                startActivity(intent);
//                                        }
//                                    });
//                                    openDialog.show();
//                                    // doSomething
//                                }

                                amountpayable = Double.parseDouble(modelsignin.get(j).getPayableAmount());
                                amountcollected = Double.parseDouble(modelsignin.get(j).getPayableAmount());
                                expresschargesamt = Double.parseDouble(modelsignin.get(j).getExpressDeliveryCharge());
                                Log.e("psdsaaf", String.valueOf(amountpayable));
                                if (amountpayable>0.0){

                                   // finishjobstatus();

                                    home.setText("ENTER AMOUNT RECEIVED");
                                }
                                else {

                                    home.setText("FINISH");

                                }
                                Log.e(String.valueOf(amountpayable),String.valueOf(amountpayable));
                                List<String> jobinfo  = new ArrayList<>();
                                jobinfo = modelsignin.get(j).getCategory();

                                for (int k= 0;k<jobinfo.size();k++){


                                   Log.e("cat", modelsignin.get(j).getCategory().get(k));


                                    DataFish2 sds = new DataFish2(modelsignin.get(j).getCategory().get(k) , modelsignin.get(j).getQty().get(k), modelsignin.get(j).getPrice().get(k), modelsignin.get(j).getSubTotal().get(k));


                                        filterdata2.add(sds);
                                }

                            }

                            for (int i=0;i<filterdata2.size();i++){


                                float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
                                float foo3 = Float.parseFloat(filterdata2.get(i).amt);


                                garmentscount+= foo;
                                sum+=foo3;

                                //   quantity.put(filterdata2.get(i).noofpieces);
                            }
                            s =  ((0/100) *sum)+sum;
                            btmtotal.setText(getResources().getString(R.string.rupee)+s);
                           // btmtotal.setText();
                            grdtotal.setText(String.valueOf(Math.round(garmentscount)));
                            expresscharges.setText(getResources().getString(R.string.rupee)+expresschargesamt);

                            Adapter = new AdapterFish(GiveDelivery.this, filterdata2);
                            Adapter.setHasStableIds(false);
                            mRVFishPrice.setAdapter(Adapter);
                            mRVFishPrice.setHasFixedSize(false);
                            //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                            //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                            mRVFishPrice.setLayoutManager(new LinearLayoutManager(GiveDelivery.this, LinearLayoutManager.VERTICAL,true));


//                                    }


//                                    jobOrder = gson.fromJson(mMessage2,JobOrder.class);


//                                String s = jobOrder.getCustomerId();

//                                filterdata2.add(jobOrder);


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


        pd = new ProgressDialog(GiveDelivery.this);
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
                String mMessage = e.getMessage().toString();
                Log.e("resyul reer",mMessage);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(GiveDelivery.this);
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
//                                                Intent intent = new Intent(PickupDetails.this,GiveDelivery.class);
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

                                JSONArray array = new JSONArray(mMessage);

                                for(int k = 0; k < array.length(); k++) {
                                    JSONObject jsonObject = new JSONObject(array.get(k).toString());

                                    if (jsonObject.getString("statusCode").equalsIgnoreCase("0")){
                                        final Dialog openDialog = new Dialog(GiveDelivery.this);
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
                                                Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.setCancelable(false);
                                        openDialog.show();

                                    }

                                    else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                        final Dialog openDialog = new Dialog(GiveDelivery.this);
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
                                                Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
                                                //                                           TinyDB tinyDB = new TinyDB(PickupDetails.this);
//                                            tinyDB.putString("customerId",mm.getCustomerId());
//                                            tinyDB.putString("jobId",mm.getJobid());
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
    private void finishjobstatus() {


        pd = new ProgressDialog(GiveDelivery.this);
        pd.setMessage("Finishing Job..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {

           // postdat.put("count",String.valueOf(Math.round(garmentscount)));
           // postdat.put("status", "PICKUP-CONFIRMED");

            postdat.put("partnerId",tinyDB.getString("partnerid"));
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
            postdat.put("amountPayable",amountpayable);
            postdat.put("amountCollected", amountcollected);


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        Log.e("finishjoborder",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"finishJobOrder")
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
                        final Dialog openDialog = new Dialog(GiveDelivery.this);
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
//                                                Intent intent = new Intent(PickupDetails.this,GiveDelivery.class);
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
                mMessage2 = response.body().string();
                Log.e("Resyehgr",mMessage2);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resyehgr",mMessage2);

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage2);
                                if (jsonObject.getString("statusCode").equalsIgnoreCase("0")){
                                    final Dialog openDialog = new Dialog(GiveDelivery.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Entered Amount is less than Balance to Pay.");
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    dialogImage.setBackgroundResource(R.drawable.failure);
                                    dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.failure));
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);

                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
                                    dialogno.setText("OK");
                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
//                                            startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                    openDialog.setCancelable(false);

                                }

                                else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                    final Dialog openDialog = new Dialog(GiveDelivery.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Please verify order and handover clothes to customer");
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    dialogImage.setBackgroundResource(R.drawable.success);
                                    dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.success));
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);

                                    //dialogno.setBackground(R.drawable.greenroundcorner);
                                    dialogno.setText("OK");
                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
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


        pd = new ProgressDialog(GiveDelivery.this);
        pd.setMessage("Updating Status..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
            postdat.put("status",radiostatus);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("body",body.toString());
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
                        final Dialog openDialog = new Dialog(GiveDelivery.this);
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
//                                                Intent intent = new Intent(Deliverydetails.this,SummaryReport.class);
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
                                    final Dialog openDialog = new Dialog(GiveDelivery.this);
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
                                            Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                    openDialog.setCancelable(false);

                                }

                                else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                    final Dialog openDialog = new Dialog(GiveDelivery.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("status");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Your transaction has been succesfully updated");
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
                                            Intent intent = new Intent(GiveDelivery.this,Dashpage.class);
                                            //                                           TinyDB tinyDB = new TinyDB(Deliverydetails.this);
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
