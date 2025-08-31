package com.studentapp.ui.addstudent

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.studentapp.R
import com.studentapp.databinding.FragmentAddStudentBinding
import com.studentapp.model.Student
import com.studentapp.model.StudentRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddStudentFragment : Fragment() {

    private var _binding: FragmentAddStudentBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null


    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let { binding.studentImage.setImageURI(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedImageUri = if (savedInstanceState != null) {
            if (android.os.Build.VERSION.SDK_INT >= 33) {
                savedInstanceState.getParcelable("selectedImageUri", android.net.Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable("selectedImageUri")
            }
        } else null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        selectedImageUri?.let { binding.studentImage.setImageURI(it) }

        binding.studentImage.setOnClickListener { pickImage.launch("image/*") }

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

        binding.addressEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveStudentAndClose()
                true
            } else false
        }


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_add_student, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_save_student -> {
                        saveStudentAndClose()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun saveStudentAndClose() {
        val name = binding.nameEditText.text?.toString()?.trim().orEmpty()
        val id = binding.idEditText.text?.toString()?.trim().orEmpty()
        val phone = binding.phoneEditText.text?.toString()?.trim().orEmpty()
        val address = binding.addressEditText.text?.toString()?.trim().orEmpty()
        val birthDate = binding.birthDateEditText.text?.toString()?.takeIf { it.isNotBlank() }
        val birthTime = binding.birthTimeEditText.text?.toString()?.takeIf { it.isNotBlank() }
        val isChecked = binding.checkedCheckBox.isChecked


        val errors = buildList {
            if (name.isEmpty()) add("Name is required")
            if (id.isEmpty()) add("ID is required")
        }
        if (errors.isNotEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage(errors.joinToString("\n"))
                .setPositiveButton(android.R.string.ok, null)
                .show()
            return
        }

        val student = Student(
            name = name,
            id = id,
            phone = phone,
            address = address,
            isChecked = isChecked,
            imageUri = selectedImageUri?.toString(),
            birthDate = birthDate,
            birthTime = birthTime
        )

        StudentRepository.addStudent(student)

        AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage("Student added successfully.")
            .setPositiveButton(android.R.string.ok) { d, _ ->
                d.dismiss()
                findNavController().navigateUp()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedImageUri?.let { outState.putParcelable("selectedImageUri", it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
