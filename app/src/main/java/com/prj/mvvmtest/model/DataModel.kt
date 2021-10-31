package com.prj.mvvmtest.model

import com.prj.mvvmtest.model.enum.KakaoSearchSortEnum
import com.prj.mvvmtest.model.response.ImageSearchResponse
import io.reactivex.Single


interface DataModel{
    fun getData(query:String, sort: KakaoSearchSortEnum, page:Int, size:Int) : Single<ImageSearchResponse>
}
