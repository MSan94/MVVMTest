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


## 참고 사이트
