package com.example.monisha.propertymarketing.AuthenticationForms;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.monisha.propertymarketing.App.AnalyticsTrackers;
import com.example.monisha.propertymarketing.Constants;
import com.example.monisha.propertymarketing.Interface.DaggerLogInComponent;
import com.example.monisha.propertymarketing.Interface.LogInComponent;
import com.example.monisha.propertymarketing.Main2Activity;
import com.example.monisha.propertymarketing.MainActivity;
import com.example.monisha.propertymarketing.Modules.NetworkConnectionModule;
import com.example.monisha.propertymarketing.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
   private static final String TWITTER_KEY = "f4oU3YOURYJFnb0bOdOCxTLbC";
    private static final String TWITTER_SECRET = "HWZB87EqYwJBc688gerHuGE2gY6gctBfEr4vYfGV01Wk8jKloB";

    private TwitterLoginButton loginButton;
    //TextView textView;
    TwitterSession session;

    static EditText eEmail;
    EditText ePassword;

    Button bBuyersLogin;
    Button bSellersLogin;

    Button bRegistration;

    //volley request declaration

    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_USER_TYPE="usertype";

    String emailVolley;
    String passwordVolley;
    String userTypeVolley;
    boolean flagSelectedType=false;
    private ProgressDialog progDialog;
    private String urlLogin;


    //Google Analytics
    public static final String TAG = LogIn.class
            .getSimpleName();

    private static LogIn mInstance;
    ////

    //SharedPreferences for user authentication
    SharedPreferences sharedPreferences;

    Constants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_log_in);

        //creating Constants object with daggers 2
        LogInComponent component= DaggerLogInComponent.builder().networkConnectionModule(new NetworkConnectionModule()).build();
        constants=component.provideConstants();



        userTypeVolley=" ";
        Spinner spinner = (Spinner) findViewById(R.id.user_type);

        // Initializing a String Array
        String[] plants = new String[]{
                "",
                "Buyer",
                "Seller"

        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_user_type_login,R.id.user_type, plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_user_type_login);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        userTypeVolley="";
                        flagSelectedType=false;
                        break;
                    case 1:
                        userTypeVolley="buyers";
                        flagSelectedType=true;
                        break;
                    case 2:
                        userTypeVolley="seller";
                        flagSelectedType=true;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Google Analytics
        mInstance = this;

        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        ////

        //twitter
                    loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

                    loginButton.setCallback(new Callback<TwitterSession>() {
                        @Override
                        public void success(Result<TwitterSession> result) {

                            session = result.data;

                            String username = session.getUserName();
                            Long userid = session.getUserId();

                            if (userTypeVolley.equals("buyers") && (flagSelectedType=true)) {
                                Toast.makeText(LogIn.this, "BUYER", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LogIn.this, MainActivity.class);
                                startActivity(intent);
                            } else if (userTypeVolley.equals("seller") && (flagSelectedType=true)) {
                                Toast.makeText(LogIn.this, "SELLER", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LogIn.this, Main2Activity.class);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(LogIn.this, LogIn.class);
                                startActivity(intent);
                                Toast.makeText(LogIn.this, "Selected a user type first", Toast.LENGTH_LONG).show();
                            }


                            getUserData();

                        }

                        @Override
                        public void failure(TwitterException exception) {
                            Log.d("TwitterKit", "Login with Twitter failure", exception);
                        }
                    });

        ////
        bBuyersLogin=(Button)findViewById(R.id.bBuyersLogin);
        bSellersLogin=(Button)findViewById(R.id.bSellersLogin);

        bRegistration = (Button)findViewById(R.id.bRegistration);


        bRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, Registration_Form.class);
                startActivity(intent);
            }
        });

        eEmail=(EditText)findViewById(R.id.eEmail);
        ePassword=(EditText)findViewById(R.id.ePassword);

        //initialize sharedPreferencs obj
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        ////

        //volley request initialization
        progDialog = new ProgressDialog(this);
        progDialog.setMessage("Loading...");
        progDialog.setCancelable(false);
        ////
        bBuyersLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=eEmail.getText().toString();
                final String password=ePassword.getText().toString();
                final String userType="buyers";
                //volley req call
                makeJsonStringReq(email, password, userType);
                ////
            }
        });

        bSellersLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=eEmail.getText().toString();
                final String password=ePassword.getText().toString();
                final String userType="seller";
                //volley req call

                makeJsonStringReq(email, password, userType);
                ////
            }
        });
    }


    //Twitter
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }*/

    void getUserData() {
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false);/*new Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {

                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        String twitterImage = user.profileImageUrl;

                        try {
                            Log.d("imageurl", user.profileImageUrl);
                            Log.d("name", user.name);
                            //Log.d("email",user.email);
                            Log.d("des", user.description);
                            Log.d("followers ", String.valueOf(user.followersCount));
                            Log.d("createdAt", user.createdAt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                });*/


    }

    ////
    //volley post
    private void makeJsonStringReq(String email, String password, String userType){

       // urlLogin="http://rjtmobile.com/realestate/register.php?login";
        emailVolley=email.trim();
        passwordVolley=password.trim();
        userTypeVolley = userType.trim();
        progDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, constants.getLoginURL(), new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                        Log.i("responseiss" , response);
                        if(emailVolley.length()==0 || passwordVolley.length()==0){
                            Toast.makeText(LogIn.this, "Please enter a valid email/password", Toast.LENGTH_LONG).show();
                        }
                        else if(response.contains("seller")){
                            progDialog.dismiss();
                            Toast.makeText(LogIn.this, "Successfully logged in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(response.contains("buyers")){
                            progDialog.dismiss();
                            Toast.makeText(LogIn.this, "Successfully logged in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LogIn.this, Main2Activity.class);
                            startActivity(intent);
                        }

                        else if(response.equals("[]")){
                            Toast.makeText(LogIn.this, "Invalid number or password", Toast.LENGTH_LONG ).show();
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
                params.put(KEY_EMAIL, emailVolley);
                params.put(KEY_PASSWORD, passwordVolley);
                params.put(KEY_USER_TYPE, userTypeVolley);

                return params;
            }

        };
        Volley.newRequestQueue(this).add(strReq);
       // AppController.getInstance().addToRequestQueue(strReq, "string_req");
        }
////

    //Google Analytics
    public static synchronized LogIn getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    ////


    // Voice command
    public void onButtonClick(View v){
        if(v.getId()==R.id.imageButton){
            promptSpeechInput();
        }
    }

    public void promptSpeechInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: \" remember \" to save your credentials,\n Say: \" display \" to display your credentials");

        try {
            startActivityForResult(i, 100);

        }catch(ActivityNotFoundException a){
            Toast.makeText(LogIn.this, "Sorry, your device doesn't support speech language,", Toast.LENGTH_LONG).show();
        }
    }
    public void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code, result_code, i);
        loginButton.onActivityResult(request_code, result_code, i);

        switch(request_code){
            case 100: if(result_code==RESULT_OK && i!=null){
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if(result.get(0).equals("remember")) {
                    Toast.makeText(LogIn.this, "credentials saved", Toast.LENGTH_LONG).show();
                    String email = eEmail.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    String password=ePassword.getText().toString();
                    editor.putString("password", password);

                    editor.commit();
                }
                else if(result.get(0).equals("display")){
                    Toast.makeText(LogIn.this, "credentials filled successfully", Toast.LENGTH_LONG).show();
                    eEmail.setText(sharedPreferences.getString("email", ""));
                    ePassword.setText(sharedPreferences.getString("password", ""));
                }
                else{
                    Toast.makeText(LogIn.this, "command not recognized", Toast.LENGTH_LONG).show();
                }
            }
                break;
        }
    }
    //// Voice command end
}