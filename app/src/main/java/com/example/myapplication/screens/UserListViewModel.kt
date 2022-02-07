package com.example.myapplication.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.UserActionListener
import com.example.myapplication.model.User
import com.example.myapplication.model.UsersListener
import com.example.myapplication.model.UsersService
import com.example.myapplication.task.*

data class UserListItem(
    val user: User,
    val isInProgress: Boolean
)


class UserListViewModel(
    private val usersService: UsersService
) : BaseViewModel(), UserActionListener {

    private val _users = MutableLiveData<Result<List<UserListItem>>>() //Mutable not only listen data also can change, private only for VM
    val users: LiveData<Result<List<UserListItem>>> = _users // public, fragment can listen but can't change

    val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> = _actionShowDetails

    val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast


    private val userIdsInProgress = mutableSetOf<Long>()
    private var usersResult: Result<List<User>> = EmptyResult() //can be in class viewmodelstate
        set(value) {
            field = value
            notifyUpdates()
        }


    private val listener: UsersListener = {

        usersResult = if(it.isEmpty()){
            EmptyResult()
        }else {
            SuccessResult(it)
        }

    }

    init {
        loadUser()
        usersService.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener) // userService is singelton, so we need to remove it, cause it live more than livedata
    }
    fun loadUser(){
        usersResult = PendingResult()
        usersService.loadUsers()
            .onError {
                usersResult = ErrorResult(it)
            }
            .autoCancel()
    }

 /*   fun moveUser(user: User, movyBy: Int){
        if(isInProgress(user)) return
        addProgressTo(user)
        usersService.moveUser(user, movyBy)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError { removeProgressFrom(user) }
            .autoCancel()
    }*/

  /*  fun deleteUser(user: User){
        usersService.deleteUser(user)
            .onError { removeProgressFrom(user) }
            .onSuccess { removeProgressFrom(user) }
            .autoCancel()

    }*/

    private fun notifyUpdates() {
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }

    private fun addProgressTo(user: User) {
        userIdsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom(user: User) {
        userIdsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean {
        return userIdsInProgress.contains(user.id)
    }

    override fun onUserMove(user: User, moveBy: Int) {
        if(isInProgress(user)) return
        addProgressTo(user)
        usersService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_move_user)
            }
            .onError { removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_move_user)}
            .autoCancel()
    }

    override fun onUserDelete(user: User) {
        usersService.deleteUser(user)
            .onError { removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_delete_user)}
            .onSuccess { removeProgressFrom(user) }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
       _actionShowDetails.value = Event(user)
    }

}