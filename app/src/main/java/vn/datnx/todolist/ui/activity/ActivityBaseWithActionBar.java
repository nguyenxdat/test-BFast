package vn.datnx.todolist.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import vn.datnx.todolist.R;

public abstract class ActivityBaseWithActionBar extends ActivityBase {

    @BindView(R.id.txt_title_action_bar)
    TextView txtTitleActionBar;
    @BindView(R.id.img_action_right)
    ImageView imgActionRight;

    protected void setTitleActionBar(String title) {
        txtTitleActionBar.setText(title);
    }

}
