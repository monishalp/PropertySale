package com.dada.realestatemanager.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dada.realestatemanager.AppController;
import com.dada.realestatemanager.R;
import com.dada.realestatemanager.util.AppConfig;
import com.dada.realestatemanager.util.PropertyItem;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BuyerMainActivity extends Activity {

    MaterialEditText zipInput;
    ShimmerTextView st,sub_title;
    String[] SPINNERLIST = {"House", "Apartment", "Community"};

    private ProgressDialog pDialog;
    private MaterialBetterSpinner buyerMaterialDesignSpinner;
    public static List<PropertyItem> propertyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);

        zipInput = (MaterialEditText) findViewById(R.id.zip_code_input);
        //zipInput.setError("Wrong form");

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        sub_title = (ShimmerTextView)findViewById(R.id.title);
        Shimmer title= new Shimmer();
        title.setRepeatCount(5).setStartDelay(500).setDuration(5000);
        title.start(sub_title);

        st = (ShimmerTextView)findViewById(R.id.shimmer_tv);
        Shimmer sh = new Shimmer();
        sh.setRepeatCount(5).setStartDelay(500).setDuration(5000);
        sh.start(st);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.android_material_design_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);
        //materialDesignSpinner.setOnItemSelectedListener(new SpinnerClass());

        AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                makeRequest(AppConfig.URL_CAT, buyerMaterialDesignSpinner);
            }
        };
        materialDesignSpinner.setOnItemClickListener(listener);
        ArrayAdapter<String> adapterTemp=new ArrayAdapter<String>(BuyerMainActivity.this,
                android.R.layout.simple_dropdown_item_1line, new String[]{});
        buyerMaterialDesignSpinner=(MaterialBetterSpinner)findViewById(R.id.type_spinner);
        buyerMaterialDesignSpinner.setAdapter(adapterTemp);
        buyerMaterialDesignSpinner.setDropDownHeight(0);
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void makeRequest(String url, final MaterialBetterSpinner spinner) {

        showpDialog();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    List<String> spinnerArray=new ArrayList<>();
                    for(int i=0;i<response.length();i++){
                        JSONObject temp=response.getJSONObject(i);
                        spinnerArray.add(temp.getString("Category Name"));

                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(BuyerMainActivity.this,
                            android.R.layout.simple_dropdown_item_1line, spinnerArray);
                    spinner.setAdapter(adapter);
                    spinner.setDropDownHeight(-2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void searchButton(View v)
    {
        //zipInput.validateWith(new RegexpValidator("wrong form","\\d{5}"));
        //startActivity(new Intent(this, MapsActivity.class));
        // Toast.makeText(this, zipInput.getError(),Toast.LENGTH_LONG).show();
        String cat=buyerMaterialDesignSpinner.getText().toString();
        String zip=zipInput.getText().toString();
        String catId="";
        if(cat.equals("Rent")){
            catId="1";
        }
        else if(cat.equals("Outright Purchase")){
            catId="2";
        }
        String searchRequest=AppConfig.URL_SEARCH+zip+AppConfig.URL_SEARCH_EXTRA+catId;
        makeRequest(searchRequest);
    }

    private void makeRequest(String url) {

        showpDialog();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    //Toast.makeText(MainActivity.this,""+response.length(),Toast.LENGTH_LONG).show();
                    if(response.length()==0){
                        Toast.makeText(BuyerMainActivity.this, "No result matching", Toast.LENGTH_LONG).show();
                    }
                    else{
                        propertyList=new ArrayList<>();
                        for(int i=0;i<response.length();i++){
                            JSONObject temp=response.getJSONObject(i);
                            propertyList.add(new PropertyItem(
                                    temp.getString("Property Id"),
                                    temp.getString("Property Name"),
                                    temp.getString("Property Type"),
                                    temp.getString("Property Category"),
                                    temp.getString("Property Address1"),
                                    temp.getString("Property Address2"),
                                    temp.getString("Property Zip"),
                                    temp.getString("Property Image 1"),
                                    temp.getString("Property Image 2"),
                                    temp.getString("Property Image 3"),
                                    temp.getString("Property Latitude"),
                                    temp.getString("Property Longitude"),
                                    temp.getString("Property Cost"),
                                    temp.getString("Property Size"),
                                    temp.getString("Property Desc")));
                        }
                        startActivity(new Intent(BuyerMainActivity.this, MapsActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
