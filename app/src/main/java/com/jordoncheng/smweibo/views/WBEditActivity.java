package com.jordoncheng.smweibo.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jordoncheng.smweibo.controlers.WBDetailCtrlActivity;
import com.jordoncheng.smweibo.R;

/**
 * Created by Administrator on 3/28/2018.
 */

public class WBEditActivity extends WBDetailCtrlActivity {

    private final static int SELECT_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        //对状态栏有影响
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void getImage(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent2.setType("image/*");
        Bundle request = new Bundle();
        request.putBoolean("return-data", true);
        intent2.putExtras(request);
        startActivityForResult(intent2, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            ImageView iv = findViewById(R.id.image);
            iv.setImageURI(data.getData());
        }
    }

    public void exit(View view) {
        finish();
    }

    public void intercept(View view) {
    }
}
