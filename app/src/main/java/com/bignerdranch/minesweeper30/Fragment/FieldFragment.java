package com.bignerdranch.minesweeper30.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bignerdranch.minesweeper30.Activity.SettingsActivity;
import com.bignerdranch.minesweeper30.FieldCell;
import com.bignerdranch.minesweeper30.R;
import com.bignerdranch.minesweeper30.Util.PictureUtil;
import com.bignerdranch.minesweeper30.data.Record;
import com.bignerdranch.minesweeper30.data.RecordManager;

public class FieldFragment extends Fragment implements Runnable {
    private static final String MSG_TIME = "msg time";
    public static final int FIELD_SIZE = 10;
    public static final int NUM_OF_BOMBS = 20;
    public static final int SETTINGS_REQUEST_CODE = 1;

    private FieldCell[][] mButtons;
    private int mFieldSize = FIELD_SIZE;
    private int mNumOfBombs = NUM_OF_BOMBS;
    private int mBombsLeft = NUM_OF_BOMBS;
    private int mFlagsLeft = NUM_OF_BOMBS;

    private int mTime;
    private Thread mTimer;
    private Handler mThreadHandler;

    private boolean mFieldIsGenerated;
    private boolean isGameOver;

    private Button mRestartButton;
    private TextView mTimerTextView;
    private TextView mScoreView;
    private GridLayout mGridLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        mThreadHandler = new TimerHandler();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                mFieldSize = data.getIntExtra(SettingsFragment.RESULT_FIELD_SIZE, 0);
                mNumOfBombs = data.getIntExtra(SettingsFragment.RESULT_NUM_OFF_BOMBS, 0);
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                nullTheCells();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nullTheCells();
        updateBombTextView();
    }

    @Override
    public void onPause() {
        super.onPause();
        isGameOver = true;
        openAllCells();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_field, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings_menu:
                Intent i = SettingsActivity.getIntent(this.getActivity(), mNumOfBombs, mFieldSize);
                startActivityForResult(i, SETTINGS_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        Bundle b;
        Message message;
        while(!isGameOver){
            message = new Message();
            b = new Bundle();
            mTime = (int)(System.currentTimeMillis() - time);
            b.putInt(MSG_TIME, mTime);
            message.setData(b);
            mThreadHandler.sendMessage(message);
            try {
                Thread.sleep(50);
            }catch (Exception e){e.printStackTrace();
            }
        }
    }

    private void init(View view){
        mRestartButton = view.findViewById(R.id.restart_button);
        mRestartButton.setOnClickListener(l -> {
            isGameOver = true;
            nullTheCells();
            updateTimerTextView(0);
        });

        mTimerTextView = view.findViewById(R.id.timer_textview);
        updateTimerTextView(mTime);

        mScoreView = view.findViewById(R.id.score_textview);
        updateBombTextView();

        mButtons = new FieldCell[mFieldSize][mFieldSize];
        mGridLayout = view.findViewById(R.id.field_layout);
        mGridLayout.setColumnCount(mFieldSize);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < mFieldSize; i++) {
            for (int j = 0; j < mFieldSize; j++) {
                Button b = (Button) inflater.inflate(R.layout.field_button, mGridLayout, false);
                mButtons[i][j] = new FieldCell(b);
                int finalI = i;
                int finalJ = j;
                mButtons[i][j].setOnClickListener(v->{
                    if(!mFieldIsGenerated) {
                        generateGame(finalI, finalJ);
                        isGameOver = false;
                        mTimer = new Thread(this);
                        mTimer.start();
                    }
                    if(!mButtons[finalI][finalJ].isOpened()) openCell(finalI, finalJ);
                });
                mButtons[i][j].setOnLongClickListener(v -> {
                    setFlag(finalI, finalJ);
                    return true;
                });
                mGridLayout.addView(mButtons[i][j].getButton());
            }
        }
    }

    private void nullTheCells(){
        mFieldIsGenerated = false;
        for (int i = 0; i < mFieldSize; i++) {
            for (int j = 0; j < mFieldSize; j++) {
                mButtons[i][j].nullTheCell();
            }
        }
        updateBombTextView();
    }

    private void generateGame(int i, int j){
        int x;
        int y;
        mFlagsLeft = mNumOfBombs;
        mBombsLeft = mNumOfBombs;
        for (int k = 0; k < mNumOfBombs; k++) {
            do{
                x = (int) (Math.random() * mFieldSize);
                y = (int) (Math.random() * mFieldSize);
            } while(mButtons[x][y].isBomb() ||( x==i && y==j));
            placeBomb(x, y);
        }
        mFieldIsGenerated = true;
        updateBombTextView();
    }

    private void placeBomb(int x, int y) {
        mButtons[x][y].setBomb();
        if (y == 0) {
            if (x == 0) {
                mButtons[x + 1][y].incrementValue();
                mButtons[x + 1][y + 1].incrementValue();
                mButtons[x][y + 1].incrementValue();
            } else if (x == mFieldSize - 1) {
                mButtons[x][y + 1].incrementValue();
                mButtons[x - 1][y + 1].incrementValue();
                mButtons[x - 1][y].incrementValue();
            } else {
                mButtons[x + 1][y].incrementValue();
                mButtons[x + 1][y + 1].incrementValue();
                mButtons[x][y + 1].incrementValue();
                mButtons[x - 1][y + 1].incrementValue();
                mButtons[x - 1][y].incrementValue();
            }
        } else if (y == mFieldSize - 1) {
            if (x == 0) {
                mButtons[x][y - 1].incrementValue();
                mButtons[x + 1][y - 1].incrementValue();
                mButtons[x + 1][y].incrementValue();
            } else if (x == mFieldSize - 1) {
                mButtons[x - 1][y].incrementValue();
                mButtons[x - 1][y - 1].incrementValue();
                mButtons[x][y - 1].incrementValue();
            } else {
                mButtons[x - 1][y].incrementValue();
                mButtons[x - 1][y - 1].incrementValue();
                mButtons[x][y - 1].incrementValue();
                mButtons[x + 1][y - 1].incrementValue();
                mButtons[x + 1][y].incrementValue();
            }
        } else if (x % mFieldSize == 0) {
            mButtons[x][y - 1].incrementValue();
            mButtons[x + 1][y - 1].incrementValue();
            mButtons[x + 1][y].incrementValue();
            mButtons[x + 1][y + 1].incrementValue();
            mButtons[x][y + 1].incrementValue();
        } else if (x % (mFieldSize - 1) == 0) {
            mButtons[x][y + 1].incrementValue();
            mButtons[x - 1][y + 1].incrementValue();
            mButtons[x - 1][y].incrementValue();
            mButtons[x - 1][y - 1].incrementValue();
            mButtons[x][y - 1].incrementValue();
        } else {
            mButtons[x][y - 1].incrementValue();
            mButtons[x + 1][y - 1].incrementValue();
            mButtons[x + 1][y].incrementValue();
            mButtons[x + 1][y + 1].incrementValue();
            mButtons[x][y + 1].incrementValue();
            mButtons[x - 1][y + 1].incrementValue();
            mButtons[x - 1][y].incrementValue();
            mButtons[x - 1][y - 1].incrementValue();
        }
    }

    private void openCell(int i, int j){
        FieldCell cell = mButtons[i][j];
        if(!cell.isOpened() && !cell.isFlag()) {
            cell.setOpened();
            if(cell.isBomb()) {
                gameOver();
            }
            if (cell.getValue() == 0) {
                if (j == 0) {
                    if (i == 0) {
                        openCell(i + 1, j);
                        openCell(i + 1, j + 1);
                        openCell(i, j + 1);
                    } else if (i == mFieldSize - 1) {
                        openCell(i, j + 1);
                        openCell(i - 1, j + 1);
                        openCell(i - 1, j);
                    } else {
                        openCell(i + 1, j);
                        openCell(i + 1, j + 1);
                        openCell(i, j + 1);
                        openCell(i - 1, j + 1);
                        openCell(i - 1, j);
                    }
                } else if (j == mFieldSize - 1) {
                    if (i == 0) {
                        openCell(i, j - 1);
                        openCell(i + 1, j - 1);
                        openCell(i + 1, j);
                    } else if (i == mFieldSize - 1) {
                        openCell(i - 1, j);
                        openCell(i - 1, j - 1);
                        openCell(i, j - 1);
                    } else {
                        openCell(i - 1, j);
                        openCell(i - 1, j - 1);
                        openCell(i, j - 1);
                        openCell(i + 1, j - 1);
                        openCell(i + 1, j);
                    }
                } else if (i % mFieldSize == 0) {
                    openCell(i, j - 1);
                    openCell(i + 1, j - 1);
                    openCell(i + 1, j);
                    openCell(i + 1, j + 1);
                    openCell(i, j + 1);
                } else if (i % (mFieldSize - 1) == 0) {
                    openCell(i, j + 1);
                    openCell(i - 1, j + 1);
                    openCell(i - 1, j);
                    openCell(i - 1, j - 1);
                    openCell(i, j - 1);
                } else {
                    openCell(i, j - 1);
                    openCell(i + 1, j - 1);
                    openCell(i + 1, j);
                    openCell(i + 1, j + 1);
                    openCell(i, j + 1);
                    openCell(i - 1, j + 1);
                    openCell(i - 1, j);
                    openCell(i-1, j-1);
                }
            }
        }
    }

    private void setFlag(int i, int j){
        FieldCell cell = mButtons[i][j];
        if(cell.setFlag()){
            if(cell.isBomb())
                mBombsLeft--;
            mFlagsLeft--;
        }
        else{
            if(cell.isBomb())
                mBombsLeft++;
            mFlagsLeft++;
        }
        if(mFlagsLeft == 0) gameOver();
        updateBombTextView();
    }

    private void openAllCells(){
        for (int i = 0; i < mFieldSize; i++) {
            for (int j = 0; j < mFieldSize; j++) {
                mButtons[i][j].setOpened();
                mButtons[i][j].setClickable(false);
                mButtons[i][j].setLongClickable(false);
            }
        }
    }

    private void updateBombTextView(){
        String s = getResources().getString(R.string.bombs_left) + mFlagsLeft;
        mScoreView.setText(s);
    }

    private void updateTimerTextView(int time){
        String timeString = getString(R.string.time) + time/1000 + "." + time%1000;
        mTimerTextView.setText(timeString);
    }

    private void gameOver(){
        isGameOver = true;
        openAllCells();
        if(mFlagsLeft == 0 && mBombsLeft == 0){
            Bitmap image = PictureUtil.getBitmapFromView(mGridLayout);
            Record r = new Record(mTime, mNumOfBombs, mFieldSize, image);
            boolean newRecord = RecordManager.getInstance(getContext()).addRecord(r);
            if(newRecord) {
                Toast.makeText(getContext(), "New top 10 score!", Toast.LENGTH_SHORT).show();
            }
        }
        else Toast.makeText(getContext(), "Game over!", Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("HandlerLeak")
    private class TimerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            updateTimerTextView(mTime);
        }
    }
}