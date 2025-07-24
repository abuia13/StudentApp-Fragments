package com.studentapp

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.studentapp.Model.Student
import com.studentapp.Model.StudentRepository

class AddStudentActivity : AppCompatActivity() {

    //  משתנים גלובליים
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    // מאפשר בחירת תמונה מהגלריה
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        //  Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add New Student"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //  קישור לרכיבים
        imageView = findViewById(R.id.student_image)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val idEditText = findViewById<EditText>(R.id.idEditText)
        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        val addressEditText = findViewById<EditText>(R.id.addressEditText)
        val checkBox = findViewById<CheckBox>(R.id.checkedCheckBox)
        val addButton = findViewById<Button>(R.id.addStudentButton)

        // בחירת תמונה
        imageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // הוספת סטודנט
        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val id = idEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val address = addressEditText.text.toString()
            val isChecked = checkBox.isChecked

            if (name.isNotBlank() && id.isNotBlank()) {
                val newStudent = Student(
                    name = name,
                    id = id,
                    phone = phone,
                    address = address,
                    isChecked = isChecked,
                    imageUri = selectedImageUri?.toString()
                )
                StudentRepository.addStudent(newStudent)
                Toast.makeText(this, "Student added!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill in name and ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
