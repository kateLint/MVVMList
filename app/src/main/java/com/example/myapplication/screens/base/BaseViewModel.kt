package com.example.myapplication.screens.base

import androidx.lifecycle.ViewModel
import com.example.myapplication.task.Task


open class BaseViewModel: ViewModel() {

    open fun onResult(result: Any){

    }


    private val task = mutableListOf<Task<*>>()
    override fun onCleared() {
        super.onCleared()
        task.forEach { it.cancel() }
    }

    fun <T> Task<T>.autoCancel(){
        task.add(this)
    }

}