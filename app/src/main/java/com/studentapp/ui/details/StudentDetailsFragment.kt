package com.studentapp.ui.details

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.studentapp.R
import com.studentapp.databinding.FragmentStudentDetailsBinding
import com.studentapp.model.Student
import com.studentapp.model.StudentRepository

class StudentDetailsFragment : Fragment() {

    private var _binding: FragmentStudentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: StudentDetailsFragmentArgs by navArgs()
    private lateinit var student: Student

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        student = args.student
        bindStudent(student)

        // קבלה של אות עדכון מעריכה
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("studentUpdatedId")
            ?.observe(viewLifecycleOwner) { id ->
                StudentRepository.getStudentById(id)?.let { updated ->
                    student = updated
                    bindStudent(student)
                }
            }

        // תפריט Action Bar
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_student_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        val action = StudentDetailsFragmentDirections
                            .actionStudentDetailsToEditStudent(student)
                        findNavController().navigate(action)
                        true
                    }
                    R.id.action_delete -> {
                        confirmDelete()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        StudentRepository.getStudentById(student.id)?.let {
            student = it
            bindStudent(student)
        }
    }

    private fun bindStudent(s: Student) {
        binding.nameEditText.setText(s.name)
        binding.idEditText.setText(s.id)
        binding.phoneEditText.setText(s.phone)
        binding.addressEditText.setText(s.address)
        binding.birthDateEditText.setText(s.birthDate ?: "")
        binding.birthTimeEditText.setText(s.birthTime ?: "")
        binding.isCheckedCheckbox.isChecked = s.isChecked

        if (!s.imageUri.isNullOrEmpty()) {
            binding.studentImageView.setImageURI(Uri.parse(s.imageUri))
            binding.studentImageView.background = null
        } else {
            binding.studentImageView.setImageResource(R.drawable.ic_person)
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.name}?")
            .setPositiveButton("Delete") { _, _ ->
                StudentRepository.removeStudent(student)
                Toast.makeText(requireContext(), "Student deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
