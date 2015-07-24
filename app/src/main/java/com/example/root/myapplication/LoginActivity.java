package com.example.root.myapplication;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class LoginActivity extends Activity {
    String userPosition;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // User Id Edit View Object
    EditText useridET;
    // Passwprd Edit View Object
    EditText pwdET;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorMsg = (TextView)findViewById(R.id.login_error);

        useridET = (EditText)findViewById(R.id.loginUserid);

        pwdET = (EditText)findViewById(R.id.loginPassword);

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Please wait...");

        prgDialog.setCancelable(false);
        /*button.findViewById(R.id.Wifi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getBaseContext(), MainWifiActivity.class);
            }
        });*/

    }


    public void login(View view) {
        Log.d("login", "button pressed");
        new LoginTask().execute();
        Log.d("login", "logintask finished");
    }

    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void loginUser(View view){
        // Get Email Edit View Value
        String email = useridET.getText().toString();
        // Get Password Edit View Value
        String password = pwdET.getText().toString();
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        // When Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(email) && Utility.isNotNull(password)){
            // When Email entered is Valid

            // Put Http parameter username with value of Email Edit View control
            params.put("username", email);
            // Put Http parameter password with value of Password Edit Value control
            params.put("password", password);
            // Invoke RESTful Web Service with Http parameters
            invokeWS(params);
            // When Email is invalid

        } else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://s/JPropertyWebService/login/dologin", params, new AsyncHttpResponseHandler() {
            // client.get("http://192.168.1.2:8080/JPropertyWebService/login/dologin", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int responseCode, Header[] responseHeaders, byte[] responsebody) {
                String response = new String(responsebody);
                // Hide Progress Dialog
                //prgDialog.hide();
                try {

                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        prgDialog.hide();
                        errorMsg.setText("");
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        userPosition = obj.getString("error_msg");
                        // Navigate to Home screen

                        navigatetoRelatedActivity();
                    }
                    // Else display error message
                    else {
                        prgDialog.hide();
                        errorMsg.setText(obj.getString("error_msg"));
                        // Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoRelatedActivity(){
        if(userPosition.equals("MANAGER")||userPosition.equals("FRONT DESK")) {
            Intent homeIntent = new Intent(getApplicationContext(), FragmentContainerActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }/*
        if(userPosition.equals("HOUSEKEEPER")) {
            Intent homeIntent = new Intent(getApplicationContext(), HouseKeeping.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
        if(userPosition.equals("MAINTENANCE")) {
            Intent homeIntent = new Intent(getApplicationContext(), Maintenance.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }*/
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param
     *//*
    public void navigatetoRegisterActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }*/
    //private OnMessageRecieved messageRecieved;
    public class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            // Get Email Edit View Value
            String uid = useridET.getText().toString();
            // Get Password Edit View Value
            String password = pwdET.getText().toString();

            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.AndroidToServer.LogInMessage logInMessage = new Message.AndroidToServer.LogInMessage(uid, password);

                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                Log.d("LogInTask", "printing uname:" + logInMessage.getUsername() + " password:" + logInMessage.getPassword());
                out.writeObject(logInMessage);

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

    }

}
