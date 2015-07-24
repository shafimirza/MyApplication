package com.example.root.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.econolodge.econolodgeapp3.Message.SERVERIP;
import static com.econolodge.econolodgeapp3.Message.SERVERPORT;

public class MPhotoActivity2 extends ActionBarActivity {
    private Uri pic;
    Integer[] imageIDs;
    ScrollView parent;
     private class Image
    {
        public String status;
        public Bitmap bitmap;
        Image(String status, Bitmap bitmap)
        {
            this.status = status;
            this.bitmap = bitmap;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mphoto_activity2);
        Gallery gallery = (Gallery) findViewById(R.id.gallery1);
        new GetTask().execute();
//the images to display
        // get the listview
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mphoto_activity2, menu);
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
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Picture Task~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class GetTask extends AsyncTask<Message.AndroidToServer.dataToBeRequested,Void,Message.ServerToAndroid.returnTaskImageData> {
        @Override
        protected Message.ServerToAndroid.returnTaskImageData doInBackground(Message.AndroidToServer.dataToBeRequested... params) {
            // boolean mRun = true;
            Object inMessage;
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                Socket socket = new Socket(serverAddr, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message.AndroidToServer AS = new Message.AndroidToServer();
                AS.setRequest(Message.AndroidToServer.dataToBeRequested.TASKIMAGEDATA);
                System.out.println(AS);
                out.writeObject(AS);
                inMessage = in.readObject();
                return (Message.ServerToAndroid.returnTaskImageData)inMessage;
            } catch (Exception e){
                e.printStackTrace();
                return  new Message.ServerToAndroid.returnTaskImageData();
            }
        }
        protected void onPostExecute(Message.ServerToAndroid.returnTaskImageData params) {
            Message.ServerToAndroid.TaskImageClass tidc = params.getNextImage();
            System.out.println("ENTERED OnPostExecute");
            while (tidc != null) {
                imageIDs = new Integer[Integer.parseInt(tidc.getFile())];
                System.out.println(imageIDs);
                tidc = params.getNextImage();
            }
            final Integer[] finalimageIDs = imageIDs;
            Gallery gallery = (Gallery) findViewById(R.id.gallery1);
            gallery.setAdapter(new ImageAdapter(MPhotoActivity2.this));
            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Toast.makeText(getBaseContext(), "pic" + (position + 1) + " selected",
                            Toast.LENGTH_SHORT).show();
                    ImageView imageView = (ImageView) findViewById(R.id.image1);
                    imageView.setImageResource(finalimageIDs[position]);
                }
            });}}
            public  class ImageAdapter extends BaseAdapter {
                private Context context;
                private int itemBackground;
                public ImageAdapter(Context c) {
                    context = c;// sets a grey background; wraps around the images
                    TypedArray a = obtainStyledAttributes(R.styleable.Theme);
                    itemBackground = a.getResourceId(R.styleable.ActionBar_background, 0);
                    a.recycle();
                }
                // returns the number of images
                public int getCount() {
                    return imageIDs.length;
                }
                // returns the ID of an item
                public Object getItem(int position) {
                    return position;
                }
                // returns the ID of an item
                public long getItemId(int position) {
                    return position;
                }
                // returns an ImageView view
                public View getView(int position, View convertView, ViewGroup parent) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(imageIDs[position]);
                    imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
                    imageView.setBackgroundResource(itemBackground);
                    return imageView;
                }
            }}
