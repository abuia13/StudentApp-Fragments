package com.studentapp.model

object StudentRepository {
    private val studentsList: MutableList<Student> = mutableListOf(
        Student("Alice", "123456789", "050-1234567", "Tel Aviv"),
        Student("Bob", "987654321", "052-7654321", "Haifa"),
        Student("Charlie", "112233445", "053-3334444", "Jerusalem")
    )

    fun getAllStudents(): List<Student> {
        return studentsList
    }

    fun addStudent(student: Student) {
        studentsList.add(student)
    }

    fun removeStudent(student: Student) {
        studentsList.remove(student)
    }

    fun updateStudent(index: Int, student: Student) {
        studentsList[index] = student
    }

    fun getStudent(index: Int): Student {
        return studentsList[index]
    }

    fun getStudentIndexById(id: String): Int? {
        return studentsList.indexOfFirst { it.id == id }.takeIf { it >= 0 }
    }
}