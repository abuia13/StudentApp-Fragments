package com.studentapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.studentapp.model.Student

class StudentAdapter(
    private val students: List<Student>,
    private val onItemClick: (Int) -> Unit,
    private val onCheckClick: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.student_name)
        val id: TextView = itemView.findViewById(R.id.student_id)
        val image: ImageView = itemView.findViewById(R.id.student_image)
        val checkBox: CheckBox = itemView.findViewById(R.id.student_check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.name.text = student.name
        holder.id.text = student.id
        holder.checkBox.isChecked = student.isChecked

        // Display student image or default icon
        if (!student.imageUri.isNullOrEmpty()) {
            holder.image.setImageURI(Uri.parse(student.imageUri))
            holder.image.background = null
        } else {
            holder.image.setImageResource(R.drawable.ic_person)
            holder.image.setBackgroundResource(R.drawable.circle_background)
        }

        holder.checkBox.setOnClickListener {
            onCheckClick(position)
        }

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

    }

    override fun getItemCount(): Int = students.size
}
