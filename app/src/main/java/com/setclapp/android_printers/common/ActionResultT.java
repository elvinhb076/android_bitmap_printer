package com.setclapp.android_printers.common;

public class ActionResultT<T> extends ActionResult {

    private T data;

    public T getData() {
        return data;
    }

    public static <T> ActionResultT<T> succeed(T value) {

        ActionResultT<T> result = new ActionResultT<>();

        result.data = value;

        success(result);

        return result;
    }
}
