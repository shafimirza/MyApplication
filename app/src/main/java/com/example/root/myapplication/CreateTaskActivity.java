package com.example.root.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class CreateTaskActivity extends ActionBarActivity {
    private EditText eTitle, eDesc;
    Spinner espin;

    //ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        eTitle = (EditText) findViewById(R.id.tasktitle);
        eTitle.setInputType(InputType.TYPE_NULL);
        eDesc = (EditText) findViewById(R.id.taskdesc);
        eDesc.setInputType(InputType.TYPE_NULL);
        espin = (Spinner) findViewById(R.id.spinner);
        Button task = (Button) findViewById(R.id.addTask);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Toast.makeText, "test", Toast.LENGTH_LONG).show();
                Log.d("RegisterUser", "Test");
                Log.d("createuser", "button pressed");
                new CreateTaskTask().execute();
                Log.d("createtask", "craetetask finished");
            }
        });



                /*@Override
                public boolean onCreateOptionsMenu (Menu menu){
                    // Inflate the menu; this adds items to the action bar if it is present.
                    getMenuInflater().inflate(R.menu.menu_create_task, menu);
                    return true;
                }

                @Override
                public boolean onOptionsItemSelected (MenuItem item) {
                    // Handle action bar item clicks here. The action bar will
                    // automatically handle clicks on the Home/Up button, so long
                    // as you specify a parent activity in AndroidManifest.xml.
                    int id = item.getItemId();

                    //noinspection SimplifiableIfStatement
                    if (id == R.id.action_settings) {
                        return true;
                    }

                    // return super.onOptionsItemSelected(item);


                    //public void addTask (View view) {
                    // //   final ProgressDialog prgDialog = new ProgressDialog(this);
                    //}

                    // EditText eTitle = (EditText) findViewById(R.id.tasktitle);
                    //eTitle.setInputType(InputType.TYPE_NULL);
                    //EditText eDesc = (EditText) findViewById(R.id.taskdesc);
                    //eDesc.setInputType(InputType.TYPE_NULL);


                    //Spinner eSpin = (Spinner) findViewById(R.id.spinner);


                    //Button addtask = (Button) findViewById(R.id.addTask);
                    //addtask.setOnClickListener(new View.OnClickListener() {
                    //  @Override
                    // public void onClick(View v) {
                    //Toast.makeText("test", Toast.LENGTH_LONG).show();
                    //    Log.d("RegisterUser", "Test");
                    //  Log.d("createuser", "button pressed");
                    ///new CreateTaskTask().execute();
                    //Log.d("createtask", "craetetask finished");
                    // final ProgressDialog prgDialog = new ProgressDialog(getActivity());
                    //egister();


                    //    prgDialog.show();

                    //new SendTask().execute(tTitle, tDesc, tSpin);


        /*
        RequestParams params = new RequestParams();
        params.put("title", tTitle);
        params.put("description", tDesc);
        params.put("schedule", tSpin);

        prgDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.2.128/econolodgeapp/addtask.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int responseCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                Toast.makeText(getApplicationContext(), "Task Added Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                String response = new String(bytes);
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Log.d("CreateTaskNetwork", "in StatusCode = 500");
                    Log.d("CreateTaskNetwork", response);
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

    /*class SendTask extends AsyncTask<String, Void, String>
    {
        String tag = "SendTask";
        @Override
        protected String doInBackground(String... args) {
            String error_msg = "empty";
            try {
                String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(args[0], "UTF-8");
                data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(args[1], "UTF-8");
                data += "&" + URLEncoder.encode("schedule", "UTF-8") + "=" + URLEncoder.encode(args[2], "UTF-8");

                URL url = new URL("http://192.168.2.125/econolodgeapp/addtask.php");

                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();


                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if((error_msg = reader.readLine()) == null)
                {
                    Log.d(tag, "Reader null");
                }
                else
                    Log.d(tag, error_msg);

            } catch (Exception e) {
                error_msg = "Error: Network Exception";
                e.printStackTrace();
            }
            return error_msg;
        }
        @Override
        public void onPostExecute(String msg) {
            prgDialog.hide();  //doesn't work
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        } */
        ;
    }

    public class CreateTaskTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            // Get Last_name Edit View Value
            // EditText eTitle = (EditText) findViewById(R.id.tasktitle);
            String tTitle = eTitle.getText().toString();

            //EditText eDesc = (EditText) findViewById(R.id.taskdesc);
            String tDesc = eDesc.getText().toString();

            Spinner eSpin = (Spinner) findViewById(R.id.spinner);
            String tSpin = eSpin.getSelectedItem().toString();

            String tag = "CreateTaskTask";
            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.AndroidToServer.CreateTaskTask createTask = new Message.AndroidToServer.CreateTaskTask(tTitle, tDesc, tSpin);

                Log.d("task", "title:" + createTask.gettDesc());
                System.err.println("title" + "first_name");


                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                Log.d(tag, "Created Socket");
                System.err.println("Created Socket");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Log.d(tag, "Created output");
                System.err.println("Created output");

                Log.d("task", "title:" + createTask.gettTitle() + "first_name" + createTask.gettDesc());
                out.writeObject(createTask);

                Log.d(tag, "Wrote Output");
                System.err.println("Wrote output");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d("OnpostExecute", "Create task task finished");
        }
    }

}

