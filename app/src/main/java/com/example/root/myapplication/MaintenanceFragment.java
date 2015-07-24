package com.example.root.myapplication;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.econolodge.econolodgeapp3.Message.*;
import static com.econolodge.econolodgeapp3.Message.SERVERIP;
import static com.econolodge.econolodgeapp3.Message.SERVERPORT;


/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceFragment extends Fragment {


    public MaintenanceFragment() {
        // Required empty public constructor
    }

    String listStrings[];
    ListView listview;
    View globalview;
    protected static final int CONTEXT_MENU_OPTION1 = 1;
    protected static final int CONTEXT_MENU_OPTION2 = 2;
    List<MRowItem> mRowItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maintenance, container, false);
        globalview= rootView;
        // Inflate the layout for this fragment
        listview = (ListView) rootView.findViewById(R.id.listviewMaintainence);

        new GetTask().execute();



        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(mRowItems.get(info.position).getTitle() + ":Select Option");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION1, 0, "images");
        menu.add(Menu.NONE, CONTEXT_MENU_OPTION2, 1, "work status");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView available = (TextView) listview.getChildAt(info.position).findViewById(R.id.Available);
        switch (item.getItemId()) {
            case CONTEXT_MENU_OPTION1:
                listview.getChildAt(info.position).setBackgroundResource(R.color.Green);
                Intent intent = new Intent(getActivity(), MPhotoActivity2.class);
                startActivity(intent);
                return true;
            case CONTEXT_MENU_OPTION2:
                listview.getChildAt(info.position).setBackgroundResource(R.color.red);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    public class MRowItem {
        private String title;
        private String description;
        private String time;
        private String id;
        public MRowItem (String title, String description, String time, String id )
        {   this.title=title;
            this.description=description;
            this.time=time;
           this.id = id;
        }



        public String getTitle() {
            return title;
        }
        public void setTitle(String title){
            this.title=title;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description){
            this.description=description;
        }
        public String getTime() {
           return time;
        }
        public void setTime(String time){
            this.time=time;
        }
        public String getId() {
        return id;
       }

        @Override
        public String toString(){
            return title+ "/n"+description+ "/n"+time;
        }
    }

   public class GetTask extends AsyncTask<AndroidToServer.dataToBeRequested,Void,ServerToAndroid.returnTaskData> {

        @Override
        protected ServerToAndroid.returnTaskData doInBackground(AndroidToServer.dataToBeRequested... params) {
           // boolean mRun = true;
            Object inMessage;
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                Socket socket = new Socket(serverAddr, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                AndroidToServer AS = new AndroidToServer();
                AS.setRequest(AndroidToServer.dataToBeRequested.TASKDATA);
                System.out.println(AS);
                out.writeObject(AS);
                inMessage = in.readObject();
                return (ServerToAndroid.returnTaskData)inMessage;
           } catch (Exception e){
              e.printStackTrace();
            return  new ServerToAndroid.returnTaskData();
           }

        }
        protected void onPostExecute(ServerToAndroid.returnTaskData params) {
            ServerToAndroid.TaskDataClass tdc = params.getNextTask();
            String title;
           String id;
            String description;
            MRowItem item;
            mRowItems = new ArrayList<MRowItem>();
            System.out.println("ENTERED OnPostExecute");
            while(tdc!=null) {
                title = new String(tdc.getTitle().toString());
                description = new String(tdc.getDescription().toString());
                id = new String(String.valueOf(tdc.GetTaskID()));
                System.out.println(title+"   "+ description);
                item  = new MRowItem(title,description,title,id);
                System.out.println("TITLE=" + title + "DESCRIPTION=" +description );
                mRowItems.add(item);
                tdc=params.getNextTask();
            }
            listview = (ListView) globalview.findViewById(R.id.listviewMaintainence);
            MListViewAdapter adapter = new MListViewAdapter(getActivity(),
                    R.layout.fragment_maintenance, mRowItems);
            listview.setAdapter(adapter);
            registerForContextMenu(listview);
            }
        }

        private class MListViewAdapter extends ArrayAdapter<MRowItem> {
        Context context;
        public MListViewAdapter(Context context, int fragment_maintenance, List<MRowItem> mRowItems) {
            super(getActivity().getApplicationContext(), fragment_maintenance, mRowItems);
            this.context= context;
            notifyDataSetChanged();
        }

        private class ViewHolder {
            TextView txtTitle;
            TextView txtDescription;
            TextView txtTime;
            TextView taskno;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            MRowItem rowItem = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.maintainencelistitems, null);
                holder= new ViewHolder();
                holder.taskno=(TextView) convertView.findViewById(R.id.TaskNo);
                holder.txtTitle=(TextView) convertView.findViewById(R.id.Task);
                holder.txtDescription=(TextView) convertView.findViewById(R.id.Description);
                holder.txtTime=(TextView) convertView.findViewById(R.id.Time);
                convertView.setTag(holder);
            }else
                holder =(ViewHolder) convertView.getTag();
            holder.taskno.setText(rowItem.getId());
            holder.txtTitle.setText(rowItem.getTitle());
            holder.txtDescription.setText(rowItem.getDescription());
            holder.txtTime.setText(rowItem.getTime());
            return  convertView;
        }
    }
}







