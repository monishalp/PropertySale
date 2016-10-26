package com.example.monisha.propertymarketing.AuthenticationForms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.monisha.propertymarketing.R;
import java.util.HashMap;
import java.util.Map;

public class Registration_Form extends AppCompatActivity {


    private Button bRegister;
    private Button bGoBack;
    private TextView regUserName;
    private TextView regEmail;
    private TextView regMobile;
    private TextView regPassword;
    private TextView regDOB;
    private TextView regAddress1;
    private TextView regAddress2;
    private TextView regUserType;


    private ProgressDialog progDialog;
    private String urlLogin;

    public static final String KEY_USERNAME="username";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_EMAIL="email";
    public static final String KEY_MOBILE="mobile";
    public static final String KEY_DOB="dob";
    public static final String KEY_ADDRESS1="address1";
    public static final String KEY_ADDRESS2="address2";
    public static final String KEY_USER_TYPE="usertype";

    String usernameVolley;
    String passwordVolley;
    String emailVolley;
    String mobileVolley;
    String dobVolley;
    String address1Volley;
    String address2Volley;
    String userTypeVolley;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__form);


        regUserName = (TextView) findViewById(R.id.username);
        regEmail = (TextView) findViewById(R.id.email);
        regMobile = (TextView) findViewById(R.id.mobile);
        regPassword = (TextView) findViewById(R.id.password);
        regDOB=(TextView)findViewById(R.id.dob);
        regAddress1 = (TextView) findViewById(R.id.address1);
        regAddress2 = (TextView) findViewById(R.id.address2);
        userType="";
        //regUserType = (TextView) findViewById(R.id.userType);

        Spinner user_type = (Spinner) findViewById(R.id.user_type);

        // Initializing a String Array
        String[] plants = new String[]{
                "type",
                "Buyer",
                "Seller"

        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_user_type_register,R.id.user_type, plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_user_type_register);
        user_type.setAdapter(spinnerArrayAdapter);

        user_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        userType="";
                        break;
                    case 1:
                        userType="buyers";
                        break;
                    case 2:
                        userType="seller";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bRegister = (Button) findViewById(R.id.register);
        bGoBack = (Button) findViewById(R.id.bGoBack);

        //volley request initialization
        progDialog = new ProgressDialog(this);
        progDialog.setMessage("Loading...");
        progDialog.setCancelable(false);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = regUserName.getText().toString();
                final String password = regPassword.getText().toString();
                final String email = regEmail.getText().toString();
                final String mobile = regMobile.getText().toString();

                final String dob = regDOB.getText().toString();
                final String address1 = regAddress1.getText().toString();
                final String address2 = regAddress2.getText().toString();
                final String user_type=userType;
               // userType = regUserType.getText().toString();



                makeJsonStringReq(username, password, email, mobile, dob, address1, address2, user_type);

            }
        });

        bGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_Form.this, LogIn.class);
                startActivity(intent);
            }
        });

    }

    //volley post request
    private void makeJsonStringReq(String pUsername, String pPassword, String pEmail, String pMobile, String pDob, String pAddress1, String pAddress2, String pUserType){

        urlLogin="http://rjtmobile.com/realestate/register.php?signup";
        usernameVolley=pUsername.trim();
        passwordVolley=pPassword.trim();
        emailVolley=pEmail.trim();
        mobileVolley=pMobile.trim();
        dobVolley=pDob;
        address1Volley=pAddress1.trim();
        address2Volley=pAddress2.trim();
        userTypeVolley = pUserType.trim();

        progDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlLogin, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                Log.i("responseregis", response);
                if(emailVolley.length()==0 || passwordVolley.length()==0){
                    Toast.makeText(Registration_Form.this, "Please enter a valid password/email", Toast.LENGTH_LONG).show();
                }
                else if(!(userTypeVolley.equals("buyers") || userTypeVolley.equals("seller"))){
                    Toast.makeText(Registration_Form.this, "select \"buyers\" or \"seller\" to register", Toast.LENGTH_LONG).show();
                }
                else if(response.contains("false") && (userTypeVolley.equals("buyers") || userTypeVolley.equals("seller")) && !(emailVolley.length()==0 || passwordVolley.length()==0)){
                    Toast.makeText(Registration_Form.this, "Email already registered", Toast.LENGTH_LONG).show();
                }

                else if(response.contains("true")){
                 /* Here launching another activity when login successful. If you persist login state
                     use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                    Toast.makeText(Registration_Form.this, "Successfully registered", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Registration_Form.this, LogIn.class);
                    startActivity(intent);
                }
                progDialog.hide();
                progDialog.dismiss();

            }}
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("LogIn: error", " check");
                progDialog.hide();
                progDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String,String>();

                if(emailVolley.length()==0 || passwordVolley.length()==0){

                    //Toast.makeText(Registration_Form.this, "Please enter a valid password/email", Toast.LENGTH_LONG).show();
                }
                else if(!(userTypeVolley.equals("buyers") || userTypeVolley.equals("seller"))){
                   // Toast.makeText(Registration_Form.this, "select \"buyers\" or \"seller\" to register", Toast.LENGTH_LONG).show();
                }
                else {
                    params.put(KEY_USERNAME, usernameVolley);
                    params.put(KEY_PASSWORD, passwordVolley);
                    params.put(KEY_EMAIL, emailVolley);
                    params.put(KEY_MOBILE, mobileVolley);
                    params.put(KEY_DOB, dobVolley);
                    params.put(KEY_ADDRESS1, address1Volley);
                    params.put(KEY_ADDRESS2, address2Volley);
                    params.put(KEY_USER_TYPE, userTypeVolley);
                }
                return params;
            }

        };
        Volley.newRequestQueue(this).add(strReq);
        // AppController.getInstance().addToRequestQueue(strReq, "string_req");
    }
    ////
}


