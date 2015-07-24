package com.example.root.myapplication;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.econolodge.econolodgeapp3.Message.SERVERIP;
import static com.econolodge.econolodgeapp3.Message.SERVERPORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {
    List<TaskRowItem> taskRowItems;
    ListView listview;
    View globalView;
    private String tTitle="";
    public TasksFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        globalView = rootView;
        new GetTask().execute();
        //new GetTaskInput();
        return rootView;
   }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tasks_action_bar, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("TaskFragment", "onOptionsItemSelected itemid:" + item.getItemId() + "inflaterId:" + R.id.action_add_task);
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d("TaskFragment", "case matched");
                Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
    public class TaskRowItem {
        private String title;
        public TaskRowItem (String title)
        {   this.title=title;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title){
            this.title=title;
        }
        @Override
        public String toString(){
            return title;
        }
    }
    public class GetTask extends AsyncTask<Message.AndroidToServer.dataToBeRequested,Void,Message.ServerToAndroid.returnTaskData> {
        @Override
        protected Message.ServerToAndroid.returnTaskData doInBackground(Message.AndroidToServer.dataToBeRequested... params) {
            // boolean mRun = true;
            Object inMessage;
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                Socket socket = new Socket(serverAddr, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message.AndroidToServer AS = new Message.AndroidToServer();
                AS.setRequest(Message.AndroidToServer.dataToBeRequested.TASKDATA);
                System.out.println(AS);
                out.writeObject(AS);
                inMessage = in.readObject();
                return (Message.ServerToAndroid.returnTaskData)inMessage;
            } catch (Exception e){
                e.printStackTrace();
                return  new Message.ServerToAndroid.returnTaskData();
            }
        }
        protected void onPostExecute(Message.ServerToAndroid.returnTaskData params) {
            Message.ServerToAndroid.TaskDataClass tdc = params.getNextTask();
            String title;
            TaskRowItem item;
            taskRowItems = new ArrayList<TaskRowItem>();
            System.out.println("ENTERED OnPostExecute");
            while(tdc!=null) {
                title = new String(tdc.getTitle().toString());
                item  = new TaskRowItem(title);
                taskRowItems.add(item);
                tdc=params.getNextTask();
            }
            listview = (ListView) globalView.findViewById(R.id.listview1);
            TaskListViewAdapter adapter = new TaskListViewAdapter(getActivity(),
                    R.layout.fragment_tasks, taskRowItems);
            listview.setAdapter(adapter);
            registerForContextMenu(listview);
        }
    }
    private class TaskListViewAdapter extends ArrayAdapter<TaskRowItem> {
        Context context;

        public TaskListViewAdapter(Context context, int fragment_tasks, List<TaskRowItem> taskRowItems) {
            super(getActivity().getApplicationContext(), fragment_tasks, taskRowItems);
            this.context= context;
            notifyDataSetChanged();
        }
        private class ViewHolder {
            TextView Title;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            TaskRowItem rowItem = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.tasklist_items, null);
                holder= new ViewHolder();
                holder.Title=(TextView) convertView.findViewById(R.id.Task);
                convertView.setTag(holder);
            }else
                holder =(ViewHolder) convertView.getTag();
            holder.Title.setText(rowItem.getTitle());
            return  convertView;
        }
    }
}



