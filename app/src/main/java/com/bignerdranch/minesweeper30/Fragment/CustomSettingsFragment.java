package com.bignerdranch.minesweeper30.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bignerdranch.minesweeper30.R;

public class CustomSettingsFragment extends DialogFragment {
    public static final String ARGS_NUM_OF_BOMBS = "Args_num_of_bombs";
    public static final String ARGS_FIELD_SIZE = "Args_field_size";
    public static final String EXTRA_NUM_OF_BOMBS = "Extra_num_of_bombs";
    public static final String EXTRA_FIELD_SIZE = "Extra_field_size";


    private int mCurrentBombs;
    private int mCurrentFieldSize;
    private NumberPicker mBombsPicker;
    private NumberPicker mFieldSizePicker;

    private CustomSettingsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        mCurrentBombs = b.getInt(ARGS_NUM_OF_BOMBS);
        mCurrentFieldSize = b.getInt(ARGS_FIELD_SIZE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings_count, container, false);
        initView(v);
        return v;
    }

    private void initView(View v){
        mBombsPicker = v.findViewById(R.id.bombs_number_picker);
        mBombsPicker.setMaxValue(SettingsFragment.MAX_NUM_OF_BOMBS);
        mBombsPicker.setMinValue(SettingsFragment.MIN_NUM_OF_BOMBS);
        mBombsPicker.setValue(mCurrentBombs);

        mFieldSizePicker = v.findViewById(R.id.field_size_number_picker);
        mFieldSizePicker.setMinValue(SettingsFragment.MIN_FIELD_SIZE);
        mFieldSizePicker.setMaxValue(SettingsFragment.MAX_FIELD_SIZE);
        mFieldSizePicker.setValue(mCurrentFieldSize);

        Button OKButton = v.findViewById(R.id.ok_settings_button);
        OKButton.setOnClickListener(l->setResult());
    }

    public void setResult(){
        mCurrentBombs = mBombsPicker.getValue();
        mCurrentFieldSize = mFieldSizePicker.getValue();
        if(mCurrentFieldSize*mCurrentFieldSize> mCurrentBombs) {
            Intent i = new Intent();
            i.putExtra(EXTRA_NUM_OF_BOMBS, mCurrentBombs);
            i.putExtra(EXTRA_FIELD_SIZE, mCurrentFieldSize);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            this.onStop();
        }
        else {
            Toast.makeText(this.getContext(), R.string.num_of_bombs_error_toast, Toast.LENGTH_LONG).show();
        }
    }

    public static CustomSettingsFragment getInstance(int bombs, int fieldSize){
        CustomSettingsFragment csf = new CustomSettingsFragment();
        Bundle b = new Bundle();
        b.putInt(ARGS_FIELD_SIZE, fieldSize);
        b.putInt(ARGS_NUM_OF_BOMBS, bombs);
        csf.setArguments(b);
        return csf;
    }
}
