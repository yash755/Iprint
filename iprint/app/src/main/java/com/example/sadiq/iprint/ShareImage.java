package com.example.sadiq.iprint;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ShareImage extends AppCompatActivity {

    String selectedImagePath;
    EditText emailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Uri imageUri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
        selectedImagePath = ImageFilePath.getPath(getApplicationContext(), imageUri);

        emailid = (EditText)findViewById(R.id.email);

        System.out.println("Path" + selectedImagePath);

    }

    public void buttonUpload1(View v)
    {
        if ( selectedImagePath == null || emailid.getText().toString().equals(""))
        {
            Toast.makeText(this, "Something is empty or not selected", Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.out.println("Output is" + emailid + selectedImagePath);
            SendRequest sendRequest = new SendRequest(this);
            sendRequest.fetchuserdatainbackground(getApplicationContext(),emailid.getText().toString(),selectedImagePath);
        }
    }

}
