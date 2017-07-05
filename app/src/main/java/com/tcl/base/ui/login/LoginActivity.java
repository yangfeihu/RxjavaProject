package com.tcl.base.ui.login;

import android.view.View;
import android.widget.Toast;

import com.tcl.base.R;
import com.tcl.base.base.BaseActivity;
import com.tcl.base.databinding.ActivityLoginBinding;
import com.tcl.base.model.Msg;
import com.tcl.base.ui.main.MainActivity;

import static android.widget.Toast.makeText;

public class LoginActivity extends BaseActivity<LoginPresenter, ActivityLoginBinding> implements LoginContract.View {
    @Override
    public int getRootView() {
        return R.layout.activity_login;
    }

    public void onClick(View view) {
        mPresenter.login(mViewBinding.name.getText().toString(), mViewBinding.password.getText().toString());
    }

    @Override
    protected void handler(Msg msg) {
        super.handler(msg);
        makeText(this, "" + msg.type, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String errorString, int errorcode, int requestcode) {
        this.runOnUiThread(() -> makeText(LoginActivity.this, "loginFailed", Toast.LENGTH_LONG).show());
    }

    @Override
    public <T> void onSuccess(T t, int requestcode) {
        this.runOnUiThread(() -> makeText(LoginActivity.this, "loginSuccess", Toast.LENGTH_LONG).show());
        openActivity(MainActivity.class);
    }

}

