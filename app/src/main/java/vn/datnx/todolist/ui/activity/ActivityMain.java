package vn.datnx.todolist.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import vn.datnx.todolist.R;
import vn.datnx.todolist.adapter.AdapterListTodo;
import vn.datnx.todolist.model.ItemTodo;
import vn.datnx.todolist.network.TaskCreateNewTodo;
import vn.datnx.todolist.network.TaskGetListTodo;
import vn.datnx.todolist.network.TaskNetworkBase;
import vn.datnx.todolist.ui.dialog.DialogCreateTodo;
import vn.datnx.todolist.utils.DividerItemDecoration;
import vn.datnx.todolist.utils.ItemTouchHelperCallback;

public class ActivityMain extends ActivityBaseWithActionBar
        implements DialogCreateTodo.OnCreateNewTodoListener {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.txt_number)
    TextView txtNumber;
    @BindView(R.id.img_profile)
    ImageView imgProfile;

    private AdapterListTodo adapterListTodo;
    private ItemTouchHelperExtension itemTouchHelper;
    private ItemTouchHelperExtension.Callback callback;

    private View.OnClickListener onAddMoreTodoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogCreateTodo();
        }
    };
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getListTodo();
        }
    };

    @Override
    protected int getLayoutView() {
        return R.layout.main_activity;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitleActionBar(getString(R.string.title_todo_list));
        adapterListTodo = new AdapterListTodo(getApplicationContext(), new ArrayList<ItemTodo>());
        recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycleView.setAdapter(adapterListTodo);
        recycleView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        callback = new ItemTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(recycleView);
        adapterListTodo.setItemTouchHelperExtension(itemTouchHelper);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        getListTodo();
        imgActionRight.setOnClickListener(onAddMoreTodoListener);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    @OnClick(R.id.img_profile)
    void onOpenProfileListener() {

    }

    private void getListTodo() {
        showLoading();
        TaskGetListTodo taskGetListTodo = new TaskGetListTodo(getApplicationContext());
        taskGetListTodo.request(new Response.Listener<ArrayList<ItemTodo>>() {
            @Override
            public void onResponse(ArrayList<ItemTodo> response) {
                closeLoading();
                swipeRefreshLayout.setRefreshing(false);
                updateRecycleView(response);
                updateTextView();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                closeLoading();
                swipeRefreshLayout.setRefreshing(false);
                showMessageWith(errorCode, errorMessage);
            }
        });
    }

    private void updateTextView() {
        int numberComplete = 0;
        int numberUnComplete = 0;
        for (ItemTodo itemTodo : adapterListTodo.getListItem()) {
            if (itemTodo.isComplete()) {
                numberComplete ++;
            } else {
                numberUnComplete ++;
            }
        }
        txtNumber.setText(numberUnComplete + "/" + numberComplete);
    }

    private void updateRecycleView(ArrayList<ItemTodo> listTodo) {
        adapterListTodo.addAllObject(listTodo);
    }

    private void showDialogCreateTodo() {
        DialogCreateTodo dialogCreateTodo = new DialogCreateTodo();
        dialogCreateTodo.show(getSupportFragmentManager(), DialogCreateTodo.class.getName());
    }

    private void createNewTodo(final ItemTodo itemTodo) {
        showLoading();
        TaskCreateNewTodo taskCreateNewTodo = new TaskCreateNewTodo(getApplicationContext(), itemTodo);
        taskCreateNewTodo.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                closeLoading();
                adapterListTodo.addObject(itemTodo);
                updateTextView();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                closeLoading();
                showMessageWith(errorCode, errorMessage);
            }
        });
    }

    @Override
    public void onCreateNewTodo(ItemTodo itemTodo) {
        createNewTodo(itemTodo);
    }
}
