package com.grv.grvcabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements View.OnClickListener {

    public static final String LOGIN_URL = "http://gov.net16.net/GrvCabs/Login.php";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public static final String KEY_USERNUMBER = "usernumber";
    public static final String KEY_PASSWORD = "password";

    private EditText user_number;
    private EditText user_pass;

    private String usernumber;
    private String password;
    public ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user_number = (EditText) findViewById(R.id.input_mobile_number);
        user_pass = (EditText) findViewById(R.id.input_password);
        findViewById(R.id.searchUser).setOnClickListener(this);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.searchUser:
                userLogin();

        }


    }

    private void userLogin() {


        if (user_number.getText().toString().length() == 0 && user_pass.getText().toString().length() == 0) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
            v.vibrate(400);
            user_number.setError("Phone Number cannot be blank");
            user_pass.setError("Password cannot be blank");
        } else if (user_number.getText().toString().length() < 10 && user_pass.getText().toString().length() <= 4) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
            v.vibrate(400);
            user_number.setError("Invalid Phone Number");
            user_pass.setError("Atleast 5 letters");
        } else if (user_number.getText().toString().length() == 10 && user_pass.getText().toString().length() <= 4) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
            v.vibrate(1000);
            user_pass.setError("Atleast 5 letters");
        } else if (user_number.getText().toString().length() > 10 && user_pass.getText().toString().length() <= 4) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
            v.vibrate(400);
            user_number.setError("Invalid Phone Number");
            user_pass.setError("Atleast 5 letters");
        } else if (user_number.getText().toString().length() < 10 && user_pass.getText().toString().length() > 4) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
            v.vibrate(400);
            user_number.setError("Invalid Phone Number");
        } else if (user_number.getText().toString().length() > 10 && user_pass.getText().toString().length() >= 4) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
            v.vibrate(400);
            user_number.setError("Invalid Phone Number");
        } else if (user_number.getText().toString().length() == 10 && user_pass.getText().toString().length() > 4) {

            // code goes here for server side

            usernumber = user_number.getText().toString().trim();
            password = user_pass.getText().toString().trim();
            loading = ProgressDialog.show(this, "Please wait...", "Processing...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().contains("success")) {
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                loading.dismiss();
// Vibrate for 400 milliseconds
                                v.vibrate(400);
                                openProfile();
                            } else {
                                loading.dismiss();
                                Toast.makeText(Login.this, "Details Incorrect", Toast.LENGTH_LONG).show();
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
                                v.vibrate(400);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(KEY_USERNUMBER, usernumber);
                    map.put(KEY_PASSWORD, password);
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }


    private void openProfile() {
        Intent intent = new Intent(this, MainPage.class);
        intent.putExtra(KEY_USERNUMBER, usernumber);
        startActivity(intent);
        finish();


        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("number", user_number.getText().toString());
        editor.commit();
    }


}

