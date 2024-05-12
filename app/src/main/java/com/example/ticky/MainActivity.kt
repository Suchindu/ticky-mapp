package com.example.ticky

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.ticky.adapter.NoteAdapter
import com.example.ticky.db.DBOpenHelper
import com.example.ticky.model.NoteModel

class MainActivity : AppCompatActivity() {


    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var myDataset: MutableList<NoteModel>
    private val dbOpenHelper = DBOpenHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRecyclerView = findViewById(R.id.main_recycler_view)
        fabCreate = findViewById(R.id.fab_create)

        myDataset = dbOpenHelper.readNotes()

        mainRecyclerView.adapter = NoteAdapter(this, myDataset)
        mainRecyclerView.setHasFixedSize(true)


        fabCreate.setOnClickListener {
            val intentToAddTaskActivity = Intent(this, AddTaskActivity::class.java)
            startActivity(intentToAddTaskActivity)
            finish()
        }

    }
}