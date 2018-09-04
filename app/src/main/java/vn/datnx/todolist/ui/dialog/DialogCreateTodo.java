package vn.datnx.todolist.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import vn.datnx.todolist.R;
import vn.datnx.todolist.model.ItemTodo;

public class DialogCreateTodo extends DialogBase {

    @BindView(R.id.edt_todo_name)
    EditText edtTodoName;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_save)
    Button btnSave;

    private OnCreateNewTodoListener onCreateNewTodoListener;

    @Override
    protected int getLayoutResource() {
        return R.layout.create_todo_dialog;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {

    }

    @Override
    protected void initVariables(View rootView, Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_cancel)
    void onCancelClickListener() {
        dismiss();
    }

    @OnClick(R.id.btn_save)
    void onSaveClickListener() {
        if (checkValidate()) {
            ItemTodo itemTodo = new ItemTodo();
            itemTodo.setTitle(edtTodoName.getText().toString());
            onCreateNewTodoListener.onCreateNewTodo(itemTodo);
            dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onCreateNewTodoListener = (OnCreateNewTodoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCreateNewTodoListener");
        }

    }

    private boolean checkValidate() {
        if (TextUtils.isEmpty(edtTodoName.getText())) {
            showMessage(getString(R.string.error_todo_name_not_null));
            return false;
        }
        return true;
    }

    public interface OnCreateNewTodoListener {
        void onCreateNewTodo(ItemTodo itemTodo);
    }

}
