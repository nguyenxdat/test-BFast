package vn.datnx.todolist.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import vn.datnx.todolist.model.ItemTodo;
import vn.datnx.todolist.network.helper.NetworkConstant;

public class TaskCreateNewTodo extends TaskNetworkBase<Boolean> {

    private ItemTodo itemTodo;

    public TaskCreateNewTodo(Context context, ItemTodo itemTodo) {
        super(context);
        this.itemTodo = itemTodo;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        return Boolean.TRUE;
    }

    @Override
    protected JSONObject genBodyParam() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", itemTodo.getTitle());
        jsonObject.put("complete", itemTodo.isComplete());
        jsonObject.put("accountId", getUserId());
        return jsonObject;
    }

    @Override
    protected String getUrl() {
        return String.format(NetworkConstant.API.CREATE_TODO, getUserId());
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }
}
