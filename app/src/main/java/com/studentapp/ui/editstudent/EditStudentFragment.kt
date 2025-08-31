package com.studentapp.ui.editstudent

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.studentapp.R
import com.studentapp.databinding.FragmentEditStudentBinding
import com.studentapp.model.Student
import com.studentapp.model.StudentRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditStudentFragment : Fragment() {

    private var _binding: FragmentEditStudentBinding? = null
    private val binding get() = _binding!!

    private val args: EditStudentFragmentArgs by navArgs()
    private lateinit var student: Student

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        student = args.student

        // מילוי שדות קיימים
        binding.nameEditText.setText(student.name)
        binding.idEditText.setText(student.id)
        binding.phoneEditText.setText(student.phone)
        binding.addressEditText.setText(student.address)
        binding.isCheckedCheckbox.isChecked = student.isChecked
        binding.birthDateEditText.setText(student.birthDate ?: "")
        binding.birthTimeEditText.setText(student.birthTime ?: "")

        // Pickers לתאריך ושעה
        val cal = Calendar.getInstance()
        val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFmt = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding.birthDateEditText.setOnClickListener {
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(), { _, year, month, day ->
                cal.set(year, month, day)
                binding.birthDateEditText.setText(dateFmt.format(cal.time))
            }, y, m, d).show()
        }

        binding.birthTimeEditText.setOnClickListener {
            val h = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            TimePickerDialog(requireContext(), { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.birthTimeEditText.setText(timeFmt.format(cal.time))
            }, h, min, true).show()
        }

        // תפריט Action Bar
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_edit_student, menu)
            }
            override fun onMenuItemSelected(item: MenuItem): Boolean =
                when (item.itemId) {
                    R.id.action_save -> { saveStudentAndClose(); true }
                    else -> false
                }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveStudentAndClose() {
        val updated = Student(
            name = binding.nameEditText.text.toString().trim(),
            id = student.id,
            phone = binding.phoneEditText.text.toString().trim(),
            address = binding.addressEditText.text.toString().trim(),
            isChecked = binding.isCheckedCheckbox.isChecked,
            imageUri = student.imageUri,
            birthDate = binding.birthDateEditText.text?.toString()?.takeIf { it.isNotBlank() },
            birthTime = binding.birthTimeEditText.text?.toString()?.takeIf { it.isNotBlank() }
        )

        StudentRepository.updateStudent(updated)

        // מציגים דיאלוג קודם, מנווטים בתוך OK, ללא navigateUp מוקדם
        AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage("Student updated successfully.")
            .setPositiveButton(android.R.string.ok) { d, _ ->
                d.dismiss()

                // שולחים אות עדכון למסך הקודם
                findNavController().previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("studentUpdatedId", updated.id)

                // ניווט יחיד אחורה
                findNavController().navigateUp()
            }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
