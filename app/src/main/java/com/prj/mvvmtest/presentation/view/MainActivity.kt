package com.prj.mvvmtest.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.prj.mvvmtest.R
import com.prj.mvvmtest.databinding.ActivityMainBinding
import com.prj.mvvmtest.presentation.viewmodel.ContactViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var contactViewModel : ContactViewModel
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        contactViewModel = ViewModelProvider.of(this).get(ContactViewModel::class.java)
    }
}