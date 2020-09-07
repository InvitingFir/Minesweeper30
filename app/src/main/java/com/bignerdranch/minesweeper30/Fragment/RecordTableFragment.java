package com.bignerdranch.minesweeper30.Fragment;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bignerdranch.minesweeper30.R;
import com.bignerdranch.minesweeper30.data.Record;
import com.bignerdranch.minesweeper30.data.RecordManager;

import java.io.File;
import java.util.List;

public class RecordTableFragment extends Fragment {
    private static final String BOMBS_ARGS = "bombs args";
    private static final String FIELD_SIZE_ARGS = "field size args";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_IMAGE = 0;
    private RecyclerView.Adapter<RecordViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private List<Record> mRecords;
    private TextView mNoRecordsTextView;


    private RecordTableFragment(){}

    public static RecordTableFragment getInstance(int bombs, int fieldSize){
        RecordTableFragment fragment = new RecordTableFragment();
        Bundle b = new Bundle();
        b.putInt(BOMBS_ARGS, bombs);
        b.putInt(FIELD_SIZE_ARGS, fieldSize);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        int bombs = b.getInt(BOMBS_ARGS);
        int fieldSize = b.getInt(FIELD_SIZE_ARGS);
        RecordManager manager = RecordManager.getInstance(getContext());
        mRecords = manager.getRecords(bombs, fieldSize);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_record_table, container, false);
        mRecyclerView = view.findViewById(R.id.record_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new RecordAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mNoRecordsTextView = view.findViewById(R.id.no_records_textview);
        if(mRecords.size() > 0) mNoRecordsTextView.setVisibility(View.INVISIBLE);
        return view;
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder>{

        @NonNull
        @Override
        public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new RecordViewHolder(inflater, R.layout.list_item_record, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
            Record r = mRecords.get(position);
            holder.bind(r);
        }

        @Override
        public int getItemCount() {
            return mRecords.size();
        }
    }

    private class RecordViewHolder extends RecyclerView.ViewHolder{
        private TextView mTime;
        private TextView mDate;
        private FrameLayout mFrameLayout;
        private Record mRecord;

        public RecordViewHolder(LayoutInflater inflater, int id, ViewGroup parent){
            super(inflater.inflate(id, parent, false));
            mFrameLayout = itemView.findViewById(R.id.list_item_layout);
            mFrameLayout.setOnClickListener(l ->{
                File file = RecordManager.getInstance(getContext()).getImageFile(mRecord);
                RecordImageFragment fragment = RecordImageFragment.getInstance(file.getPath());
                fragment.show(getFragmentManager(), DIALOG_IMAGE);
            });
            mTime = itemView.findViewById(R.id.time_textview);
            mDate = itemView.findViewById(R.id.date_textview);
        }

        public void bind(Record record){
            mRecord = record;
            String timeString = getResources().getString(R.string.time);
            String time = record.getTime()/1000 + "." + record.getTime()%1000;
            String dateString = getResources().getString(R.string.date);
            String date = DateFormat.format("dd.MM.yyyy", record.getDate()).toString();
            mTime.setText(timeString + time);
            mDate.setText(dateString + date);
        }


    }

}
