package com.worldline.os.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> inputText = new MutableLiveData<>();

    public void setInputText(String text) {
        inputText.setValue(text);
    }

    public LiveData<String> getInputText() {
        return inputText;
    }
}