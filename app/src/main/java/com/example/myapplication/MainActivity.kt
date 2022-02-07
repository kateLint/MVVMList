package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.User
import com.example.myapplication.model.UsersListener
import com.example.myapplication.model.UsersService
import com.example.myapplication.screens.UserDetailsFragment
import com.example.myapplication.screens.UserListFragment


class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, UserListFragment())
                .commit()
        }
    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(user.id ))
            .commit()
    }

    override fun goBack() {
       onBackPressed()
    }

    override fun toast(messageRes: Int) {
       Toast.makeText(this,messageRes,Toast.LENGTH_SHORT).show()
    }

}