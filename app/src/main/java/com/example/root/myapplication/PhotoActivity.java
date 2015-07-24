package com.example.root.myapplication;

import android.content.Context;
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


public class
        PhotoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
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

    public void picClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, SUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUCCESS) {
            if (resultCode == RESULT_OK) {
                pic = data.getData();
                Bitmap bmPic = (Bitmap) data.getExtras().get("data");
                PictureTask background = new PictureTask(this);
                background.execute(bmPic);
            }
        }
    }

    private class PictureTask extends AsyncTask<Bitmap, Void, Void> {

        private Context context;

        private PictureTask(Context c) {
            context = c;
        }

        @Override
        protected Void doInBackground(Bitmap... args) {
            Bitmap download = null;
            String line = null;
            //compress and encode
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            args[0].compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] byte_arr = stream.toByteArray();
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy_hhmmss");
            String timeString = s.format(new Date());
            Log.d("Time", timeString);
            //upload
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
