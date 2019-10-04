package com.setclapp.android_printers.common;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {

    private Boolean _isSucceed;
    private Exception _exception;
    private List<String> failureResults;

    private Object data;

    public Object getData() {
        return this.data;
    }

    public ActionResult() {

        failureResults = new ArrayList<>();

    }

    public Boolean isSucceed() {
        return _isSucceed;
    }

    public List<String> getFailureResult() {
        return failureResults;
    }

    public void setFailureResult(String... params) {
        for (String value : params)
            failureResults.add(value);
    }

    public Exception getException() {
        return _exception;
    }

    public static ActionResult failure(String... failureResult) {

        ActionResult actionResult = new ActionResult();

        failure(actionResult, failureResult);

        return actionResult;
    }

    public static ActionResultT failure(Exception exception) {

        ActionResultT result = new ActionResultT();

        failure(result, exception);

        return result;
    }

    protected static void success(ActionResult result)
    {
        result._isSucceed = true;
        result._exception=null;
    }

    public static ActionResult succeed() {

        ActionResult result = new ActionResult();

        succeed(result);

        return result;
    }

    protected static void succeed(ActionResult result) {
        result._isSucceed = true;
        result._exception = null;
    }

    protected static void failure(ActionResult result, String... failureResult) {

        result._isSucceed = false;
        result.setFailureResult(failureResult);
    }


    protected static void failure(ActionResult result, Exception exception) {

        result._isSucceed = false;
        result._exception = exception;

        if (exception != null)
            result.failureResults.add(exception.getMessage());
    }


}


