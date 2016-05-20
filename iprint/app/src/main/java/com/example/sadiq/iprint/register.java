package com.example.sadiq.iprint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText RName;
    EditText REmail;
    EditText RPassword;
    Button bRegister;
    String name,email,pass,cpass;
    int pstatus=0;
    private String urlJsonObj = "http://gameworld.pythonanywhere.com/signup";
    private ProgressDialog pDialog;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RName = (EditText) findViewById(R.id.RName);
        REmail = (EditText) findViewById(R.id.REmail);
        RPassword = (EditText) findViewById(R.id.RPassword);
        bRegister = (Button) findViewById(R.id.bRegister);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignValues();
                makeJsonObjectRequest();
            }
        });
    }

        private void assignValues(){
        name=RName.getText().toString();
        email=REmail.getText().toString();
        pass=RPassword.getText().toString();
        }


        private void showpDialog() {
                if (!pDialog.isShowing())
                    pDialog.show();
            }

        private void hidepDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    private void makeJsonObjectRequest()
    {
//        Toast.makeText(getApplication(),fname + "," + lname + "," + uname + "," + email + "," + pass + "," + cpass + "," +
//                sex + "," + dob + "," + coun + "," + desc,Toast.LENGTH_LONG).show();

        showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, urlJsonObj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("success"))
                    {
                        Toast.makeText(getApplication(),"Account Successfully Created :D",Toast.LENGTH_SHORT).show();
                        intent = new Intent(register.this,login.class);
                        startActivity(intent);
                    }else if (status.equals("error"))
                    {
                        Toast.makeText(getApplication(),"Email or Username already exists :/",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getApplication(),"wtf server",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getApplication(), "Sign Up Failed :(", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No internet connection",Toast.LENGTH_SHORT).show();
                System.out.println(error);
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("email",email);
                params.put("password",pass);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);

    }

}
