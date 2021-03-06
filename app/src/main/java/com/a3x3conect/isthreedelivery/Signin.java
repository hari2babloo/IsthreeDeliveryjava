package com.a3x3conect.isthreedelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3x3conect.isthreedelivery.Models.Sigin;
import com.a3x3conect.isthreedelivery.Models.TinyDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Signin extends AppCompatActivity {

    Button signin;
    MaterialEditText userid,pass;
    TextView signuptxt,forgotpass;
    String mMessage;
    List<Sigin> modelsignin;
    TinyDB tinyDB;
    ProgressDialog pd;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        tinyDB = new TinyDB(this);
        userid  = findViewById(R.id.userid);
        pass = findViewById(R.id.pass);
        signin = findViewById(R.id.signin);

        tinyDB = new TinyDB(this);

        Log.e("partnerid",tinyDB.getString("partnerid"));
        String s = tinyDB.getString("partnerid");

        if (s != null && !s.isEmpty()){

            Intent intent = new Intent(Signin.this,Dashpage.class);
            startActivity(intent);
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userid.getText().toString().isEmpty()){
                    userid.setError("Please Enter User ID");
                }
                else if (pass.getText().toString().isEmpty()){
                    pass.setError("Please Enter Your Password");
                }
                else {
                    Validate();
                }
            }
        });
    }
    private void Validate() {
        pd = new ProgressDialog(Signin.this);
        pd.setMessage("Signing in..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("userName", userid.getText().toString());
            postdat.put("password", pass.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"login")
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
                        final Dialog openDialog = new Dialog(Signin.this);
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
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
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
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            TraverseData();
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

    private void TraverseData() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Sigin>>(){}.getType();
        modelsignin = gson.fromJson(mMessage,listType);
        for(int j = 0; j < modelsignin.size(); j++){
            Integer status = modelsignin.get(j).getStatus();
            //  modelsignin.get(j).getStatus();
            if (status.equals(0)){
                final Dialog openDialog = new Dialog(Signin.this);
                openDialog.setContentView(R.layout.alert);
                openDialog.setTitle("");
                TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                dialogTextContent.setText("Please Enter Valid Credentials");
                ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                dialogImage.setBackgroundResource(R.drawable.failure);
                dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.failure));
//              dialogImage.setBackground(this.getDrawable(ContextCompat.R.drawable.failure));
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
                openDialog.show();
                openDialog.setCancelable(false);
            }
            else if (status.equals(1)){

                if(modelsignin.get(j).getUserName().equalsIgnoreCase("C0049")){

                    Intent intent = new Intent(Signin.this,Dashpage.class);
//              intent.putExtra("custid",modelsignin.get(j).getUserName());
                    tinyDB.putString("partnerid",modelsignin.get(j).getUserName());
                    startActivity(intent);
                }

                else


                if (modelsignin.get(j).getUserType().equalsIgnoreCase("Partner")){
                    Intent intent = new Intent(Signin.this,Dashpage.class);
//              intent.putExtra("custid",modelsignin.get(j).getUserName());
                    tinyDB.putString("partnerid",modelsignin.get(j).getUserName());
                    startActivity(intent);
                }
                else
                    {
                    final Dialog openDialog = new Dialog(Signin.this);
                    openDialog.setContentView(R.layout.alert);
                    openDialog.setTitle("");
                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                    dialogTextContent.setText("Please login using customer app");
                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                    dialogImage.setBackgroundResource(R.drawable.failure);
                    dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.failure));
//              dialogImage.setBackground(this.getDrawable(ContextCompat.R.drawable.failure));
                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                    dialogCloseButton.setVisibility(View.GONE);
                    Button dialogno = openDialog.findViewById(R.id.cancel);
                    dialogno.setText("OK");
                    dialogno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog.dismiss();
                        }
                    });
                    openDialog.show();
                    openDialog.setCancelable(false);
                }
                //  Log.e("status", String.valueOf(modelsignin.get(j).getStatus()));
            }
        }
    }

    @Override
    public void onBackPressed() {
        final Dialog openDialog = new Dialog(Signin.this);
        openDialog.setContentView(R.layout.alert);
        openDialog.setTitle("Exit app");
        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
        dialogTextContent.setText("Do you want to Close the app?");
        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
        Button dialogno = openDialog.findViewById(R.id.cancel);
        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                //
//                Signin.this.finish();
//                System.exit(0);
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
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
