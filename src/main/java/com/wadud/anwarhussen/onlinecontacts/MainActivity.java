package com.wadud.anwarhussen.onlinecontacts;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, password, rePassword, emailSign, passwordSign;
    Button register, signIn;
    String urlReg = "http://mahwadud.net16.net/contact/createtable.php";
    String urlLogIN = "http://mahwadud.net16.net/contact/login.php";
    final String KEY_EMAIL = "email";
    final String KEY_PASSWORD = "password";
    final String KEY_TABLE = "tablename";
     String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        rePassword = (EditText) findViewById(R.id.editTextRePassword);
        emailSign = (EditText) findViewById(R.id.editTextEmailSignIn);
        passwordSign = (EditText) findViewById(R.id.editTextPasswordSignIn);

        register = (Button) findViewById(R.id.buttonRegister);
        signIn = (Button) findViewById(R.id.buttonRegisterSignIn);
        register.setOnClickListener(this);
        signIn.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Developed by M A H WADUD", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == register) {
            final String Email = email.getText().toString().trim();
            final String username[] = Email.split("@");
            //final String
                    tableName = username[0];
            final String Password = password.getText().toString().trim();
            String RePassword = rePassword.getText().toString().trim();
            Toast.makeText(getApplicationContext(), Email + "  " + Password + " " + RePassword + "  " + tableName, Toast.LENGTH_LONG).show();
            if (Password.equals(RePassword)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlReg,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                StringTokenizer token = new StringTokenizer(response);
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                                if (token.nextToken().equals("success")) {
                                    goToAfterLogInActivity();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                Log.e("error", error.getMessage() + "  " + error.toString());

                            }
                        }
                ) {

                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_EMAIL, Email);
                        params.put(KEY_PASSWORD, Password);
                        params.put(KEY_TABLE, tableName);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            } else {
                Toast.makeText(getApplicationContext(), "Please Enter Correct password", Toast.LENGTH_LONG).show();
            }
        }
        if (v == signIn) {
          //  goToAfterLogInActivity();

            final String EmailSign = emailSign.getText().toString();
            final String username[] = EmailSign.split("@");
            //final String
            tableName = username[0];
            final String PasswordSign = passwordSign.getText().toString();
            Toast.makeText(getApplicationContext(), EmailSign + "  " + PasswordSign, Toast.LENGTH_LONG).show();
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, urlLogIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            StringTokenizer tokenizer = new StringTokenizer(response);
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                            if (tokenizer.nextToken().equals("success")) {
                                goToAfterLogInActivity();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            Log.e("error", error.getMessage() + "  " + error.toString());

                        }
                    }
            ) {

                protected Map<String, String> getParams() {
                    Map<String, String> paramss = new HashMap<String, String>();
                    paramss.put(KEY_EMAIL, EmailSign);
                    paramss.put(KEY_PASSWORD, PasswordSign);
                    return paramss;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest1);


        }
    }

    public void goToAfterLogInActivity() {

        Intent intent = new Intent(MainActivity.this, AfterLogInActivity.class);
        intent.putExtra("tname",tableName);
        startActivity(intent);
    }
}
