package com.example.myapplication

import com.example.myapplication.model.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(messageRes: Int)

}