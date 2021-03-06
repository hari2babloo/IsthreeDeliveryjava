package com.a3x3conect.isthreedelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.a3x3conect.isthreedelivery.Models.modelPickuplist;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Pickuplist extends AppCompatActivity {


    ProgressDialog pd;

    Gson gson;
    String mMessage;
    RecyclerView mRVFishPrice;
    private AdapterFish Adapter;
    String areacount;
    Spinner spinner;
    TinyDB tinyDB;
    String url;
    String spinnertext,pickupzone;
    TextView count;
    ArrayList<prizes> dataModels = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> locationid = new ArrayList<>();
    ArrayList<String> pickupzones = new ArrayList<>();



    List<modelPickuplist> filterdata=new ArrayList<modelPickuplist>();
    List<modelPickuplist> filterdata2=new ArrayList<modelPickuplist>();

    modelPickuplist data = new modelPickuplist();
    final ArrayList<String> dd = new ArrayList<>();
    List<modelPickuplist> tarif;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickuplist);

        tinyDB = new TinyDB(this);
        url = tinyDB.getString("keypickup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRVFishPrice = findViewById(R.id.fishPriceList);
        spinner = findViewById(R.id.spinner);
        count  = findViewById(R.id.count);
        gson = new Gson();

        getdata();



    }

    private void Getlocations() {

        pd = new ProgressDialog(Pickuplist.this);
        pd.setMessage("Getting Form Data");
        pd.setCancelable(false);
        pd.show();

        final   OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"serviceLocations")
                .get()
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
                        final Dialog openDialog = new Dialog(Pickuplist.this);
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

                final String mMessage2 = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.e("result",mMessage2);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                location.add("All Locations");
                                locationid.add("noid");
                                JSONArray jsonArray = new JSONArray(mMessage2);

                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject json_data = jsonArray.getJSONObject(i);

                                    location.add(json_data.getString("location"));
                                    locationid.add(json_data.getString("partnerId"));

                                    Log.e("location",json_data.getString("location"));
                                }

                                setspinner();

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

    private void setspinner() {

        final ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,location);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#163053"));

                spinnertext = locationid.get(position);
                pickupzone = location.get(position);
                Log.e("selected item", location.get(position));
                Log.e("selected item", spinnertext);

                // Adapter.notifyDataSetChanged();

//        Adapter.data.clear();

//                    Adapter.notifyDataSetChanged();


                filterdata.clear();
                filterdata2.clear();

                TraverseData();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getdata() {


        pd = new ProgressDialog(Pickuplist.this);
        pd.setMessage("Getting Details");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            //  postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("agentId", tinyDB.getString("partnerid"));
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("postdata",postdat.toString());

        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+url)
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
                            Getlocations();

                            Type listType = new TypeToken<List<modelPickuplist>>(){}.getType();
                            tarif = gson.fromJson(mMessage,listType);

                            try {
                                JSONArray jj = new JSONArray(mMessage);

                                for (int i=0;i<jj.length();i++){

                                    JSONObject jsonObject =jj.getJSONObject(i);

                                    if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                        final Dialog openDialog = new Dialog(Pickuplist.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("No Pickups");
                                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("No Pickups Available at this moment");
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
                                                Intent intent = new Intent(Pickuplist.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();

                                        openDialog.setCancelable(false);
                                        Log.e("No Pickup","No Pickuip");

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

        pickupzones.clear();

        for(int k = 0; k < tarif.size(); k++) {
            pickupzones.add(tarif.get(k).getPickupZone());
            Log.e("pickupzonesarray", String.valueOf(pickupzones));
        }

//        Set<String> hs = new HashSet<>();
//        hs.addAll(pickupzones);
//        pickupzones.clear();
//        pickupzones.addAll(hs);
//
//        Log.e("pickupzonesarray", String.valueOf(pickupzones));






        for(int k = 0; k < tarif.size(); k++){


            if (pickupzone.equalsIgnoreCase("All Locations")){

                //  Adapter.notifyDataSetChanged();

                filterdata.add(tarif.get(k));
                filterdata2.add(tarif.get(k));
                // count.setText("Pickups Count at "+pickupzone+ "  :"+filterdata.size());

                //               Log.e("eee", String.valueOf(filterdata.get(k)));



            }

            else if ( tarif.get(k).getPickupZone()!=null && tarif.get(k).getPickupZone().equalsIgnoreCase(pickupzone)){


                Adapter.notifyDataSetChanged();
                filterdata.add(tarif.get(k));
                filterdata2.add(tarif.get(k));


                Log.e("filtersize", String.valueOf(filterdata.size()));
                //   count.setText("Pickups Count at "+pickupzone+ "  "+filterdata.size());

//                Log.e("eee", String.valueOf(filterdata.get(k)));




            }

            else {

                //     filterdata2.clear();
                //    filterdata.clear();
                Adapter.notifyDataSetChanged();
                //  count.setText("No Pickups Available");


            }



        }

        Adapter = new AdapterFish(Pickuplist.this, filterdata);




        if (pickupzone.equalsIgnoreCase("All Locations")){

            if (filterdata.size()<10){

                count.setText("Pickups Count at "+pickupzone+ ": 0"+filterdata.size());
            }
            else {

                count.setText("Pickups Count at "+pickupzone+ ": "+filterdata.size());
            }


        }

        else {

            count.setVisibility(View.GONE);
        }

        if (filterdata.isEmpty()){

            count.setVisibility(View.VISIBLE);
            count.setText("No Pickups Available");
        }


        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.scrollToPosition(0);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(Pickuplist.this,LinearLayoutManager.VERTICAL,false));
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
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        // Bind data
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final modelPickuplist current = data.get(position);

            final int po = position;





            if (current.getExpressDelivery().equalsIgnoreCase("1")){

                myHolder.expressimg.setImageResource(R.drawable.expresscar);
            }

            else {


            }

            if(current.getServiceName() != null && !current.getServiceName().isEmpty()) {

                if (current.getServiceName().equalsIgnoreCase("ironing")){

                    myHolder.serviceimg.setText("I");//.setImageResource(R.drawable.iconironing);

                    myHolder.servicename.setText("Ironing");

                    myHolder.strip.setBackground(getResources().getDrawable(R.drawable.cardshapei));
  //                  myHolder.servicename.setTextColor( getResources().getColor(R.color.bluee));
                }
                else if (current.getServiceName().equalsIgnoreCase("washAndPress")){
                    myHolder.servicename.setText("Wash and Press");
                    myHolder.strip.setBackground(getResources().getDrawable(R.drawable.cardshapew));
//                    myHolder.servicename.setTextColor( getResources().getColor(R.color.red));
                    myHolder.serviceimg.setText("W");
                }
                else if (current.getServiceName().equalsIgnoreCase("dryCleaning")){
                    myHolder.strip.setBackground(getResources().getDrawable(R.drawable.cardshaped));
                    myHolder.servicename.setText("Dry Cleaning");
                    myHolder.serviceimg.setText("D"); //R.drawable.icondry);
                   // myHolder.serviceimg.setColorFilter(R.color.colorAccent);
                }


            }
            else {

                myHolder.servicename.setText("Ironing");
                myHolder.serviceimg.setText("I");
                myHolder.strip.setBackground(getResources().getDrawable(R.drawable.cardshapei));
            }

            if(current.getExpressDelivery() != null && !current.getExpressDelivery().isEmpty()){


            if (current.getExpressDelivery().equalsIgnoreCase("1")){

                //myHolder.itemView.setBackgroundColor(Color.GRAY);
                myHolder.one.setTextColor(Color.parseColor("#d20670"));
                myHolder.two.setTextColor(Color.parseColor("#d20670"));
                myHolder.three.setTextColor(Color.parseColor("#d20670"));
            }

            }

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                 //   String sss = (String) filterdata.get(position);

                 //   Log.e("position", filterdata2.get(position)); //filterdata2.get(position).toString());

                    Intent intent = new Intent(Pickuplist.this,PickupDetails.class);
                    intent.putExtra("message",mMessage);
                    intent.putExtra("jobid",filterdata.get(position).getJobid());
                    Log.e("jobid",filterdata.get(position).getJobid());
                  //  Log.e("total", String.valueOf(myHolder.getAdapterPosition()));

                   // myHolder.getAdapterPosition()

                    // filterdata2.get(position);

                 //   ArrayList<String> ss = new ArrayList<String>();

                   // ss.add(String.valueOf(filterdata2.get(position)));
                  ///  intent.putExtra("test",ss);

                    //intent.putStringArrayListExtra("test", (ArrayList<modelPickuplist>) filterdata.get(position));
                 //   Log.e("position", String.valueOf(po));
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
            myHolder.three.setText(current.getPickupScheduledAt());
            myHolder.location.setText(current.getPickupZone());
            myHolder.location.setVisibility(View.GONE);
            // myHolder.section.setText(String.valueOf( position+1)+ "/"+String.valueOf(filterdata2.size()));


            int couintt = Collections.frequency(pickupzones,current.getPickupZone());

            if (couintt<10){

                areacount = String.valueOf( "0"+couintt);


            }

            else {

                areacount = String.valueOf(couintt);

            }


//            for (int k=0;k<filterdata.size();k++){
//
//
//            }
            Log.e("position", String.valueOf(position)+filterdata.get(position).getPickupZone());

            if (position==0){

                myHolder.pickupzone.setText(current.getPickupZone() +": " + areacount);

                myHolder.pickupzone.setVisibility(View.VISIBLE);
                myHolder.line.setVisibility(View.VISIBLE);

            }

            else


            if (filterdata.get(0).getPickupZone().equalsIgnoreCase(current.getPickupZone())){

                myHolder.pickupzone.setVisibility(View.GONE);
                myHolder.line.setVisibility(View.GONE);

            }


            else if (filterdata.get(position-1).getPickupZone().equalsIgnoreCase(current.getPickupZone())){

                myHolder.pickupzone.setVisibility(View.GONE);

                myHolder.line.setVisibility(View.GONE);
            }


            else {

                myHolder.pickupzone.setText(current.getPickupZone() +": " + areacount);

                myHolder.line.setVisibility(View.VISIBLE);
                myHolder.pickupzone.setVisibility(View.VISIBLE);
            }
//           else if (filterdata.get(position-1).getPickupZone().equalsIgnoreCase(filterdata.get(position).getPickupZone())){
//
//
//
//
//           }

            //  if (position>0&&  )

//            if (myHolder.pickupzone.getText().toString().equalsIgnoreCase()){
//
//                myHolder.pickupzone.setVisibility(View.GONE);
//            }
//            if(current.getPickupZone().equalsIgnoreCase(filterdata.get(position-1).getPickupZone())){
//
//                myHolder.pickupzone.setVisibility(View.GONE);
//
//            }

//            myHolder.location.setText(filterdata.size());
            // myHolder.location.setVisibility(View.GONE);

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
            TextView one,two,three,location,section,pickupzone,servicename,serviceimg;
            ImageView expressimg;
            LinearLayout strip;
            View line;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                one = itemView.findViewById(R.id.one);
                two = itemView.findViewById(R.id.two);
                three= itemView.findViewById(R.id.three);
                location = itemView.findViewById(R.id.location);
                section = itemView.findViewById(R.id.section);
                pickupzone = itemView.findViewById(R.id.pickupzone);
                line = itemView.findViewById(R.id.line);
                serviceimg = itemView.findViewById(R.id.servicimg);
                servicename = itemView.findViewById(R.id.servicename);
                expressimg = itemView.findViewById(R.id.expressimg);
                strip = itemView.findViewById(R.id.strip);


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