package com.studentapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.studentapp.Model.StudentRepository

class StudentsRecyclerViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_recycler_view)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.students_recycler_view)
        val studentsList = StudentRepository.getAllStudents()

        studentAdapter = StudentAdapter(
            studentsList,
            onItemClick = { position ->
                val student = studentsList[position]
                val intent = Intent(this, StudentDetailsActivity::class.java)
                intent.putExtra("student", student) // ✅ שליחת האובייקט כולו
                startActivity(intent)
            },
            onCheckClick = { position ->
                val student = studentsList[position]
                student.isChecked = !student.isChecked
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add_student)
        fab.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        studentAdapter.notifyDataSetChanged()
    }
}
