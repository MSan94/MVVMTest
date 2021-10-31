package com.prj.mvvmtest.model

import com.prj.mvvmtest.model.enum.KakaoSearchSortEnum
import com.prj.mvvmtest.model.response.ImageSearchResponse
import com.prj.mvvmtest.model.service.KakaoSearchService
import io.reactivex.Single

class DataModelImpl(private val service:KakaoSearchService) : DataModel{
    private val KAKAO_APP_KEY = "57890c80a21825e392427b45a68aaf80"
    override fun getData(query:String, sort: KakaoSearchSortEnum, page:Int, size:Int) : Single<ImageSearchResponse> {
        return service.searchImage(auth = "KakaoAK $KAKAO_APP_KEY", query = query, sort = sort.sort, page = page, size = size)
    }
}