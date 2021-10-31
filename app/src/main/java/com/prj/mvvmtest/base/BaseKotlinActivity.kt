package com.prj.mvvmtest.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

/**
 *     BaseKoltinActivity<ActivitySbsMainBinding>
 *     와 같이 상속 받을 때, ActivitySbsMainBinding과 같은 파일이 자동생성되지 않는다면
 *     1. <layout></layout>으로 레이아웃이 감싸져있는지 확인
 *     2. 다시 빌드 ot 클린
 *     3. 이름 확인
 */

abstract class BaseKotlinActivity<T : ViewDataBinding, R : BaseKotlinViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T

    /**
     * setContentView로 호출할 Layout의 리소스 id
     */
    abstract val layoutResourceId : Int

    /**
     * viewModel로 쓰일 변수
     */
    abstract val viewModel : R

    /**
     * 레이아웃을 띄운 직후 호출
     * 뷰나 액티비티의 속성 등을 초기화
     * ex) RecyclerView, ToolBar, 드로어뷰
     */
    abstract fun initStartView()

    /**
     * 두번째로 호출
     * 데이터 바인딩 및 rxJava 설정
     * ex) rxjava observe, databinding observe..
     */
    abstract fun initDataBinding()

    /**
     * 바인딩 이후에 할 일을 여기에 구현
     * 그 외에 설정할 것이 있으면 이곳에서 설정
     * 클릭 리스너도 이곳에서 설정
     */
    abstract fun initAfterBinding()

    private var isSerBackButtonValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this,layoutResourceId)

        snackbarObserving()
        initStartView()
        initDataBinding()
        initAfterBinding()
    }
    private fun snackbarObserving() {
        viewModel.observeSnackbarMessage(this) {
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
        }
        viewModel.observeSnackbarMessageStr(this){
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
        }
    }
}