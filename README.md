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


# Part2 KOIN
- 의존성 주입 ( Dependency injection )
- 테스팅을 위해 사용 
- 간단한 방법으로는 모델을 내부에서 생성하지 않고 생성자의 인자로 준다.
```
---- 변경 전 ------
ViewModel{
  val model = Model()
}
---- 변경 후 ------
ViewModel(val model){
}
```
- 이렇게 뷰모델을 테스팅 모듈에서 생성할 대 테스팅용 모델을 뷰모델의 생성자로 **주입** 해줌으로서 뷰모델을 테스팅하기 쉽게 할 수 있다.
- https://medium.com/harrythegreat/kotlin%EC%97%90%EC%84%9C-dagger2-%EC%93%B0%EA%B8%B0-%ED%9E%98%EB%93%9C%EB%8B%88-%EA%B7%B8%EB%9F%BC-%EB%84%8C-koin%EC%9D%B4%EC%95%BC-e9e42ec1288e

#### gradle
```
// koin
implementation "org.koin:koin-androidx-scope:1.0.2"
implementation "org.koin:koin-androidx-viewmodel:1.0.2"
```

#### 의존성 주입 모듈
```
- 의존성 주입을 하는 실제 코드, 혹은 의존성 주입을 위한 설계도
var modelPart = module {
    factory<DataModel> {
        DataModelImpl()
    }
}

var viewModelPart = module {
    viewModel {
        MainViewModel(get())
    }
}

var myDiModule = listOf(modelPart, viewModelPart)
```
- factory{}
  - 말 그대로 공장이란 의미로, DataModelImpl()이라는 클래스를 생성
  - 다른 클래스에서 해당 부분이 필요하다면 단순히 **get()/by inject()을** 해주면 팩토리로 만든 클래스가 들어간다.
- single{}
  - 싱글톤처럼 어플리케이션에서 단 하나만 만든다.
  - 주로 retrofit을 통해 만든 서비스 클래스에서 사용
- viewModel{]
  - 말 그대로 뷰모델을 만든다.
  - 액티비티에서 by viewModel()을 통해 얻을 수 있다.

#### Application 클래스
- startKoin을 통해 의존성 주입
```
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, myDiModule)
    }
}
```
- **매니페스트 설정 필수!**
```
<application
        android:name=".MyApplication"
```

## 주입 방법
- 코드 내에서 주입
```
1) val service : BusinessService by inject()

2)
class Controller(val service : BusinessService){ 
  
  fun hello() {
     // service is ready to use
     service.sayHello()
  }
} 

3) val vm : MyViewModel by viewModel()
```
- 테스팅
```
// Just tag your class with KoinTest to unlick your testing power
class SimpleTest : KoinTest { 
  
  // lazy inject BusinessService into property
  val service : BusinessService by inject()

  @Test
  fun myTest() {
      // You can start your Koin configuration
      startKoin(myModules)

      // or directly get any instance
      val service : BusinessService = get()

      // Don't forget to close it at the end
      stopKoin()
  }
} 
```

# Part3 RxJava + Retrofit
- Retrofit으로 네트워크 통신을하고 그 결과값을 RxJava로 받아오기
#### RxJava
- 

## 참고 사이트
