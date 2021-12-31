package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()

        //1. Lets detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            //Code in here is going to be executed when the user clicks the button
//            Log.i("Caren", "User clicked on button")
//        }

        //Look up recycle view in the layout!
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //Attach adapter to recycler view
        recyclerView.adapter = adapter
        //set layout manager so it can set up itself
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the the button and input field so that the User can enter a task and put it on the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Get a reference to the button
        //and then set an onclick listener to it
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text the user inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Reset the text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save data that the user has written/inputted
    //By writing and reading from a file

    //Create a method to get the data file we need
    //Get file we need
    fun getDataFile(): File {

        //Every line is going to represent a specific task in our list of tasks!
        return File(filesDir,"data.txt")
    }

    //Load the items by reading every line in our file
    fun loadItems(){
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //Save the items (Writing) into our data
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}