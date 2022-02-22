package com.example.myapplication

class Event<T> (private val value: T){
    private var handler: Boolean = false

    fun getValue(): T?{
        if(handler) return null
        handler = true
        return value
    }
}