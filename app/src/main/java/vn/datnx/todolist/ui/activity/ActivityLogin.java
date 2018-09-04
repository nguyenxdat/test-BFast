package vn.datnx.todolist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.datnx.todolist.R;
import vn.datnx.todolist.network.TaskLogin;
import vn.datnx.todolist.network.TaskNetworkBase;

public class ActivityLogin extends ActivityBase {

    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onClickLoginButton();
                return true;
            }
            return false;
        }
    };

    @Override
    protected int getLayoutView() {
        return R.layout.login_activity;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        edtPassword.setOnEditorActionListener(onEditorActionListener);
    }

    @OnClick(R.id.img_avatar)
    void onChangeAvatarClickListener() {

    }

    @OnClick(R.id.txt_forgot_password)
    void onForgotPasswordClickListener() {

    }

    @OnClick(R.id.btn_login)
    void onLoginClickListener() {
        onClickLoginButton();
    }

    private void onClickLoginButton() {
        if (checkValidate()) {
            login();
        }
    }

    private boolean checkValidate() {
        if (TextUtils.isEmpty(edtEmail.getText())) {
            showToast(getString(R.string.error_email_not_null));
            return false;
        }
        if (TextUtils.isEmpty(edtPassword.getText())) {
            showToast(getString(R.string.error_password_not_null));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText()).matches()) {
            showToast(getString(R.string.error_email_not_valid));
            return false;
        }
        return true;
    }

    private void login() {
        showLoading();
        TaskLogin taskLogin = new TaskLogin(getApplicationContext(),
                edtEmail.getText().toString(), edtPassword.getText().toString());
        taskLogin.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                closeLoading();
                openMain();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                closeLoading();
                showMessageWith(errorCode, errorMessage);
            }
        });

    }

    private void openMain() {
        startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
        finish();
    }

}
