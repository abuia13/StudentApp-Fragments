package com.studentapp.Model

import java.io.Serializable
data class Student(
    var name: String,
    var id: String,
    var phone: String,
    var address: String,
    var isChecked: Boolean = false,
    var imageUri: String? = null
) : Serializable
