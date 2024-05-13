package com.example.ticky

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.example.ticky.db.DBOpenHelper
import com.example.ticky.utils.COLUMN_DEADLINE
import com.example.ticky.utils.COLUMN_NAME_DESCRIPTION
import com.example.ticky.utils.COLUMN_NAME_TITLE
import com.example.ticky.utils.COLUMN_PRIORITY
import java.util.Calendar

class UpdateTaskActivity : AppCompatActivity() {


    private lateinit var etUpdatedTitle: TextInputLayout
    private lateinit var etUpdatedDescription: TextInputLayout
    private lateinit var etUpdatedDeadline: TextInputLayout
    private lateinit var fabUpdate: FloatingActionButton
    private lateinit var radioGroup: RadioGroup
    private val dbOpenHelper = DBOpenHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        etUpdatedTitle = findViewById(R.id.et_updated_title)
        etUpdatedDescription = findViewById(R.id.et_updated_description)
        etUpdatedDeadline = findViewById(R.id.et_updated_deadline)
        fabUpdate = findViewById(R.id.fab_update)
        radioGroup = findViewById(R.id.radioGroup_updated_priority)

        val titleOld = intent.getStringExtra(COLUMN_NAME_TITLE)
        val descriptionOld = intent.getStringExtra(COLUMN_NAME_DESCRIPTION)
        val deadlineOld = intent.getStringExtra(COLUMN_DEADLINE)
        val priorityOld = intent.getStringExtra(COLUMN_PRIORITY)

        if (!titleOld.isNullOrBlank()) {
            etUpdatedTitle.editText?.text = Editable.Factory.getInstance().newEditable(titleOld)
        }

        if (!descriptionOld.isNullOrBlank()) {
            etUpdatedDescription.editText?.text = Editable.Factory.getInstance().newEditable(descriptionOld)
        }

        if (!deadlineOld.isNullOrBlank()) {
            etUpdatedDeadline.editText?.text = Editable.Factory.getInstance().newEditable(deadlineOld)
            Log.d("UpdateNoteActivity", "Deadline: $deadlineOld")
        }

//
//
        etUpdatedDeadline.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    val deadline = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.time)
                    etUpdatedDeadline.editText?.setText(deadline)
                }

                TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                    Calendar.MINUTE), true).show()
            }

            DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        fabUpdate.setOnClickListener {
            updateData()
        }

    }

    private fun updateData() {

        val id = intent.getIntExtra(BaseColumns._ID, 0).toString()

        if (etUpdatedTitle.editText?.text.toString().isEmpty()) {
            etUpdatedTitle.error = "Please enter your Title"
            etUpdatedTitle.requestFocus()
            return
        }

        if (etUpdatedDescription.editText?.text.toString().isEmpty()) {
            etUpdatedDescription.error = "Please enter your Description"
            etUpdatedDescription.requestFocus()
            return
        }

        if (etUpdatedDeadline.editText?.text.toString().isEmpty()) {
            etUpdatedDeadline.error = "Please enter your Deadline Date"
            etUpdatedDeadline.requestFocus()
            return
        }
        val selectedPriorityId = radioGroup.checkedRadioButtonId
        if (selectedPriorityId == -1) {
            Toast.makeText(this, "Please select a priority", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedPriority = findViewById<RadioButton>(selectedPriorityId).text.toString()

        if (notEmpty()) {

            dbOpenHelper.updateNote(
                id,
                etUpdatedTitle.editText?.text.toString(),
                etUpdatedDescription.editText?.text.toString(),
                etUpdatedDeadline.editText?.text.toString(),
                selectedPriority
            )
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }

    }

    private fun notEmpty(): Boolean {
        val selectedPriorityId = radioGroup.checkedRadioButtonId
        return (etUpdatedTitle.editText?.text.toString().isNotEmpty()
                && etUpdatedDescription.editText?.text.toString().isNotEmpty()
                && etUpdatedDeadline.editText?.text.toString().isNotEmpty()
                && selectedPriorityId != -1)
    }

}