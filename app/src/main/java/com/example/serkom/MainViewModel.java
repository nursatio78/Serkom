package com.example.serkom;

import androidx.lifecycle.ViewModel;

import com.example.serkom.callback.ActionListener;

public class MainViewModel extends ViewModel {

    private ActionListener actionListener;

    public MainViewModel(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
