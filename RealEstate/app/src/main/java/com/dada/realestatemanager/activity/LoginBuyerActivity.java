package com.dada.realestatemanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dada.realestatemanager.AppController;
import com.dada.realestatemanager.R;
import com.dada.realestatemanager.util.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginBuyerActivity extends AppCompatActivity {
    CheckBox rememberMeCheckBox;
    private EditText buyer_email, buyer_password;
    private TextView signInButton;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_buyer);

        buyer_email = (EditText) findViewById(R.id.buyer_email);
        buyer_password = (EditText) findViewById(R.id.buyer_password);
        signInButton = (TextView) findViewById(R.id.loginbuyer_textview_signinbutton);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.loginbuyer_checkbox_rememberme);

        buyer_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isEmailValid(buyer_email.getText().toString().trim())) {
                    buyer_email.setTextColor(Color.RED);
                } else {
                    buyer_email.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyer_email.getText().toString().equals("") || buyer_password.getText().toString().equals(""))
                    Toast.makeText(LoginBuyerActivity.this, "Username/Password Can NOT be empty!", Toast.LENGTH_LONG).show();
                else {
                    singIn();
                }
            }
        });


    }

    public void singIn() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,

                Constant.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(LoginBuyerActivity.this, response, Toast.LENGTH_LONG).show();
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject obj = array.getJSONObject(0);

                    Intent i = new Intent(LoginBuyerActivity.this, BuyerMainActivity.class);
                    i.putExtra("userid", obj.getString("User Id"));
                    i.putExtra("usertype", obj.getString("User Type"));
                    startActivity(i);

                } catch (Exception e) {
                }
                pDialog.hide();

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
                params.put("email", buyer_email.getText().toString());
                params.put("password", buyer_password.getText().toString());
                params.put("usertype", "buyer");
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
