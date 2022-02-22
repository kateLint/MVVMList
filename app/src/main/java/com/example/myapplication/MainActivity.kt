package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.User
import com.example.myapplication.model.UsersListener
import com.example.myapplication.model.UsersService
import com.example.myapplication.navigator.MainNavigator
import com.example.myapplication.navigator.Navigator
import com.example.myapplication.screens.UserDetailsFragment
import com.example.myapplication.screens.UserListFragment
import com.example.myapplication.screens.base.BaseFragment
import com.example.myapplication.screens.base.BaseScreen


class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private val navigator by viewModels<MainNavigator>{ViewModelProvider.AndroidViewModelFactory(application)}

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
   /*     if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, UserListFragment())
                .commit()
        }*/
        if(savedInstanceState == null){
            navigator.launchFragment(this, UserListFragment().Screen(), addToBackStack = false)
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)

    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        navigator.whenActivityActive.mainActivity = this
    }

    override fun onPause() {
        super.onPause()
        navigator.whenActivityActive.mainActivity = null
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            if(supportFragmentManager.backStackEntryCount > 0){
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }else{
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
            val result = navigator.result.value?.getValue()?:return
            if(f is BaseFragment){
                f.viewModel.onResult(result)
            }
        }
    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(user.id ))
            .commit()
    }



    override fun launch(screen: BaseScreen) {

    }

    override fun goBack(result: Any?) {
        onBackPressed()
    }

    override fun toast(messageRes: Int) {
       Toast.makeText(this,messageRes,Toast.LENGTH_SHORT).show()
    }

}