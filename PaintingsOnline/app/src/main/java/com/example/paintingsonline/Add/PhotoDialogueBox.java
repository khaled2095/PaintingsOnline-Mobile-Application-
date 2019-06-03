package com.example.paintingsonline.Add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paintingsonline.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class PhotoDialogueBox extends DialogFragment
{
    private static final int PICKFILE_REQUEST_CODE = 4321;
    // private static final int CAMERA_REQUEST_CODE = 1234;

    public interface OnPhotoSelectedListener
    {
        void getimagepath(Uri imagepath);
        void getBytes(byte[] imgBytes);
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        return view;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap)
    {
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
            Uri selectedimageURI = data.getData();
            try {
                final InputStream imageStream = getContext().getContentResolver().openInputStream(selectedimageURI);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                byte [] mUploadBytes = getBytesFromBitmap(selectedImage);

                monPhotoSelectedListener.getBytes(mUploadBytes);
                monPhotoSelectedListener.getimagepath(selectedimageURI);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getDialog().dismiss();
        }
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
