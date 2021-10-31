# MVVMTest
MVVM패턴 실습 레파지토리

![image](https://user-images.githubusercontent.com/81352078/139571647-78f0e0a3-2d6c-4484-b706-7d25b2c26969.png)


## MVVM이란?
- MVP 패턴에서 Presenter와 View, 그리고 Model간의 양방향 의존성이 너무 깊어서, 그 점을 개선한 패턴
- View는 ViewModel의 참조를 가지고 이지만, ViewModel은 View의 참조를 가지고 있지 않다.
- ViewModel도 Model의 참조를 가지고 있지만, Model은 ViewModel의 참조를 가지고 있지 않다.
- ViewModel은 StartActivity나 SnackBar등을 호출하는 방법
  - View가 ViewModel을 Binding하고 있으면 된다.
  - ViewModel은 단순히 값을 바꾸기만 하고, View가 그 값이 바뀌는걸 관찰

## 실습
#### Gradle 설정
- 마이그레이션
```
  Refactor -> Migrate to AndroidX... (Android Studio 3.2 이상)
```

- RxJava, Koin, picasso, retorfit 사용 ( 버전은 공부시점 버전 )
```
    // rxjava
    implementation "io.reactivex.rxjava2:rxjava:2.2.0"
    implementation "io.reactivex.rxjava2:rxandroid:2.0.2"

    // koin
    implementation "org.koin:koin-androidx-scope:1.0.2"
    implementation "org.koin:koin-androidx-viewmodel:1.0.2"

    // picasso
    implementation "com.squareup.picasso:picasso:2.5.2"

    // retrofit
    implementation "com.squareup.retrofit2:retrofit:2.5.0"
    implementation "com.squareup.retrofit2:converter-gson:2.5.0"
    implementation "com.squareup.retrofit2:adapter-rxjava2:2.5.0"
    implementation "com.squareup.retrofit2:retrofit-mock:2.4.0"
```

- DataBinding
```
    buildFeatures{
        dataBinding true
    }
```

#### BaseViewModel
- ViewModel이 상속 받은 BaseViewModel 생성
- 기본적으로 ViewModel은 android.lifecycle.ViewModel을 상속받으면 끝
```
open class BaseKotlinViewModel : ViewModel(){

    /**
     * RxJava의 observing을 위한 부분
     * addDisposable을 이용하여 추가하기만 하면 된다.
     */

    private val compositedDisposable = CompositeDisposable()
    
    fun addDisposable(disposable : Disposable){
        compositedDisposable.add(disposable)
    }

    override fun onCleared() {
        compositedDisposable.clear()
        super.onCleared()
    }

}
```
- Model에서 들어오는 Single<>과 같은 RxJava 객체들의 Observing을 위한 부분
- 기본적으로 RxJava의 Observable들은 compositeDisposable에 추가를 해주고, 뷰모델이 없어질 때 추가했던 것들을 지워야 한다.
- 이 부분은 그러한 동작을 수행하는 코드로서, Observable들을 옵저빙할때 addDisposable()을 쓰게 된다.
- 또한 ViewModel은 View와의 생명주기를 공유하기 때문에 View가 부서질 때 ViewModel의 onCleared()가 호출되어 클리어 시킨다.
- 대략적인 사용법은 아래와 같다.
```
addDisposable(model.requestToServer(senderInfo)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            // 성공적인 응답
        }, {
            // 에러
        }))
```

#### BaseView
```
/**
 *     BaseKoltinActivity<ActivitySbsMainBinding>
 *     와 같이 상속 받을 때, ActivitySbsMainBinding과 같은 파일이 자동생성되지 않는다면
 *     1. <layout></layout>으로 레이아웃이 감싸져있는지 확인
 *     2. 다시 빌드 ot 클린
 *     3. 이름 확인
 */

abstract class BaseView<T : ViewDataBinding, R : BaseKotlinViewModel> : AppCompatActivity() {
    
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
        
        initStartView()
        initDataBinding()
        initAfterBinding()
    }

}
```
- 모든 액티비티는 이 베이스 액티비티를 구현
- **lateinit var viewDataBinding** : T : 액티비티의 layout을 빌드하면 자동 생성 클래스
  ```
  <?xml version="1.0" encoding="utf-8"?>
    <layout
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:tools="http://schemas.android.com/tools"
            xmlns:app="http://schemas.android.com/apk/res-auto">

        <androidx.constraintlayout.widget.ConstraintLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                tools:context=".view.MainActivity">
        </androidx.constraintlayout.widget.ConstraintLayout>
    </layout>
  ```
  - 일반적인 레이아웃을 만들지만, 그 레이아웃을 <layout></layout>으로 감싼다.
  - 그리고 빌드를 하면 ViewDataBinding 클래스가 자동 생성
- **lateinit var layoutResourceId** : Int
  - onCreate에서 레이아웃 리소스를 설정해야하는데, BaseKotlinActivity에서 onCreate를 오버라이딩 해버리니 BaseKotlinActivity를 구현한 액티비티는 레이아웃 리소스를 설정해줄 곳이 없다.
  - 그래서 아래와 같은 방법 사용
  ```
  ovveride val layoutResourceId : Int
    get() = R.layout.activity_main
    
  -- BaseKotlinActivity에서는
  viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
  ```
- **abstract val viewModel : R**
  - 액티비티가 BaseKotlinActivity를 구현할 때, ViewDataBinding클래스 뿐만 아니라 뷰모델 클래스도 제네릭으로 준다.
  - 스낵바 옵저빙을 위해서 사용

#### MainActivity
```
import com.prj.mvvmtest.R
import com.prj.mvvmtest.base.BaseKotlinActivity
import com.prj.mvvmtest.databinding.ActivityMainBinding
import com.prj.mvvmtest.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseKotlinActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModel : MainViewModel by viewModel()

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }
}
```
- 제네릭으로 ViewDataBinding과 ViewModel을 준다.
- by viewModel()은 koin 의존성 주입

#### MainViewModel
```
class MainViewModel(private val model:DataModel): BaseKotlinViewModel() {
}
```
- 뷰모델을 생성할 때 파라미터로 model은 준다

### model
```
interface DataModel {
    fun getData()
}

class DataModelImpl: DataModel{
    override fun getData() {
        return
    }
}
```

## 참고 사이트
