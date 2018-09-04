package vn.datnx.todolist.adapter.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class ViewHolderHelper extends RecyclerView.ViewHolder {

    public ViewHolderHelper(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
