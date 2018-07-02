RxJava简介

RxJava可以看做是观察者模式的升级，使用RxJava的目的主要是为了数据异步处理。相比于Handler来说，优点就在于简洁，逻辑上非常简单明了。
基本组成

由Observable、Observer、Subscribe完成基本功能，Observable（被观察者）、Observer/Su（观察者）、Subscribe（订阅）这三部分完成观察模式。

Observable作为被观察者，在整个过程中担任消息通知的工作，可以指定自己本身的逻辑方法，并且通过onNext()、onComplete()将通知发出。

subscribe作为订阅，作为链条连接Observable与Observer，完成观察者与被观察者之间的联系。

Observer作为观察者，当收到通知后，能够在onNext()、onComplete()、onError()中处理通知内容。RxJava同时提供了Subscribe接口，该接口与Observer接口基本一致，另加了onStart():在通知发送前执行预准备代码；unsubscribe()方法：用于取消订阅，应及时在不需要时调用，避免内存泄漏。

基本使用

1.分别定义

//创建Observable
Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
    @Override
    public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext("Hello");
        subscriber.onNext("Hi");
        subscriber.onNext("Aloha");
        subscriber.onCompleted();
    }
});

//创建Observer
Obaserver observer = new Observer<String>() {
    @Override
    public void onNext(String text) {
        Log.i("TAG", text);
    }

    @Override
    public void onCompleted() {
        //TODO 完成内容
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
    }
};

//订阅
observable.subcribe(observer);

2.Observable的变形

Observable observable = Observable.just("This", "is", "RxJava");
1
以上代码执行后将会**依次执行**onNext(“This”),onNext(“is”),onNext(“RxJava”),onComplete();

String[] strs = {"This", "is", "RxJava"};
Observable observable = Observable.form(strs);
1
2
以上代码执行后将会**依次执行**onNext(“This”),onNext(“is”),onNext(“RxJava”),onComplete();

3.Observer的变形

能够单独抽出onNext()、onComplete()、onError()来对Observable进行观察。

//onNext()  ————参数可定义为其他类型
obserable.subcribe(new Action1<String>(){
    public void call(String next){
        Log.i(TAG, next);
    }
});

//onError()
obserable.subcribe(new Action1<Throwable>(){
    public void call(Throwable throwable){

    }
});

//onComplete()
obserable.subcribe(new Action0(){
    public void call(){
        "Complete 不存在参数"
    }
});

4.连续定义

Observable.create(new OnSubscribe<String>() {
    @Override
    public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext("run onNext()");
        subscriber.onCompleted();
    }
}).subscribe(new Observer<String>() {
    @Override
    public void onNext(String text) {
        Log.i("TAG", text);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
    }
});

Scheduler

了解以上的内容后，RxJava的基本使用已经没啥问题了，然而，正如之前所说，我们大多数使用RxJava的目的是达到不同线程之间的通信并且让这一过程的逻辑尽量简洁。

而上面所介绍的所有动作都是在同一线程中完成的。也就是说在默认时，Observable中方法、Observer中方法都会在Retrofit创建的线程中运行。而在这一情况下，我们使用RxJava就没有意义了。这时就需要使用到Scheduler来进行线程控制。

线程分类

Schedulers.immdiate：当前线程（默认值）
Schedulers.io：适合IO操作（文件读写、网络数据交互）
Schdulers.newThread：开启新线程
Schdulers.computation：计算线程（常用于图像计算）
AndroidSchdulers.mainThread：Android中特用，主线程（UI线程）

线程使用

sbuscribeOn()：被观察者动作线程

observerOn()：观察者处理线程

Scheduler原理

之前看到扔物线大神说这里是精华部分，嗯，反正我是看high了。关于这一部分，我目前实在是没有太多精力完全看明白，所以先不献丑了，等到后期我能够完全理解后，再向大家介绍。

Scheduler使用(变换)

map()
能够完成对象的转换。将Observable获取的对象通过map()将数据从一种对象装换为另一对象，配合Function1完成这一动作。

Func1<Object1, Obejct2>()

Object1:代表参数对象
OBejct2:代表返回对象

Func1与Action1基本一致，唯一区别就在于Func1存在返回值。

flatmap()
完成对象转换，并且是将一个对象转换为Observable

'  单调的文字说明是有点绕,用代码来看看  '

'  设定：  '
'  Person：包含属性  name(String), age(int), address(Address[])  包含多个'

'  目的：获取所有的朋友所有住址名称'

    Person[] persons = "......";

    Observable.from(persons).
               flatmap(new Func1<Person, Observable<friend, Address>>(){
                    public Observable<Address> call(Person person){
                        return Observable.from(person.getAddress());
                    }
               }).
               subscribe(new Action1<Address>(){
                    public void call(Address address){
                        Log.i("TAG", address.getName());
                    }
               });

打完收功。是不是很简单，都不用解释。

来来来，你把键盘放下，我好好说话。

那么这个flatmap到底做了啥，我们先来观察一下代码。

目的是要获取每个Person的所有Address

flatmap()首先处理Person对象，从一个person对象中获取Address[]，之后将获取到的Address[]通过Observable.from()依次处理，return这个点上，所做的是将Observable依次传递给Observer处理。而Observer将会获取到Address，用来进一步处理。

嗯，解释的很清楚，再不懂就不能怪我。

好好好，先把鼠标线从我脖子上拿开，我再给你画张图。

这里写图片描述

清晰，明了，给自己点赞。

以上的内容就是目前对RxJava的理解啦，现在网上还有很多大神关于这一部分的讲解，大多都很详细，本文算是交流学习的记录吧。
