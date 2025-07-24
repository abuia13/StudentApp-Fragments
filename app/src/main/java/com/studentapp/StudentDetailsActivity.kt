package com.studentapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.studentapp.Model.Student
import com.studentapp.Model.StudentRepository

class StudentDetailsActivity : AppCompatActivity() {

    private var studentId: String? = null
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Student Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //  קבלת Student מלא או רק ID
        student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("student", Student::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("student") as? Student
        }

        studentId = student?.id ?: intent.getStringExtra("student_id")

        //  כפתור Edit
        val editButton = findViewById<Button>(R.id.btn_edit_student)
        editButton.setOnClickListener {
            studentId?.let {
                val intent = Intent(this, EditStudentActivity::class.java)
                intent.putExtra("student_id", it)
                startActivity(intent)
            }
        }
        //  כפתור Delete
        val deleteButton = findViewById<Button>(R.id.btn_delete_student)

        deleteButton.setOnClickListener {
            student?.let { st ->
                AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete ${st.name}?")
                    .setPositiveButton("Yes") { _, _ ->
                        StudentRepository.removeStudent(st)
                        Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // טען לפי ID מהמאגר
        val updatedStudent = studentId?.let { id ->
            StudentRepository.getStudentIndexById(id)?.let {
                StudentRepository.getStudent(it)
            }
        }

        // קישור לרכיבים
        val nameText = findViewById<TextView>(R.id.student_name)
        val idText = findViewById<TextView>(R.id.student_id)
        val phoneText = findViewById<TextView>(R.id.student_phone)
        val addressText = findViewById<TextView>(R.id.student_address)
        val checkBox = findViewById<CheckBox>(R.id.student_checkbox)
        val imageView = findViewById<ImageView>(R.id.student_image)


        updatedStudent?.let {

            nameText.text = "Name: ${it.name}"
            idText.text = "ID: ${it.id}"
            phoneText.text = "Phone: ${it.phone}"
            addressText.text = "Address: ${it.address}"
            checkBox.isChecked = it.isChecked

            // טעינת תמונה אם יש URI, אחרת ברירת מחדל

            if (!it.imageUri.isNullOrEmpty()) {
                imageView.setImageURI(android.net.Uri.parse(it.imageUri))
            } else {
                imageView.setImageResource(R.drawable.ic_person)
            }
        }
    }
}
