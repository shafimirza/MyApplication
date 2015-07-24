package com.example.root.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class JavaPhotoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_photo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_java_photo, menu);
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
    private static final int SUCCESS = 1;
    private Uri pic;
    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, SUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUCCESS) {
            if (resultCode == RESULT_OK) {
                pic = data.getData();
                Bitmap bmPic = (Bitmap) data.getExtras().get("data");
                PictureTask background = new PictureTask();
                background.execute(bmPic);
            }
        }
    }

    class PictureTask extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected Void doInBackground(Bitmap... arg) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            arg[0].compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] byte_arr = stream.toByteArray();
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy_hhmmss");
            String timeString = s.format(new Date());
            Log.d("Time", timeString);

            try{
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.AndroidToServer.PictureMessage pictureMessage = new Message.AndroidToServer.PictureMessage(byte_arr, timeString);

                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(pictureMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
