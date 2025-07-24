package com.studentapp.ui.addstudent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.studentapp.model.Student
import com.studentapp.model.StudentRepository
import com.studentapp.databinding.FragmentAddStudentBinding

class AddStudentFragment : Fragment() {

    private var _binding: FragmentAddStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val id = binding.idEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val address = binding.addressEditText.text.toString()
            val isChecked = binding.isCheckedCheckbox.isChecked

            if (name.isBlank() || id.isBlank()) {
                Toast.makeText(requireContext(), "Name and ID are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(
                name = name,
                id = id,
                phone = phone,
                address = address,
                isChecked = isChecked
            )

            StudentRepository.addStudent(student)

            Toast.makeText(requireContext(), "Student saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
