package com.example.root.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SocketInputTest extends ActionBarActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_input_test);
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_socket_input_test, menu);
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


   /* public void send(View view) {
        EditText et = (EditText) findViewById(R.id.edittext);
        Message.AndroidToServer send = new Message.AndroidToServer(et.getText().toString());
        new GetInput().execute(send);
    }

    public class GetInput extends AsyncTask<Message.AndroidToServer, Void, Message.ServerToAndroid> {
        @Override
        protected Message.ServerToAndroid doInBackground(Message.AndroidToServer... args) {
            //boolean mRun = true;
            Object inMessage;
            try{
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);

                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


                out.writeObject(args[0]);

                inMessage = in.readObject();

            } catch (Exception e) {
                e.printStackTrace();
                return new Message.ServerToAndroid("ERROR: Exception");
            }
            if(inMessage instanceof Message.ServerToAndroid)
            {
                return (Message.ServerToAndroid) inMessage;
            } else
                return new Message.ServerToAndroid("Error: Empty");
        }

        @Override
        protected void onPostExecute(Message.ServerToAndroid args) {
            //text.setText(args.getMessage());
        }
    }*/

}
