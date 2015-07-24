package com.example.root.myapplication;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateUserFragment extends Fragment {


    private EditText txtDate, leavingDate, hirDate, fname, lname, citi, zipp, phoneno, e_mail, addres, ss, userid, password, stat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog leaveDialog;
    private DatePickerDialog hireDialog;
    private SimpleDateFormat dateFormatter;
    private View rootView;
    private Spinner employeetype;
    private Spinner activ;

   /* private String txtD;
    private String lDate;
    private String hDate;
    private String sFName;
    private String slname;
    private String scity;
    private String szip;
    private String sphoneno;
    private String semail;
    private Spinner employeetype;
    private String spinner;
    private Spinner active;
    private String spinner2;
    private String saddress;
    private String sssn;
    private String suserid;
    private String spassword;*/

    ProgressDialog prgDialog;

    public CreateUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_user, container, false);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);




        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Adding Task"); //doesn't work
        prgDialog.setCancelable(false);
        Log.d("CreateTask", "log working");

        fname = (EditText) rootView.findViewById(R.id.fName);
        fname.setInputType(InputType.TYPE_NULL);
        lname = (EditText) rootView.findViewById(R.id.lName);
        lname.setInputType(InputType.TYPE_NULL);
        addres = (EditText) rootView.findViewById(R.id.address);
        addres.setInputType(InputType.TYPE_NULL);
        citi = (EditText) rootView.findViewById(R.id.city);
        citi.setInputType(InputType.TYPE_NULL);
        zipp = (EditText) rootView.findViewById(R.id.zip);
        zipp.setInputType(InputType.TYPE_NULL);
        stat = (EditText) rootView.findViewById(R.id.state);
        stat.setInputType(InputType.TYPE_NULL);

        phoneno = (EditText) rootView.findViewById(R.id.phone);
        phoneno.setInputType(InputType.TYPE_NULL);
        e_mail = (EditText) rootView.findViewById(R.id.registerEmail);
        e_mail.setInputType(InputType.TYPE_NULL);
        fname = (EditText) rootView.findViewById(R.id.fName);
        fname.setInputType(InputType.TYPE_NULL);
        txtDate = (EditText) rootView.findViewById(R.id.txtDate);
        txtDate.setInputType(InputType.TYPE_NULL);
        //txtDate.requestFocus();
        final Calendar newCalendar = Calendar.getInstance();
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        txtDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            }
        });
        leavingDate = (EditText) rootView.findViewById(R.id.leavingDate);
        leavingDate.setInputType(InputType.TYPE_NULL);
        //leavingDate.requestFocus();
        leavingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar leaveCalendar = Calendar.getInstance();
                leaveDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        leavingDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, leaveCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                getActivity();
            }
        });
        hirDate = (EditText) rootView.findViewById(R.id.hirDate);
        hirDate.setInputType(InputType.TYPE_NULL);
        //hirDate.requestFocus();
        hirDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar hireCalendar = Calendar.getInstance();
                hireDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        hirDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, hireCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                getActivity();
            }


        });
        ss = (EditText) rootView.findViewById(R.id.ssn);
        ss.setInputType(InputType.TYPE_NULL);
        employeetype = (Spinner) rootView.findViewById(R.id.positionspinner);
        activ = (Spinner) rootView.findViewById(R.id.activespinner);
        userid = (EditText) rootView.findViewById(R.id.userid);
        userid.setInputType(InputType.TYPE_NULL);
        password = (EditText) rootView.findViewById(R.id.registerPassword);
        password.setInputType(InputType.TYPE_NULL);
        Button register = (Button) rootView.findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
                Log.d("RegisterUser", "Test");
                new CreateUserTask().execute();
                Log.d("createtask", "craetetask finished");
                //new LoginDetailsTask().execute();
                // Log.d("LoginDetails", "Login details finished");

            }
        });
        return rootView;
    }
    public void onClick(View view) {
        if (view == txtDate) {
            datePickerDialog.show();
        } else if (view == hirDate) {
            hireDialog.show();
        } else if (view == leavingDate) {
            leaveDialog.show();
        }
    }

        // final ProgressDialog prgDialog = new ProgressDialog(getActivity());
        //egister();


        //employeetype.setInputType(InputType.TYPE_NULL);



   /* public void egister() {
          txtD = txtDate.getText().toString();
         lDate = leavingDate.getText().toString();
         hDate = hirDate.getText().toString();
         sFName = fname.getText().toString();
         slname = lname.getText().toString();
         semail = email.getText().toString();
         scity = city.getText().toString();
         sphoneno = phoneno.getText().toString();
         szip = zip.getText().toString();
         sssn = ssn.getText().toString();
         spinner = employeetype.getSelectedItem().toString();
         spinner2 = active.getSelectedItem().toString();
         saddress = address.getText().toString();
         suserid = userid.getText().toString();
         spassword = password.getText().toString();
         prgDialog.show();
         new RegisterUser().execute(slname, sFName, semail, sphoneno, saddress, scity, szip, txtD, hDate, lDate, sssn, spinner2, spinner);
         prgDialog.hide();
     }

     class RegisterUser extends AsyncTask<String, Void, String> {
         String tag = "RegisterUser";

         @Override
         protected String doInBackground(String... args) {
             String error_msg = "empty";
             try {
                 String data = URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(args[0], "UTF-8");
                 data += "&" + URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(args[1], "UTF-8");
                 data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(args[2], "UTF-8");
                 data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(args[3], "UTF-8");
                 data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(args[4], "UTF-8");
                 data += "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(args[5], "UTF-8");
                 data += "&" + URLEncoder.encode("zip", "UTF-8") + "=" + URLEncoder.encode(args[6], "UTF-8");
                 data += "&" + URLEncoder.encode("birthdate", "UTF-8") + "=" + URLEncoder.encode(args[7], "UTF-8");
                 data += "&" + URLEncoder.encode("hiredate", "UTF-8") + "=" + URLEncoder.encode(args[8], "UTF-8");
                 data += "&" + URLEncoder.encode("leavedate", "UTF-8") + "=" + URLEncoder.encode(args[9], "UTF-8");
                 data += "&" + URLEncoder.encode("ssn", "UTF-8") + "=" + URLEncoder.encode(args[10], "UTF-8");
                 data += "&" + URLEncoder.encode("active", "UTF-8") + "=" + URLEncoder.encode(args[11], "UTF-8");
                 data += "&" + URLEncoder.encode("position", "UTF-8") + "=" + URLEncoder.encode(args[12], "UTF-8");
                 URL url = new URL("http://192.168.2.129/createUser.php");
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                 conn.setDoInput(true);
                 conn.setDoOutput(true);

                 OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                 wr.write(data);
                 wr.flush();


                 BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                 if ((error_msg = reader.readLine()) == null) {
                     Log.d(tag, "Reader null");
                 } else
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
             Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
         }
     }*/
    public class CreateUserTask extends AsyncTask<Void, Void, Void> {
        /* last_name="", first_name="",  email="",phone="",
         address="", city="", zip="",state,birthdate="",
         hiredate="", leavedate="",ssn="";*/
        @Override
        protected Void doInBackground(Void... args) {
            // Get Last_name Edit View Value
            String last_name = lname.getText().toString();
            // Get First_name Edit View Value
            String first_name = fname.getText().toString();
            String email = e_mail.getText().toString();
            String phone = phoneno.getText().toString();
            String address = addres.getText().toString();
            String city = citi.getText().toString();
            String zip = zipp.getText().toString();
            String state = stat.getText().toString();
            String birthdate = txtDate.getText().toString();
            String leavedate = leavingDate.getText().toString();
            String hiredate = hirDate.getText().toString();
            String ssn = ss.getText().toString();
            String position = employeetype.getSelectedItem().toString();
            String active = activ.getSelectedItem().toString();
            String User = userid.getText().toString();
            String Password = password.getText().toString();


            String tag = "CreateUserTask";
            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.AndroidToServer.RegisterEmployee registerEmployee = new Message.AndroidToServer.RegisterEmployee(last_name, first_name, email, phone, address,
                        city, zip, state, birthdate, hiredate, leavedate, ssn, position, active,User,Password);

                Log.d("Register", "last_name:" + registerEmployee.getLastName() + "first_name" + registerEmployee.getFirstName());
                System.err.println("last_name:" + registerEmployee.getLastName() + "first_name" + registerEmployee.getFirstName());


                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                Log.d(tag, "Created Socket");
                System.err.println("Created Socket");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Log.d(tag, "Created output");
                System.err.println("Created output");

                Log.d("Register", "last_name:" + registerEmployee.getLastName() + "first_name" + registerEmployee.getFirstName());
                out.writeObject(registerEmployee);

                Log.d(tag, "Wrote Output");
                System.err.println("Wrote output");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d("OnpostExecute", "Create user task finished");
        }

    }


    /*public class LoginDetailsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            // Get Last_name Edit View Value
            String User = userid.getText().toString();
            String Password = password.getText().toString();
            String Position = employeetype.getSelectedItem().toString();



            String tag = "LoginDetailsTask";
            try {
                InetAddress serverAddr = InetAddress.getByName(Message.SERVERIP);
                Message.LoginDetailsTask loginDetailsTask = new Message.LoginDetailsTask( User,Password,Position);

                Log.d("Login", "user" + loginDetailsTask.getUser() + "password" +loginDetailsTask.getPassword());
                System.err.println("user:" + loginDetailsTask.getUser() + "password" + loginDetailsTask.getPassword());


                Socket socket = new Socket(serverAddr, Message.SERVERPORT);

                Log.d(tag, "Created Socket");
                System.err.println("Created Socket");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Log.d(tag, "Created output");
                System.err.println("Created output");

                Log.d("Register", "usr:" + loginDetailsTask.getUser() + "password" + loginDetailsTask.getPassword());
                out.writeObject(loginDetailsTask);

                Log.d(tag, "Wrote Output");
                System.err.println("Wrote output");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d("OnpostExecute", "LoginDetails task finished");
        }

    }*/

}


