package com.prj.mvvmtest.util.snackbar

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class SnackbarMessage : SingleLiveEvent<Int>() {
    fun observe(owner : LifecycleOwner , observer:(Int) -> Unit){
        super.observe(owner, Observer { it ->
            it?.run{
                observer(it)
            }
        })
    }
}

class SnackbarMessageString : SingleLiveEvent<String>(){
    fun observe(owner : LifecycleOwner, observer : (String) -> Unit){
        super.observe(owner, Observer{ it ->
            it?.run {
                observer(it)
            }
        })
    }
}
