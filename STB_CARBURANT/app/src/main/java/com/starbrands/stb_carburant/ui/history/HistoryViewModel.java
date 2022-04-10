package com.starbrands.stb_carburant.ui.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class HistoryViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> number;

    public MutableLiveData<String> getNumber() {
        Random random= new Random();

        if (number == null)
        {
            number=new MutableLiveData<>();

            number.setValue("le numero est "+(random.nextInt(10-1)+1));
        }
        else
            {
                number.setValue("le numero est "+(random.nextInt(10-1)+1));
            }
        return number;
    }
}