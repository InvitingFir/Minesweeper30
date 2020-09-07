package com.bignerdranch.minesweeper30.Activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bignerdranch.minesweeper30.R;

public abstract class SingleActivityFragment extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if(f == null){
            fm.beginTransaction().add(R.id.fragment_container, getFragment()).commit();
        }
    }

    protected abstract Fragment getFragment();
}
