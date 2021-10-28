package com.prj.mvvmtest.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.prj.mvvmtest.R
import com.prj.mvvmtest.data.model.Entity
import com.prj.mvvmtest.databinding.ActivityMainBinding
import com.prj.mvvmtest.presentation.viewmodel.MainViewModel
import com.prj.mvvmtest.util.adapter.RecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        binding.viewModel = viewModel

        val mAdapter = RecyclerViewAdapter(this,viewModel)
        binding.recyclerview.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }

        viewModel.allUsers.observe(this, Observer { users ->
            // update the cached copy of the users in the adapter
            users?.let{
                mAdapter.setUsers(it)
            }
        })
        binding.button.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                viewModel.insert(
                    Entity(0,binding.edit.toString())
                )
            }
        }

    }
}