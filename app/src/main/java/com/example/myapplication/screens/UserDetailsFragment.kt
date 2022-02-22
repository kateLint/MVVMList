package com.example.myapplication.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentUserDetailsBinding
import com.example.myapplication.model.User
import com.example.myapplication.model.UserDetails
import com.example.myapplication.screens.base.BaseFragment
import com.example.myapplication.screens.base.BaseScreen
import com.example.myapplication.screens.base.screenViewModel
import com.example.myapplication.task.SuccessResult

class UserDetailsFragment : BaseFragment(){

    class Screen(val useID: Long): BaseScreen

    private lateinit var binding: FragmentUserDetailsBinding
   // private val viewModel: UserDetailsViewModel by viewModels { factory()}

    override val viewModel by screenViewModel<UserDetailsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(requireArguments().getLong(ARG_USER_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(layoutInflater, container, false)


        viewModel.actionShowToast.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let{
                   // messageRes-> navigator().toast(messageRes = messageRes)
            }
        })


        viewModel.state.observe(viewLifecycleOwner, Observer {
            binding.contentContainer.visibility = if(it.showContent) {
                val userDetails = (it.userDetailsResult as SuccessResult).data
                binding.userNameTextView.text = userDetails.user.name


                if (userDetails.user.photo.isNotBlank()) {
                    Glide.with(this)
                        .load(userDetails.user.photo)
                        .circleCrop()
                        .into(binding.photoImageView)
                } else {
                    Glide.with(this)
                        .load(R.drawable.ic_user_avatar)
                        .into(binding.photoImageView)
                    // you can also use the following code instead of these two lines ^
                    // Glide.with(photoImageView.context)
                    //        .load(R.drawable.ic_user_avatar)
                    //        .into(photoImageView)
                }
                binding.userDetailsTextView.text = userDetails.details
                
                View.VISIBLE
            }else{
                
                View.GONE
            }
            binding.progressBar.visibility = if(it.showPregress) View.VISIBLE else View.GONE
            binding.deleteButton.isEnabled = it.enableDeleteButton
        })
    /*    viewModel.state.observe(viewLifecycleOwner, Observer {
            binding.userNameTextView.text = it.user.name
            if (it.user.photo.isNotBlank()) {
                Glide.with(this)
                    .load(it.user.photo)
                    .circleCrop()
                    .into(binding.photoImageView)
            } else {
                 Glide.with(this)
                        .load(R.drawable.ic_user_avatar)
                        .into(binding.photoImageView)
                // you can also use the following code instead of these two lines ^
                // Glide.with(photoImageView.context)
                //        .load(R.drawable.ic_user_avatar)
                //        .into(photoImageView)
            }
            binding.userDetailsTextView.text = it.details

        })*/

        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
        /*    navigator().toast(R.string.user_has_been_deleted)
            navigator().goBack()*/ //now work with event
        }
        return binding.root
    }
    companion object {

        private const val ARG_USER_ID = "ARG_USER_ID"

        fun newInstance(userId: Long): UserDetailsFragment{
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf(ARG_USER_ID to userId)
            return fragment
        }
    }

}