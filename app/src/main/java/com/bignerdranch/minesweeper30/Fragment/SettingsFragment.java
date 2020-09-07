package com.bignerdranch.minesweeper30.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bignerdranch.minesweeper30.Activity.RecordTableActivity;
import com.bignerdranch.minesweeper30.R;

public class SettingsFragment extends Fragment {
    private static final String CUSTOM_SETTINGS = "Custom settings";
    private static final int CUSTOM_SETTINGS_REQUEST_CODE = 0;
    private static final String ARGS_NUM_OF_BOMBS = "args num of bombs";
    private static final String ARGS_FIELD_SIZE = "args field size";
    public static final String RESULT_FIELD_SIZE = "result field size";
    public static final String RESULT_NUM_OFF_BOMBS = "result num of bombs";
    public static final int MIN_FIELD_SIZE = 2;
    public static final int MAX_FIELD_SIZE = 10;
    public static final int MIN_NUM_OF_BOMBS = 0;
    public static final int MAX_NUM_OF_BOMBS = 100;
    private int mCurrentFieldSize;
    private int mCurrentNumOfBombs;
    private TextView mNumOfBombsTextView;
    private TextView mFieldSizeTextView;
    private Button mHowToPlayButton;

    private SettingsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        mCurrentNumOfBombs = args.getInt(ARGS_NUM_OF_BOMBS, MIN_NUM_OF_BOMBS);
        mCurrentFieldSize = args.getInt(ARGS_FIELD_SIZE, MIN_FIELD_SIZE);
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        viewInit(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.credits:
                Toast.makeText(this.getContext(), R.string.credits_toast, Toast.LENGTH_LONG).show();
                return true;
            case R.id.record_table_item:
                Intent i = RecordTableActivity.getIntent(this.getActivity(), mCurrentNumOfBombs, mCurrentFieldSize);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setResult() {
        Intent i = new Intent();
        i.putExtra(RESULT_FIELD_SIZE, mCurrentFieldSize);
        i.putExtra(RESULT_NUM_OFF_BOMBS, mCurrentNumOfBombs);
        this.getActivity().setResult(Activity.RESULT_OK, i);
    }

    public static Fragment getInstance(int numOfBombs, int fieldSize) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_FIELD_SIZE, fieldSize);
        bundle.putInt(ARGS_NUM_OF_BOMBS, numOfBombs);
        SettingsFragment sf = new SettingsFragment();
        sf.setArguments(bundle);
        return sf;
    }

    private void viewInit(View view){
        mHowToPlayButton = view.findViewById(R.id.how_to_play);
        mHowToPlayButton.setOnClickListener(l->{
            Uri gameRules = Uri.parse("https://mmorpg.one/kak-igrat-v-sapera.html");
            Intent i = new Intent(Intent.ACTION_VIEW, gameRules);
            startActivity(i);
        });

        Button hardSettingsButton = view.findViewById(R.id.hard_settings_button);
        hardSettingsButton.setOnClickListener(l ->{
            updateBombsNSize(35, 10);
        });

        Button mediumSettingsButton = view.findViewById(R.id.medium_settings_button);
        mediumSettingsButton.setOnClickListener(l ->{
            updateBombsNSize(20, 10);
        });

        Button easySettingsButton = view.findViewById(R.id.easy_settings_button);
        easySettingsButton.setOnClickListener(l ->{
            updateBombsNSize(10, 10);
        });

        Button customSettingsButton = view.findViewById(R.id.custom_settings_button);
        customSettingsButton.setOnClickListener(l ->{
            CustomSettingsFragment csf = CustomSettingsFragment.getInstance(mCurrentNumOfBombs, mCurrentFieldSize);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            csf.setTargetFragment(this, CUSTOM_SETTINGS_REQUEST_CODE);
            csf.show(fm, CUSTOM_SETTINGS);
        });

        mFieldSizeTextView = view.findViewById(R.id.field_settings_text_view);
        mNumOfBombsTextView = view.findViewById(R.id.bombs_settings_text_view);
        updateSettingsTextView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CUSTOM_SETTINGS_REQUEST_CODE)
            if(resultCode == Activity.RESULT_OK){
                int a = data.getIntExtra(CustomSettingsFragment.EXTRA_NUM_OF_BOMBS, MIN_NUM_OF_BOMBS);
                int b = data.getIntExtra(CustomSettingsFragment.EXTRA_FIELD_SIZE, MIN_FIELD_SIZE);
                updateBombsNSize(a, b);
            }
    }

    private void updateBombsNSize(int bombs, int fieldSize){
        mCurrentFieldSize = fieldSize;
        mCurrentNumOfBombs = bombs;
        setResult();
        updateSettingsTextView();
    }

    private void updateSettingsTextView(){
        String bombs = getResources().getQuantityString(R.plurals.num_of_bombs_settings, mCurrentNumOfBombs, mCurrentNumOfBombs, mCurrentNumOfBombs, mCurrentNumOfBombs);
        String size = getResources().getQuantityString(R.plurals.field_size_settings, mCurrentFieldSize, mCurrentFieldSize, mCurrentFieldSize, mCurrentFieldSize);
        mNumOfBombsTextView.setText(bombs);
        mFieldSizeTextView.setText(size);
    }
}
