package com.example.myapplication.navigator

import androidx.annotation.StringRes
import com.example.myapplication.model.User
import com.example.myapplication.screens.base.BaseScreen

interface Navigator {
    fun launch(screen: BaseScreen)

    fun goBack(result:Any? = null)

    fun toast(@StringRes messageRes: Int)

    fun getString(@StringRes messageRES: Int): String

    fun showDetails(user: User)
}