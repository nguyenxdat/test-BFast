package vn.datnx.todolist.network.helper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyErrorHelper extends VolleyError {

    private int errorCode;
    private String errorMessage = "";

    public VolleyErrorHelper(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public VolleyErrorHelper(VolleyError volleyError) {
        if (volleyError instanceof TimeoutError) {
            this.errorCode = ExceptionConstant.TIME_OUT;
            this.errorMessage = volleyError.getMessage();
        } else if (isServerProblem(volleyError)) {
            this.errorCode = volleyError.networkResponse.statusCode;
            this.errorMessage = volleyError.getMessage();
        } else if (isNetworkAvailable(volleyError)) {
            this.errorCode = ExceptionConstant.NO_NETWORK;
            this.errorMessage = volleyError.getMessage();
        } else {
            if (null != volleyError.networkResponse) {
                this.errorCode = volleyError.networkResponse.statusCode;
                this.errorMessage = volleyError.getClass().toString() +
                        (" /\n " + volleyError.getMessage() + " /\n " + volleyError.getCause().getCause().getMessage());
            } else {
                this.errorCode = ExceptionConstant.UNKNOWN_EXCEPTION;
                this.errorMessage = "";
            }
        }
    }

    private static boolean isServerProblem(VolleyError error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    private boolean isNetworkAvailable(VolleyError volleyError) {
        if (volleyError instanceof NetworkError || volleyError instanceof NoConnectionError)
            return true;
        else
            return false;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
