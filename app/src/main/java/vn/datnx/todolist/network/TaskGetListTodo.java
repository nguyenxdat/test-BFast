package vn.datnx.todolist.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.datnx.todolist.model.ItemTodo;
import vn.datnx.todolist.network.helper.NetworkConstant;

public class TaskGetListTodo extends TaskNetworkBase<ArrayList<ItemTodo>> {

    public TaskGetListTodo(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<ItemTodo> genDataFromJSON(String data) throws JSONException {
        ArrayList<ItemTodo> listData = new ArrayList<>();
        JSONArray arrTodo = new JSONArray(data);
        for (int i = 0; i < arrTodo.length(); i++) {
            JSONObject objTodo = arrTodo.optJSONObject(i);
            String title = objTodo.optString("title");
            boolean isComplete = objTodo.optBoolean("complete");
            ItemTodo itemTodo = new ItemTodo();
            itemTodo.setTitle(title);
            itemTodo.setComplete(isComplete);
            listData.add(itemTodo);
        }
        return listData;
    }

    @Override
    protected JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    protected String getUrl() {
        return String.format(NetworkConstant.API.LIST_TODO, getUserId());
    }

    @Override
    protected int getMethod() {
        return Request.Method.GET;
    }
}
