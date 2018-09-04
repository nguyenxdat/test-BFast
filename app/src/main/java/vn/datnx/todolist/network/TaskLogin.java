package vn.datnx.todolist.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import vn.datnx.todolist.network.helper.NetworkConstant;

public class TaskLogin extends TaskNetworkBase<Boolean> {

    private String email;
    private String password;

    public TaskLogin(Context context, String email, String password) {
        super(context);
        this.email = email;
        this.password = password;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        String accessToken = jsonObject.optString("id");
        int userID = jsonObject.optInt("userId");
        saveTokenAndId(accessToken, userID);
        return Boolean.TRUE;
    }

    @Override
    protected JSONObject genBodyParam() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        return jsonObject;
    }

    @Override
    protected String getUrl() {
        return NetworkConstant.API.LOGIN;
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }
}
