package com.bignerdranch.minesweeper30.Activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import com.bignerdranch.minesweeper30.Fragment.RecordTableFragment;

public class RecordTableActivity extends SingleActivityFragment{
    private static final String BOMBS_EXTRA = "bombs extra";
    private static final String FIELD_SIZE_EXTRA = "field size extra";


    public static Intent getIntent(Context context, int bombs, int fieldSize){
        Intent i = new Intent(context, RecordTableActivity.class);
        i.putExtra(BOMBS_EXTRA, bombs);
        i.putExtra(FIELD_SIZE_EXTRA, fieldSize);
        return i;
    }

    public Fragment getFragment() {
        Intent i = getIntent();
        int bombs = i.getIntExtra(BOMBS_EXTRA, 0);
        int fieldSize = i.getIntExtra(FIELD_SIZE_EXTRA, 0);
        return RecordTableFragment.getInstance(bombs, fieldSize);
    }
}
