package com.example.myapplication.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.App
import com.example.myapplication.Navigator
import java.lang.IllegalStateException

class ViewModelFactory (
    private val app: App
        ): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass)
        {
            UserListViewModel::class.java ->{
                UserListViewModel(app.usersService)
            }
            UserDetailsViewModel::class.java->{
                UserDetailsViewModel(app.usersService)
            }
            else->{
                throw IllegalStateException("Unknown view model class")
            }
        }
       return viewModel as T
    }
 }

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator