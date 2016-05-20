package com.example.sadiq.iprint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Imageupload extends AppCompatActivity {

    public static final String UPLOAD_URL = "";
    public static final String EMAIL_KEY = "email";
    public static final String UPLOAD_KEY = "image";

   // private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;
  //  private  String email;
    EditText emailid;

   // private Bitmap bitmap;

   // private Uri filePath;

    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_CAMERA     = 2;

    static final int SELECT_PDF = 3;

    private Bitmap bitmap;
    String selectedImagePath;
    ImageView i1;

    Uri picUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);

        Intent intent = new Intent();
       // email = intent.getStringExtra(EMAIL_KEY);

        emailid = (EditText)findViewById(R.id.email);
        System.out.println(emailid);

    }

    public void buttonChoose(View v)
    {
        selectImage();
    }

    public void buttonUpload(View v)
    {
        if ( selectedImagePath == null || emailid.getText().toString().equals(""))
        {
            Toast.makeText(this,"Something is empty or not selected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.out.println("Output is" + emailid + selectedImagePath);
           SendRequest sendRequest = new SendRequest(this);
            sendRequest.fetchuserdatainbackground(getApplicationContext(),emailid.getText().toString(),selectedImagePath);
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "PDF Upload","Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Imageupload.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File file=getOutputMediaFile(1);
                    picUri = Uri.fromFile(file); // create
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,picUri); // set the image file

                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            PICK_IMAGE_REQUEST);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("PDF Upload")){
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select PDF "), SELECT_PDF);
                }
            }
        });
        builder.show();
    }

    /** Create a File for saving an image */
    private  File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpeg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

            Uri uri=picUri;

            selectedImagePath = ImageFilePath.getPath(getApplicationContext(), uri);
            System.out.println("File Path " + selectedImagePath);


       /*     try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                i1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        }

        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri selectedImageUri = data.getData();

            //MEDIA GALLERY
            selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
            System.out.println("File Path " + selectedImagePath);


         /*   try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                i1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }else if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PDF)
            {
                System.out.println("SELECT_PDF");
                Uri selectedImageUri = data.getData();
                selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
                System.out.println("File Path : " + selectedImagePath);

            /*    try {
                    System.out.println("NO Exception");
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    i1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    System.out.println("IO Exception");
                    e.printStackTrace();
                }*/
            }

        }
    }

}