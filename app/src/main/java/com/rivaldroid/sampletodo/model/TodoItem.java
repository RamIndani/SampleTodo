package com.rivaldroid.sampletodo.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by ramnivasindani on 6/21/16.
 * Holds the Model for each Todo item
 */
@Table(name="Todo")
public class TodoItem extends Model implements Serializable{
    @Column(name = "TodoItemData")
    private String todoItemData;

    @Column(name="Position", unique=true)
    private int position;

    public TodoItem(){
        super();
    }
    public TodoItem(final String todoItemData, final int position){
        super();
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
