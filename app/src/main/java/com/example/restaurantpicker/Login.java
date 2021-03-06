package com.example.restaurantpicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.restaurantpicker.Models.User;
import com.example.restaurantpicker.SharedPreferenceManager.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button loginButton;
    EditText editTextEmail, editTextPassword;
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, GetAllRestaurants.class));
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Login.this, UserRegistration.class));
            }
        });
    }

    private void userLogin() {
        boolean isValidated = validateData();
        if (!isValidated)
            return;
        final String tempEmail = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        String URL = Constants.SERVER + Constants.USER_LOGIN_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(),
                                        obj.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONObject userJson = obj.getJSONObject("user");
                                saveUserInformation(userJson);
                                divertToNextActivity();

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", tempEmail);
                params.put("password", password);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext())
                .addToRequestQueue(stringRequest, Constants.REQUEST_TAG);
    }

    private boolean validateData() {
        final String tempEmail = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(tempEmail)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void saveUserInformation(JSONObject userJson) throws JSONException {
        User user = new User(
                userJson.getString("id"),
                userJson.getString("name"),
                userJson.getString("email"),
                userJson.getString("phone")
        );
        //storing the user in shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
    }

    private void divertToNextActivity() {
        finish();
        startActivity(new Intent(getApplicationContext(), GetAllRestaurants.class));
    }

    private void initializeViews() {
        loginButton = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextUserEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
    }
}
