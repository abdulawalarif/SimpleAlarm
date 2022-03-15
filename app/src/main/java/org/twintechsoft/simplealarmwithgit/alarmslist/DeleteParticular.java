package org.twintechsoft.simplealarmwithgit.alarmslist;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.twintechsoft.simplealarmwithgit.createalarm.CreateAlarmViewModel;

public class DeleteParticular extends ViewModel {

    Application application;


    private CreateAlarmViewModel createAlarmViewModel = new ViewModelProvider((ViewModelStoreOwner) application).get(CreateAlarmViewModel.class);

    public void deletesingle(int id){
    createAlarmViewModel.delete(id);
}


    //mViewModel = new ViewModelProvider(this).get(MyViewModel.class);
    //mViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
    //mViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
    //mViewModel = new ViewModelProvider(FirstFragment.class).get(MyViewModel.class);

}
