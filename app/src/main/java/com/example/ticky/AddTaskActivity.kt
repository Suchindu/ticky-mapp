package com.example.ticky

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.example.ticky.db.DBOpenHelper
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.RadioButton
import android.widget.RadioGroup
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {


    private lateinit var etTitle: TextInputLayout
    private lateinit var etDescription: TextInputLayout
    private lateinit var fabSend: FloatingActionButton
    private val dbOpenHelper = DBOpenHelper(this)
    private lateinit var etDeadline: TextInputLayout
    private lateinit var radioGroup: RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        fabSend = findViewById(R.id.fab_send)
        etDeadline = findViewById(R.id.et_deadline)
        radioGroup = findViewById(R.id.radioGroup_priority)

        etDeadline.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    val deadline = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.time)
                    etDeadline.editText?.setText(deadline)
                }

                TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
            }

            DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        fabSend.setOnClickListener {
            fabSendData()
        }

    }

    private fun fabSendData() {

        if (etTitle.editText?.text.toString().isEmpty()) {
            etTitle.error = "Please enter your Title"
            etTitle.requestFocus()
            return
        }

        if (etDescription.editText?.text.toString().isEmpty()) {
            etDescription.error = "Please enter your Description"
            etDescription.requestFocus()
            return
        }

        if (etDeadline.editText?.text.toString().isEmpty()) {
            etDeadline.error = "Please enter your Deadline Date"
            etDeadline.requestFocus()
            return
        }

        val selectedPriorityId = radioGroup.checkedRadioButtonId
        val selectedPriority = findViewById<RadioButton>(selectedPriorityId).text.toString()

        if (notEmpty()) {
            dbOpenHelper.addNote(
                etTitle.editText?.text.toString(),
                etDescription.editText?.text.toString(),
                etDeadline.editText?.text.toString(),
                selectedPriority
            )
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }

    }


    private fun notEmpty(): Boolean {
        val selectedPriorityId = radioGroup.checkedRadioButtonId
        return (etTitle.editText?.text.toString().isNotEmpty()
                && etDescription.editText?.text.toString().isNotEmpty()
                && etDeadline.editText?.text.toString().isNotEmpty()
                && selectedPriorityId != -1)

    }

}