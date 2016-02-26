package io.monteirodev.doodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by developTrade on 26-02-2016.
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<String> allContacts;
    private Context context;
    private LayoutInflater inflater;
    public ListViewAdapter(Context context, ArrayList<String> allContacts) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.allContacts = allContacts;
    }
    @Override
    public int getCount() {
        return allContacts.size();
    }
    @Override
    public Object getItem(int position) {
        return allContacts.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ContactHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.contact_inflater, parent, false);
            holder = new ContactHolder();
            assert view != null;
            holder.tvContactName = (TextView) view.findViewById(R.id.tvContactListName);
            holder.tvNumber = (TextView) view.findViewById(R.id.tvContactListNumber);
            view.setTag(holder);
        } else {
            holder = (ContactHolder) view.getTag();
        }
        holder.tvContactName.setText(allContacts.get(position));
        holder.tvNumber.setText((position+1)+"");
        return view;
    }
}
class ContactHolder {
    TextView tvContactName;
    TextView tvNumber;
}
