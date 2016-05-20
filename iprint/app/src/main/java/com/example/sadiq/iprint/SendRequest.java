package com.example.sadiq.iprint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sadiq on 5/12/2016.
 */
public class SendRequest{

    ProgressDialog progressDialog;

    public SendRequest (Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Working....");
        progressDialog.setMessage("Please Wait");


    }


    public void fetchuserdatainbackground(Context context,String email,String url){
        progressDialog.show();
        new fetchuserdataasynctask(context,email,url).execute();
    }




    public class fetchuserdataasynctask extends AsyncTask<Void,Void,String> {

        String email;
        String url;
        Context context;



        public fetchuserdataasynctask(Context context,String email,String url){

            this.email = email;
            this.url = url;
            this.context = context;


        }

        @Override
        protected String doInBackground(Void... voids) {

            String fileName = url;
            int serverResponseCode = 0;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(url);

            System.out.println("I am here ");

            try {

                System.out.println();
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://192.168.43.19:5000/uploademail");

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(60000);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("image", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                // add parameters
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"email\""
                        + lineEnd);
                dos.writeBytes(lineEnd);

                // assign value
                dos.writeBytes(email);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);




                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                //Get Response
                InputStream is = conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();


                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

                //     System.out.println("Response");
                System.out.println("Response" + response.toString() + "1");
                String aresponse = response.substring(2,response.length()-3);
                return aresponse;

            } catch (MalformedURLException ex) {

                progressDialog.dismiss();
                ex.printStackTrace();
                return  "MalformedURLException Exception";


            } catch (Exception e) {

                progressDialog.dismiss();
                e.printStackTrace();
                return "Time Out try later !!!";

            }




        } // End else block


        @Override
        protected void onPostExecute(String response) {

            progressDialog.dismiss();
            System.out.println("Response" + response + "2");
           // Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

            super.onPostExecute(null);

        }
    }
}

