package com.rivaldroid.sampletodo.model;

import java.io.Serializable;

/**
 * Created by ramnivasindani on 6/21/16.
 */
public class TodoItem implements Serializable{
    private String todoItemData;
    private int position;

    public TodoItem(final String todoItemData, final int position){
        this.todoItemData = todoItemData;
        this.position = position;
    }

    public String getTodoItemData(){
        return todoItemData;
    }

    public int getPosition(){
        return position;
    }

    public void setTodoItemData(final String updatedTodoItemData){
        this.todoItemData = updatedTodoItemData;
    }
}
