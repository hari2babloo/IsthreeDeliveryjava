package com.a3x3conect.isthreedelivery;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.a3x3conect.isthreedelivery.Models.modelPickuplist;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PickupDetails extends AppCompatActivity {




    TextView custname,address;

    Button map,call;
    modelPickuplist mm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_details);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        Integer pos = bundle.getInt("positon");

        Log.e(String.valueOf(pos),message);

        custname = (TextView)findViewById(R.id.custname);
        address = (TextView)findViewById(R.id.adressdata);
        map = (Button)findViewById(R.id.directions);
        call = (Button)findViewById(R.id.call);

        try {
            JSONArray jj = new JSONArray(message);
            JSONObject ss =jj.getJSONObject(pos);
            Gson gson = new Gson();
            mm = gson.fromJson(String.valueOf(ss),modelPickuplist.class);
            Log.e("Adres", mm.getAddress());
            custname.setText(mm.getDisplayName());
            address.setText(mm.getAddress() + ","+mm.getLandMark()+ ","+mm.getCity()+","+mm.getState());

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

    }
}
