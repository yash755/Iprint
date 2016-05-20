package com.example.sadiq.iprint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class login extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
//    private String urlJsonObj = "http://gameworld.pythonanywhere.com/";
    private String urlJsonObj = "http://192.168.43.19:5000/auth/loginapp";
    private ProgressDialog pDialog;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login = (Button) findViewById(R.id.login);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail=(EditText)findViewById(R.id.etEmail);
                etPassword=(EditText)findViewById(R.id.etPassword);
                makeJsonObjectRequest();
            }
        });
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void makeJsonObjectRequest() {

        showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, urlJsonObj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplication(),"1",LENGTH_SHORT).show();
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("success"))
                    {
                        Toast.makeText(getApplication(),"Logged In",Toast.LENGTH_SHORT).show();
                        intent = new Intent(login.this,Imageupload.class);
                        intent.putExtra("email",etEmail.getText().toString());
                        startActivity(intent);

                    }else if (status.equals("error"))
                    {
                        Toast.makeText(getApplication(),"Invalid username or password",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getApplication(),"wtf server",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(getApplication(), "Login Failed :(", Toast.LENGTH_SHORT).show();
                }
                hidepDialog();
                //mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplication(),"No internet connection",Toast.LENGTH_SHORT).show();
                //mPostCommentResponse.requestEndedWithError(error);
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",etEmail.getText().toString());
                params.put("password",etPassword.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr);
    }

}
