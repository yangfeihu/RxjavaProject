package com.tcl.base.ui.login;

import android.view.View;
import android.widget.Toast;

import com.tcl.base.R;
import com.tcl.base.base.BaseActivity;
import com.tcl.base.databinding.ActivityLoginBinding;
import com.tcl.base.model.Msg;
import com.tcl.base.net.IRequest;
import com.tcl.base.net.RequestManager;
import com.tcl.base.ui.main.MainActivity;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static android.widget.Toast.makeText;

public class LoginActivity extends BaseActivity<LoginPresenter,ActivityLoginBinding> implements LoginContract.View {
    IRequest request = RequestManager.getInstance().createRequest();
    @Override
    public int getRootView() {
        return R.layout.activity_login;
    }
    public void onClick(View view){

        //testRxJava();

    /*   Toast toast = Toast.makeText(LoginActivity.this,"",Toast.LENGTH_LONG);
        request.download("http://172.19.0.11:8080/download.zip", new IFileCallBack() {
            @Override
            public void onProgress(long progress, long total) {
                toast.setText("progress ="+progress);
                toast.show();
            }
            @Override
            public void onFailure(String error) {
                toast.setText("error ="+error);
                toast.show();
            }
            @Override
            public void onSuccess(String path) {
                toast.setText("path ="+path);
                toast.show();
            }
        });
*/

/*        Router.build("MainActivity").go(this);
          finish();
          */
/*
          new Thread(()->{
              Msg msg = new Msg();
              msg.type = 333;
              RxBus.getDefault().post(msg);
          }).start();*/

       mPresenter.login(mViewBinding.name.getText().toString(),mViewBinding.password.getText().toString());
    }
    private void testRxJava(){
        // create a flowable
/*        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("hello RxJava 2");
                e.onNext("hello RxJava 3");
                e.onNext("hello RxJava 4");
                e.onNext("hello RxJava 5");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);


        flowable = Flowable.just("hello RxJava 2","hello RxJava 3","hello RxJava 4");*/



      // create
        /*Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext="+s);
            }

            @Override
            public void onError(Throwable t) {

            }
            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };*/


/*        Consumer consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }

        };
        flowable.subscribe(consumer);*/




        //flowable.subscribe(subscriber);








      /*  Flowable.just("map")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + " -ittianyu111";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });

        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(1);
        list.add(5);
        list.add(1);
        list.add(5);



        Flowable.fromIterable(list)
                .subscribe(num -> System.out.println(num));*/



        /*Flowable.fromArray(1, 20, 5, 0, -1, 8)
                .take(2)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer.intValue() > 5;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });*/
        Flowable.range(0,100).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });







    }


    @Override
    protected void handler(Msg msg) {
        super.handler(msg);
        makeText(this,""+msg.type,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess() {
        this.runOnUiThread(()-> makeText(LoginActivity.this, "loginSuccess", Toast.LENGTH_LONG).show());
        openActivity(MainActivity.class);
    }
    @Override
    public void loginFailed() {
        this.runOnUiThread(()-> makeText(LoginActivity.this, "loginFailed", Toast.LENGTH_LONG).show());
    }

}

