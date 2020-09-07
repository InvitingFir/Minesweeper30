package com.bignerdranch.minesweeper30.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bignerdranch.minesweeper30.R;

public class RecordImageFragment extends DialogFragment {
    private static final String ARG_IMAGE_NAME = "image name";

    private Bitmap mImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        String imageName = b.getString(ARG_IMAGE_NAME);
        mImage = BitmapFactory.decodeFile(imageName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_list_item_image, container, false);
        ImageView imageView = v.findViewById(R.id.record_image);
        imageView.setImageBitmap(mImage);

        Button exitButton = v.findViewById(R.id.record_image_button);
        exitButton.setOnClickListener(l->{
            onStop();}
        );
        return v;
    }

    public static RecordImageFragment getInstance(String imagePath){
        Bundle b = new Bundle();
        b.putString(ARG_IMAGE_NAME, imagePath);

        RecordImageFragment fragment = new RecordImageFragment();
        fragment.setArguments(b);
        return fragment;
    }
}
