package vn.datnx.todolist.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.ButterKnife;
import vn.datnx.todolist.R;
import vn.datnx.todolist.impl.OnShowToastListener;
import vn.datnx.todolist.ui.dialog.DialogLoading;

public abstract class ActivityBase extends AppCompatActivity implements OnShowToastListener {

    private DialogLoading dialogLoading;
    private String currentContentLoading;
    private boolean isActivityPaused;
    private boolean isDialogShowing;
    private boolean isInterrupted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        ButterKnife.bind(this);
        initViews(savedInstanceState);
        initVariables(savedInstanceState);
    }

    protected abstract int getLayoutView();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initVariables(Bundle savedInstanceState);

    protected void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    protected void showToast(String message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    public void showMessageWith(int errorCode, String message) {
        showToast(message);
    }

    public void showLoading() {
        this.showLoading(getString(R.string.loading));
    }

    public void showLoading(String text) {
        currentContentLoading = text;
        if (isDialogShowing) {
            return;
        }
        isDialogShowing = true;
        if (isActivityPaused) {
            isInterrupted = true;
        } else {
            isInterrupted = false;
            dialogLoading = new DialogLoading();
            Bundle bundle = new Bundle();
            bundle.putString(DialogLoading.CONTENT_DIALOG, currentContentLoading);
            dialogLoading.setArguments(bundle);
            dialogLoading.show(getSupportFragmentManager(), DialogLoading.class.getName());
        }
    }

    public void closeLoading() {
        isInterrupted = false;
        isDialogShowing = false;
        if (null != dialogLoading) {
            dialogLoading.dismiss();
            dialogLoading = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityPaused = true;
        if (null != dialogLoading && isDialogShowing) {
            isInterrupted = true;
            dialogLoading.dismiss();
            dialogLoading = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityPaused = false;
        if (isDialogShowing && isInterrupted) {
            isInterrupted = false;
            isDialogShowing = false;
            showLoading(currentContentLoading);
        }
    }

    @Override
    public void onShowToast(String message, int duration) {
        showToast(message, duration);
    }
}
