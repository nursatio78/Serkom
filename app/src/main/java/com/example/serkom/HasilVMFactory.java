package com.example.serkom;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.serkom.callback.ActionListener;

public class HasilVMFactory implements ViewModelProvider.Factory {
    private Context context;
    private ActionListener actionListener;

    public HasilVMFactory(ActionListener actionListener){
        this.actionListener = actionListener;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(actionListener);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
