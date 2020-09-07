package com.bignerdranch.minesweeper30.Activity;

import androidx.fragment.app.Fragment;
import com.bignerdranch.minesweeper30.Fragment.FieldFragment;

public class MainActivity extends SingleActivityFragment {

    @Override
    protected Fragment getFragment() {
        return new FieldFragment();
    }
}
