package vn.datnx.todolist.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import vn.datnx.todolist.impl.OnShowToastListener;

public abstract class DialogBase extends DialogFragment {

    private OnShowToastListener onShowToastListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View rootView = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, rootView);
        initViews(rootView, savedInstanceState);
        initVariables(rootView, savedInstanceState);
        return rootView;
    }

    protected abstract int getLayoutResource();

    protected abstract void initViews(View rootView, Bundle savedInstanceState);

    protected abstract void initVariables(View rootView, Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onShowToastListener = (OnShowToastListener) context;
    }

    protected void showMessage(String message) {
        this.showMessage(message, Toast.LENGTH_SHORT);
    }

    protected void showMessage(String message, int duration) {
        onShowToastListener.onShowToast(message, duration);
    }
}
