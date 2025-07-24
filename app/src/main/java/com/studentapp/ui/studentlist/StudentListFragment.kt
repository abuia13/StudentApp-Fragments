package com.studentapp.ui.studentlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentapp.model.StudentRepository
import com.studentapp.databinding.FragmentStudentListBinding
import com.studentapp.StudentAdapter
import com.studentapp.R



class StudentListFragment : Fragment() {
    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val studentsList = StudentRepository.getAllStudents()

        studentAdapter = StudentAdapter(
            studentsList,
            onItemClick = { position ->
                val student = studentsList[position]
                val action = StudentListFragmentDirections.actionStudentListToStudentDetails(student)
                findNavController().navigate(action)
            },
            onCheckClick = { position ->
                val student = studentsList[position]
                student.isChecked = !student.isChecked
            }
        )

        binding.studentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.studentsRecyclerView.adapter = studentAdapter
    }

    override fun onResume() {
        super.onResume()
        studentAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.student_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_student -> {
                findNavController().navigate(StudentListFragmentDirections.actionStudentListToAddStudent())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
