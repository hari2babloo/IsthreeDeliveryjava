package com.a3x3conect.isthreedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.a3x3conect.isthreedelivery.Models.TinyDB;

public class ExpressOrders extends AppCompatActivity {
    TinyDB tinydb;
    ImageButton expresspickup,expressdeliverirs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.express_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        expressdeliverirs = (ImageButton)findViewById(R.id.expressdeliveries);
        expresspickup = (ImageButton)findViewById(R.id.exprespickups);

        tinydb = new TinyDB(this);
        expresspickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExpressOrders.this, Pickuplist.class);

                tinydb.putString("keypickup","getExpressPickupRequests");

              //  intent.putExtra("keypickup","getExpressPickupRequests");
                startActivity(intent);

            }
        });

        expressdeliverirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpressOrders.this, Deliverylist.class);
                tinydb.putString("keydelivery","getExpressDeliveryOrders");
                //intent.putExtra("keydelivery","getExpressDeliveryOrders");
                startActivity(intent);

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
