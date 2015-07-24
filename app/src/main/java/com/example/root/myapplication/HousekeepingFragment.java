package com.example.root.myapplication;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HousekeepingFragment extends Fragment {


    public HousekeepingFragment() {
        // Required empty public constructor
    }

    ListView listView;
    List<HkRowItem> HkrowItems;
    public View globalView;
    public static final String[] room = new String[]{};
    public static final String[] lines = new String[]{};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_housekeeping, container, false);
        globalView = rootView;
        HkrowItems = new ArrayList<HkRowItem>();
        for (int i = 0; i < room.length; i++) {

            HkRowItem item = new HkRowItem(room[i], lines[i]);
            HkrowItems.add(item);
        }

        // Inflate the layout for this fragment
        listView = (ListView) globalView.findViewById(R.id.hklistview);
        hkListViewAdapter adapter = new hkListViewAdapter(getActivity(),
                R.layout.fragment_housekeeping, HkrowItems);
        listView.setAdapter(adapter);
        return rootView;
    }


    public class HkRowItem {
        private String room;
        private String avail;

        public HkRowItem(String room, String avail) {
            this.room = room;
            this.avail = avail;
        }

        public String getroom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getavail() {
            return avail;
        }

        public void setAvail(String avail) {
            this.avail = avail;
        }

        @Override
        public String toString() {
            return room + "/n" + avail;
        }
    }

    private class hkListViewAdapter extends ArrayAdapter<HkRowItem> {
        Context context;

        public hkListViewAdapter(Context context, int resourceId, List<HkRowItem> items) {
            super(context, resourceId, items);
            this.context = context;
            notifyDataSetChanged();
        }

        private class ViewHolder {
            TextView txtRoomNo;
            TextView txtAvailable;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            HkRowItem rowItem = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.hklist_item, null);
                holder = new ViewHolder();
                holder.txtRoomNo = (TextView) convertView.findViewById(R.id.hkRoomNo);
                holder.txtAvailable = (TextView) convertView.findViewById(R.id.hkAvailable);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.txtRoomNo.setText(rowItem.getroom());
            holder.txtAvailable.setText(rowItem.getavail());
            return convertView;
        }
    }
}



