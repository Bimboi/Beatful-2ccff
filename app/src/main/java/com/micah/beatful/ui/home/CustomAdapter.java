package com.micah.beatful.ui.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micah.beatful.R;

public class CustomAdapter extends BaseAdapter {
    private String[] items;
    private LayoutInflater layoutInflater;

    public CustomAdapter (String[] items, LayoutInflater layoutInflater) {
        this.items = items;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View myView = layoutInflater.inflate(R.layout.list_item, null);
        TextView txtSong = myView.findViewById(R.id.txtSong);
        txtSong.setSelected(true);
        txtSong.setText(items[i]);

        return myView;
    }
}
