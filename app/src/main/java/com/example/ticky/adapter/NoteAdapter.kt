package com.example.ticky.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.example.ticky.R
import com.example.ticky.model.NoteModel
import com.example.ticky.utils.DialogBox

class NoteAdapter(

    private val context: Context,
    private val dataSet: List<NoteModel>

) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textDescription: TextView = view.findViewById(R.id.text_description)
        val textDeadline: TextView = view.findViewById(R.id.textView2)
        val textPriority: TextView = view.findViewById(R.id.textView3)
        val btnEdit: MaterialButton = view.findViewById(R.id.btn_edit)
        val btnDelete: MaterialButton = view.findViewById(R.id.btn_delete)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_recycler_single_item, parent, false)

        return NoteViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val dialog = DialogBox()
        val item = dataSet[position]

        holder.textTitle.text = item.title
        holder.textDescription.text = item.description
        holder.textDeadline.text = item.deadline
        holder.textPriority.text = item.priority

        holder.btnEdit.setOnClickListener {
            dialog.editDialog(context, item)
        }

        holder.btnDelete.setOnClickListener {
            dialog.deleteDialog(context, item)
        }
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }


}