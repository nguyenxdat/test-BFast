package vn.datnx.todolist.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.datnx.todolist.network.helper.ExceptionConstant;
import vn.datnx.todolist.network.helper.VolleyErrorHelper;
import vn.datnx.todolist.network.helper.VolleySingleton;
import vn.datnx.todolist.utils.Constants;
import vn.datnx.todolist.utils.Logger;

import static android.content.Context.MODE_PRIVATE;

public abstract class TaskNetworkBase<RESULT_DATA extends Object> {

    private static final int NETWORK_TIME_OUT = 30000;
    private static Context context;
    private Request<RESULT_DATA> request;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TaskNetworkBase(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public final void request(final Response.Listener<RESULT_DATA> listener, final ErrorListener errorListener) {
        request = new Request<RESULT_DATA>(getMethod(), getUrl(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper volleyErrorHelper;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String errorData = new String(error.networkResponse.data);
                    Logger.e("errorData", errorData);
                    volleyErrorHelper = validData(errorData);
                    if (null != volleyErrorHelper) {
                        errorListener.onError(volleyErrorHelper.getErrorCode(), volleyErrorHelper.getErrorMessage());
                    } else {
                        errorListener.onError(error.networkResponse.statusCode, errorData);
                    }
                } else {
                    if (error instanceof VolleyErrorHelper) {
                        volleyErrorHelper = (VolleyErrorHelper) error;
                    } else {
                        volleyErrorHelper = new VolleyErrorHelper(error);
                    }
                    errorListener.onError(volleyErrorHelper.getErrorCode(), volleyErrorHelper.getErrorMessage());
                }
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-type", "application/json");
                params.put("Authorization", getAccessToken());
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = null;
                try {
                    jsonObject = genBodyParam();
                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyErrorHelper volleyErrorHepler = new VolleyErrorHelper(ExceptionConstant.PARSE_ERROR, e.getMessage());
                    Response.error(volleyErrorHepler);
                }

                if (null == jsonObject) {
                    return "".getBytes();
                } else {
                    return jsonObject.toString().getBytes();
                }
            }

            @Override
            protected Response<RESULT_DATA> parseNetworkResponse(NetworkResponse response) {
                String data = new String(response.data);
                Logger.e("---------data = ", data);
                RESULT_DATA result;
                VolleyErrorHelper volleyErrorHelper;
                try {
                    result = genDataFromJSON(data);
                    return Response.success(result, getCacheEntry());
                } catch (JSONException e) {
                    volleyErrorHelper = new VolleyErrorHelper(ExceptionConstant.PARSE_ERROR, e.getMessage());
                    return Response.error(volleyErrorHelper);
                }
            }

            @Override
            protected void deliverResponse(RESULT_DATA response) {
                listener.onResponse(response);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(NETWORK_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }


    private boolean hasValueForKey(JSONObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.isNull(key)) {
            return true;
        } else {
            return false;
        }
    }

    private VolleyErrorHelper validData(String json) {
        VolleyErrorHelper volleyErrorHelper;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (hasValueForKey(jsonObject, "error")) {
                JSONObject objError = jsonObject.optJSONObject("error");
                int statusCode = objError.optInt("statusCode");
                String errorMessage = objError.optString("message");
                volleyErrorHelper = new VolleyErrorHelper(statusCode, errorMessage);
            } else {
                volleyErrorHelper = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            volleyErrorHelper = new VolleyErrorHelper(ExceptionConstant.PARSE_ERROR, e.getMessage());
        }
        return volleyErrorHelper;
    }

    protected abstract RESULT_DATA genDataFromJSON(String data) throws JSONException;

    protected abstract JSONObject genBodyParam() throws JSONException;

    protected abstract String getUrl();

    protected abstract int getMethod();

    protected Context getContext() {
        return context;
    }

    protected void saveTokenAndId(String token, int userId) {
        editor.putString(Constants.ACCESS_TOKEN, token);
        editor.putInt(Constants.USER_ID, userId);
        editor.putBoolean(Constants.LOGIN_SUCCESS, true);
        editor.apply();
    }

    protected int getUserId() {
        return sharedPreferences.getInt(Constants.USER_ID, -1);
    }

    private String getAccessToken() {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
    }

    public interface ErrorListener {
        void onError(int errorCode, String errorMessage);
    }
}
