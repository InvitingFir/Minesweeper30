package com.bignerdranch.minesweeper30.Activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import com.bignerdranch.minesweeper30.Fragment.SettingsFragment;

public class SettingsActivity extends SingleActivityFragment {
    private static final String EXTRA_NUM_OF_BOMBS = "Num of bombs";
    private static final String EXTRA_FIELD_SIZE = "Field size";

    public Fragment getFragment(){
        int fieldSize = this.getIntent().getIntExtra(EXTRA_FIELD_SIZE, 0);
        int numOfBombs = this.getIntent().getIntExtra(EXTRA_NUM_OF_BOMBS, 0);
        return SettingsFragment.getInstance(numOfBombs, fieldSize);
    }

    public static Intent getIntent(Context context, int numOfBombs, int fieldSize){
        Intent i = new Intent(context, SettingsActivity.class);
        i.putExtra(EXTRA_NUM_OF_BOMBS, numOfBombs);
        i.putExtra(EXTRA_FIELD_SIZE, fieldSize);
        return i;
    }
}
