package com.studentapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
data class Student(
    var name: String,
    var id: String,
    var phone: String,
    var address: String,
    var isChecked: Boolean = false,
    var imageUri: String? = null,
    var birthDate: String? = null,  // dd/MM/yyyy
    var birthTime: String? = null   // HH:mm
) : Serializable, Parcelable
