package com.example.paintingsonline.Add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.RotateBitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoDialogueBox extends DialogFragment {
    private final int PICKFILE_REQUEST_CODE = 4321;
    List<byte[]> AllImages = new ArrayList<>();

    //ArrayList<Uri> AllImagesUri = new ArrayList<>() ;

    public interface OnPhotoSelectedListener
    {
        void getBytes(List<byte[]> imgBytes);
    }

    OnPhotoSelectedListener monPhotoSelectedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.imagedialogbox, container, false);

        TextView selectPhoto = view.findViewById(R.id.dialogChoosePhoto);

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select_pictures"), PICKFILE_REQUEST_CODE);
            }
        });

        return view;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /*Result when selecting a new image from memory*/
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            RotateBitmap rotateBitmap = new RotateBitmap();

            if (data.getClipData() == null)
            {
                try
                {
                    AllImages.add(getBytesFromBitmap(rotateBitmap.HandleSamplingAndRotationBitmap(getContext(), data.getData())));
                    monPhotoSelectedListener.getBytes(AllImages);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {

                for (int i = 0; i < data.getClipData().getItemCount(); i++)
                {

                    try
                    {
                        AllImages.add(getBytesFromBitmap(rotateBitmap.HandleSamplingAndRotationBitmap(getContext(), data.getClipData().getItemAt(i).getUri())));
                        monPhotoSelectedListener.getBytes(AllImages);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        getDialog().dismiss();
    }



    @Override
    public void onAttach(Context context)
    {
        try
        {
            monPhotoSelectedListener = (OnPhotoSelectedListener) getActivity();

        }catch (ClassCastException e)
        {

        }
        super.onAttach(context);

    }

}
