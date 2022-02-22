package com.example.myapplication.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Event
import com.example.myapplication.R
import com.example.myapplication.model.UserDetails
import com.example.myapplication.model.UsersService
import com.example.myapplication.navigator.Navigator
import com.example.myapplication.screens.base.BaseViewModel
import com.example.myapplication.task.*

class UserDetailsViewModel(private val usersService: UsersService,
                           private val navigator: Navigator,
                           screen: UserListFragment.Screen) : BaseViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

  /*  val _actionGoBack = MutableLiveData<Event<Unit>>()
    val actionGoBack: LiveData<Event<Unit>> = _actionGoBack*/

    private val currentState: State get() = state.value!!

    init{
        _state.value = State(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
        loadUser(screen as Long)

    }


    fun loadUser(userId: Long){
        if(currentState.userDetailsResult is SuccessResult) return

        _state.value = currentState.copy(userDetailsResult = PendingResult())

        usersService.getById(userId)
            .onSuccess {
                _state.value = currentState.copy(userDetailsResult = SuccessResult(it))
            }
            .onError {
                _actionShowToast.value = Event(R.string.cant_load_user_details)
                navigator.goBack()
            }
            .autoCancel()
    }

    fun deleteUser(){
        val userDetailsResult = currentState.userDetailsResult
        if(userDetailsResult !is SuccessResult) return
        _state.value = currentState.copy(deletingInProgress = true)
        usersService.deleteUser(userDetailsResult.data.user)

            .onSuccess {
                _actionShowToast.value = Event(R.string.user_has_been_deleted)
                navigator.goBack()
            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
            }
            .autoCancel()
    }

    data class State(
        val userDetailsResult: Result<UserDetails>,
        private val deletingInProgress: Boolean
    ){
        val showContent: Boolean get() = userDetailsResult is SuccessResult
        val showPregress: Boolean get() = userDetailsResult is PendingResult || deletingInProgress
        val enableDeleteButton: Boolean get() = !deletingInProgress
    }
}