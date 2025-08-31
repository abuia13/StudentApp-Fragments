package com.studentapp.ui.studentlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentapp.R
import com.studentapp.StudentAdapter
import com.studentapp.databinding.FragmentStudentListBinding
import com.studentapp.model.StudentRepository


class StudentListFragment : Fragment(R.layout.fragment_student_list) {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentAdapter: StudentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding = FragmentStudentListBinding.bind(view)




        val studentsList = StudentRepository.getAllStudents()

        studentAdapter = StudentAdapter(
            studentsList,
            onItemClick = { position ->
                val student = studentsList[position]
                val action = StudentListFragmentDirections
                    .actionStudentListToStudentDetails(student)
                findNavController().navigate(action)
            },
            onCheckClick = { position ->
                val student = studentsList[position]
                student.isChecked = !student.isChecked
            }
        )

        binding.studentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.studentsRecyclerView.adapter = studentAdapter

        // Action Bar menu (Add)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.student_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_student -> {

                        val action = StudentListFragmentDirections.actionStudentListToAddStudent()
                        findNavController().navigate(action)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        // רענון הרשימה אחרי חזרה ממסך הוספה/עריכה
        studentAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
