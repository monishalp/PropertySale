package com.dada.realestatemanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dada.realestatemanager.AppController;
import com.dada.realestatemanager.util.Constant;
import com.dada.realestatemanager.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ProgressDialog pDialog;

    private EditText username, password, email, mobile, DOB, address1, address2;
    private TextView register_button;
    private String userType = "buyer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_button = (TextView) findViewById(R.id.register_submit_button);
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        mobile = (EditText) findViewById(R.id.register_mobile);
        address1 = (EditText) findViewById(R.id.register_address_1);
        address2 = (EditText) findViewById(R.id.register_address_2);

        DOB = (EditText) findViewById(R.id.register_dob);
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        RegisterActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isEmailValid(email.getText().toString().trim())) {
                    email.setTextColor(Color.RED);
                } else {
                    email.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText, passwordText, emailText, mobileText, addressText1, addressText2, dobText;
                usernameText = username.getText().toString().trim();
                passwordText = password.getText().toString();
                emailText = email.getText().toString().trim();
                mobileText = mobile.getText().toString();
                addressText1 = address1.getText().toString().trim();
                addressText2 = address2.getText().toString().trim();
                dobText = DOB.getText().toString().trim();
                if (usernameText.equals("") || passwordText.equals("") || emailText.equals("") || mobileText.equals("") ||
                        addressText1.equals("") || addressText2.equals("") || dobText.equals("")) {

                    Toast.makeText(RegisterActivity.this, "Please fill all fields!", Toast.LENGTH_LONG).show();
                } else if (!isEmailValid(email.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "Invalid Email!", Toast.LENGTH_LONG).show();
                } else {
                    registeration();
                }
            }
        });
    }

    public void registeration() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,

                Constant.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, "Sign Up Success", Toast.LENGTH_LONG).show();
                pDialog.hide();

                new CountDownTimer(1000, 500){

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        startActivity(new Intent(RegisterActivity.this, SelectBuyerSellerActivity.class));
                    }
                }.start();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", mobile.getText().toString());
                params.put("address1", address1.getText().toString());
                params.put("address2", address2.getText().toString());
                params.put("usertype", userType);
                params.put("usertstatus", "yes");

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        DOB.setText(date);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.usertyle_buyer:
                if (checked)
                    userType = "buyer";
                break;
            case R.id.usertyle_seller:
                if (checked)
                    userType = "seller";
                break;
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
