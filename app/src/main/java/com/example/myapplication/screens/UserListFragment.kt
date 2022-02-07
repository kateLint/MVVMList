package com.example.myapplication.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.UserActionListener
import com.example.myapplication.UsersAdapter
import com.example.myapplication.databinding.FragmentUsersListBinding
import com.example.myapplication.model.User
import com.example.myapplication.task.EmptyResult
import com.example.myapplication.task.ErrorResult
import com.example.myapplication.task.PendingResult
import com.example.myapplication.task.SuccessResult

class UserListFragment: Fragment()
{
    private lateinit var binding: FragmentUsersListBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel: UserListViewModel by viewModels{factory()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersListBinding.inflate(inflater,container,false)
        adapter = UsersAdapter(viewModel)


  /*      object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUser(user,moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }
        }*/

        viewModel.users.observe(viewLifecycleOwner, Observer {
            hideAll()
            when(it){
                is SuccessResult ->{
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.users = it.data
                }
                is ErrorResult -> {
                    binding.tryAgainContainer.visibility = View.VISIBLE
                }
                is PendingResult -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is EmptyResult ->{
                    binding.noUsersTextView.visibility = View.VISIBLE
                }
            }

        })

        viewModel.actionShowDetails.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let{
                user-> navigator().showDetails(user)
            }
        })

        viewModel.actionShowToast.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let{
                    messageRes-> navigator().toast(messageRes = messageRes)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter


        return binding.root
    }

    private fun hideAll() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tryAgainContainer.visibility = View.GONE
        binding.noUsersTextView.visibility = View.GONE
    }
}