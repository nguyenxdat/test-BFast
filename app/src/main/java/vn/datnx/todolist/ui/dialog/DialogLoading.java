package vn.datnx.todolist.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import vn.datnx.todolist.R;

public class DialogLoading extends DialogBase {

    public static final String CONTENT_DIALOG = "CONTENT DIALOG";

    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.pro_bar)
    ProgressBar progressBar;

    @Override
    protected int getLayoutResource() {
        return R.layout.loading_dialog;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        String textContent = getArguments().getString(CONTENT_DIALOG, getString(R.string.loading));
        txtContent.setText(textContent);
        setCancelable(false);
    }

    @Override
    protected void initVariables(View rootView, Bundle savedInstanceState) {

    }
}
