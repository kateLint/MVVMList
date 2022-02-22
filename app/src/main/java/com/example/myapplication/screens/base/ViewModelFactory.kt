package com.example.myapplication.screens.base

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.model.UsersService
import com.example.myapplication.navigator.ARG_SCREEN
import com.example.myapplication.navigator.MainNavigator
import com.example.myapplication.navigator.Navigator

class ViewModelFactory(
    private val usersService: UsersService?,
    private val screen:BaseScreen,
    private val fragment: BaseFragment
        ) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val hostActivity = fragment.requireActivity()
        val application = hostActivity.application
        val navigationProvider = ViewModelProvider(hostActivity, ViewModelProvider.AndroidViewModelFactory(application))
        val navigator = navigationProvider[MainNavigator::class.java]

        val constructor = modelClass.getConstructor(null,Navigator::class.java, screen::class.java)
        return constructor.newInstance(null,navigator, screen)
    }
}

inline fun<reified VM: ViewModel> BaseFragment.screenViewModel() = viewModels<VM>{
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen
    ViewModelFactory(null, screen, this)
}