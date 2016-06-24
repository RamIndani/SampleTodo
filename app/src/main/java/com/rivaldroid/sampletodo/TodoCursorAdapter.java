package com.rivaldroid.sampletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rivaldroid.sampletodo.model.TodoItem;

import java.util.List;

/**
 * Created by ramnivasindani on 6/23/16.
 */
public class TodoCursorAdapter extends ArrayAdapter<TodoItem> {

    public TodoCursorAdapter(Context context, List<TodoItem> todoItems){
        super(context, 0, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todoItem = getItem(position);
        if(null == convertView){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        tvBody.setText(todoItem.getTodoItemData());
        return convertView;
    }
}
