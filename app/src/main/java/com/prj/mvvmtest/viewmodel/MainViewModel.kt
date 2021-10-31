package com.prj.mvvmtest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prj.mvvmtest.base.BaseKotlinViewModel
import com.prj.mvvmtest.model.DataModel
import com.prj.mvvmtest.model.enum.KakaoSearchSortEnum
import com.prj.mvvmtest.model.response.ImageSearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val model: DataModel) : BaseKotlinViewModel() {

    private val _imageSearchResponseLiveData = MutableLiveData<ImageSearchResponse>()

    val imageSearchResponseLiveData: LiveData<ImageSearchResponse>
        get() = _imageSearchResponseLiveData

    fun getImageSearch(query:String, page:Int, size:Int){
        addDisposable(model.getData(query,KakaoSearchSortEnum.Accuracy,page,size)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                it.run{
                    if(documents.size > 0){
                        _imageSearchResponseLiveData.postValue(this)
                    }
                    Log.d("MainViewModel", "meta : $meta")
                }
            }, {
                Log.d("MainViewModel", "response error, message : ${it.message}")
            }))
    }

}