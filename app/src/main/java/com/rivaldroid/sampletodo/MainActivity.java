package com.rivaldroid.sampletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.rivaldroid.sampletodo.model.TodoItem;

import java.util.List;

/**
 * MainActivity is the entry point for the application
 */
public class MainActivity extends AppCompatActivity {
    //stores all the to do items
    List<TodoItem> items;
    // Adapter used with list view
    TodoCursorAdapter itemsAdapter;
    //ListView to display the list of all the elements from to do list and bound with ListView defined in XML layout
    ListView lvItems;
    //REQUEST_CODE is used to verify that onActivityResult is for the startActivityForResult that we have started.
    private final int REQUEST_CODE = 100;
    private EditText etNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bind ListView defined in XML
        lvItems = (ListView) findViewById(R.id.lvItems);
         etNewItem = (EditText) findViewById(R.id.etNewItem);
        //Read items from todo.txt if there are any
        //readItems();
        readTodoItems();
        //Create new instance of the ArrayAdater to bind with ListView and display elements
        itemsAdapter = new TodoCursorAdapter(this, items);
        //Set adapter to ListView
        lvItems.setAdapter(itemsAdapter);
        //Setup listeners on the ListView to handle events like setOnItemLongClickListener and setOnItemClickListener
        setupListViewListener();
    }

    /**
     * Listeners for ListView items are set here
     * OnItemLongClickListener is to delete the item from the list.
     * OnItemClickListener is to update the to do item in the list.
     */
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id){
                removeTodoItem(items.get(pos), pos);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long id){
                TodoItem editTodoItem = new TodoItem(items.get(position).getTodoItemData(), position);
                Intent editItemActivityIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editItemActivityIntent.putExtra("TodoItem",editTodoItem);
                etNewItem.setError(null);
                startActivityForResult(editItemActivityIntent, REQUEST_CODE);
            }
        });
    }

    /**
     * Handle activity result once the EditItemActivity returns the updated results.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode , Intent data){
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            TodoItem updatedTodoItem = (TodoItem) data.getExtras().getSerializable("TodoItem");
            items.set(updatedTodoItem.getPosition(), updatedTodoItem);
            itemsAdapter.notifyDataSetChanged();
            updateTodoItem(updatedTodoItem);
        }
    }

    private void updateTodoItem(TodoItem updatedTodoItem) {
        new Update(TodoItem.class)
                .set("TodoItemData = '"+updatedTodoItem.getTodoItemData()+"'")
                .where("Position = ?", updatedTodoItem.getPosition())
                .execute();
    }

    /**
     * Handle Add new item action on button
     * To do item can not be empty
     * @param view
     */
    public void onAddItem(View view) {

        String itemText = etNewItem.getText().toString();
        if(!itemText.trim().isEmpty()) {
            TodoItem todoItem = new TodoItem(itemText.trim(), items.size());
            itemsAdapter.add(todoItem);
            writeItem(todoItem);
            etNewItem.setText("");
        }else{
            etNewItem.setError(getResources().getString(R.string.todo_item_empty));
        }
    }

    /**
     * Read all the previously purged records
     */
    private void readTodoItems(){
        items = new Select().from(TodoItem.class).orderBy("Position ASC").execute();
    }

    /**
     * Save the given item to Todo.db
     * @param todoItem
     */
    private void writeItem(TodoItem todoItem){
        todoItem.save();
    }

    /**
     * Remove given item from Todo.db and from items ArrayList
     * @param todoItem
     * @param pos
     */
    private void removeTodoItem(TodoItem todoItem, int pos){
        if(null != todoItem ) {
            todoItem.delete();
            items.remove(pos);
            itemsAdapter.notifyDataSetChanged();
        }
    }


}
