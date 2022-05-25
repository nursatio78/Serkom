package com.example.serkom.callback;

public interface ActionListener {
    void onStart();

    void onSuccess(String message);

    void onError(String message);
}
