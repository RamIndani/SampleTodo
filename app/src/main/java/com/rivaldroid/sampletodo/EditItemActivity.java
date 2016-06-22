package com.rivaldroid.sampletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.rivaldroid.sampletodo.model.TodoItem;

/**
 * EditItemActivity is used to edit the to do list item
 */
public class EditItemActivity extends AppCompatActivity {

    //EditText to bind etEditItem from XML
    private EditText etEditItem;
    //TodoItem to hold the serialized object with position and data of the to do item
    private TodoItem editTodoItem;
    //InputMethodManager to handle the keyboard focus when activity is first shown to user.
    private InputMethodManager inputMethodManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //Get the TodoItem that is sent with the Intent to the present activity
        editTodoItem = (TodoItem) getIntent().getExtras().getSerializable("TodoItem");
        //Binding etEditItem with XML view.
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        //Set the data in the etEditItem
        etEditItem.setText(editTodoItem.getTodoItemData());
        //Set the cursor position at the end in EditText.
        etEditItem.setSelection(editTodoItem.getTodoItemData().length());
        //Set etEditItem focused.
        etEditItem.requestFocus();
        //Display keyboard when user starts the activity to edit to do item.
        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void saveUpdates(View view) {
        //Get the updated text - Updated Text can not be empty
        String updatedTodoItem = etEditItem.getText().toString();
        if(!updatedTodoItem.isEmpty()) {
            //Set updated to do data to serialized model
            editTodoItem.setTodoItemData(updatedTodoItem);
            //Create Intent to send back data to calling activity
            Intent updatedData = new Intent();
            //Attach data to intent
            updatedData.putExtra("TodoItem", editTodoItem);
            //Set the RESULT_OK to denote that update was successful
            setResult(RESULT_OK, updatedData);
            //Hide keyboard if still visible
            inputMethodManager.hideSoftInputFromWindow(etEditItem.getWindowToken(), 0);
            //Finish the activity
            finish();
        } else{
            Toast.makeText(this, getResources().getString(R.string.todo_item_empty), Toast.LENGTH_SHORT).show();
            etEditItem.setError(getResources().getString(R.string.todo_item_empty));
        }
    }
}
