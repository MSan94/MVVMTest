/*
    data = Model
    -> 프로그램에 사용되는 실제 데이터를 담당하기 떄문에 Data 패키지로 분리
    -> model : 데이터 클래스(Entity)들의 패키지
    -> repository : Local 또는 Remote DataSource와 이러한 데이터 소스를 추상화하는 Repository 클래스. 네트워크 통신(Retrofit)을 위한 패키지


    presentation = View, ViewModel
    -> 화면을 표현하는 UI를 담당하는 View와 ViewModel
    -> View : 화면을 담당하는 Activity, Fragment, CustomView등
    -> ViewModel : View의 이벤트를 받아, Model에서 필요한 데이터를 가져와 View에서 원하는 데이터로 가공하는 역할 담당


*/