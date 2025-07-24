package com.studentapp

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.studentapp.model.Student
import com.studentapp.model.StudentRepository

class EditStudentActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var updateButton: Button
    private var studentIndex: Int = -1
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Edit Student"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // Find views
        imageView = findViewById(R.id.studentImageView)
        nameEditText = findViewById(R.id.editNameEditText)
        idEditText = findViewById(R.id.editIdEditText)
        phoneEditText = findViewById(R.id.editPhoneEditText)
        addressEditText = findViewById(R.id.editAddressEditText)
        checkBox = findViewById(R.id.editCheckBox)
        updateButton = findViewById(R.id.updateStudentButton)
        val changeImageButton = findViewById<Button>(R.id.changeImageButton)

        // קבלת ID מהאינטנט
        val studentId = intent.getStringExtra("student_id")
        studentIndex = studentId?.let { StudentRepository.getStudentIndexById(it) } ?: -1

        if (studentIndex != -1) {
            val student = StudentRepository.getStudent(studentIndex)
            nameEditText.setText(student.name)
            idEditText.setText(student.id)
            phoneEditText.setText(student.phone)
            addressEditText.setText(student.address)
            checkBox.isChecked = student.isChecked

            // ✔ טעינת תמונה קיימת או ברירת מחדל
            student.imageUri?.let { uriString ->
                val uri = Uri.parse(uriString)
                imageView.setImageURI(uri)
                selectedImageUri = uri
            } ?: run {
                imageView.setImageResource(R.drawable.ic_person)
            }
        }

        // שינוי תמונה
        changeImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // עדכון סטודנט
        updateButton.setOnClickListener {
            if (studentIndex != -1) {
                val updatedStudent = Student(
                    name = nameEditText.text.toString(),
                    id = idEditText.text.toString(),
                    phone = phoneEditText.text.toString(),
                    address = addressEditText.text.toString(),
                    isChecked = checkBox.isChecked,
                    imageUri = selectedImageUri?.toString()

                )
                StudentRepository.updateStudent(studentIndex, updatedStudent)
                Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
