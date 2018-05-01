package com.jordoncheng.smweibo.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jordoncheng.smweibo.controlers.LoadImageCtrl;
import com.jordoncheng.smweibo.controlers.WBBaseCtrlAcitivity;
import com.jordoncheng.smweibo.controlers.WBStatusesCtrlActivity;
import com.jordoncheng.smweibo.R;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by Administrator on 3/21/2018.
 */

public class WBUserPopupActivity extends WBBaseCtrlAcitivity {

    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_popup);
        //对状态栏有影响
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mUser = getIntent().getBundleExtra("user").getParcelable("user");
        loadImage(mUser.avatar_large, 0, new LoadImageCtrl.LoadImageListener() {
            @Override
            public void onLoadImageSuccee(Bitmap bm, int tag) {
                ((StatusAvatarImageView)findViewById(R.id.list_item_avatar)).setImageBitmap(bm);
            }

            @Override
            public void onLoadImageErrer(String errer, int tag) {

            }
        });
        TextView sreenNmae = findViewById(R.id.list_item_status_screen_name);
        TextView name = findViewById(R.id.list_item_status_name);
        TextView followersCount = findViewById(R.id.followers_count);
        TextView friendsCount = findViewById(R.id.friends_count);
        TextView statusesCount = findViewById(R.id.statuses_count);
        TextView favouritesCount = findViewById(R.id.favourites_count);

        sreenNmae.setText("@"+mUser.screen_name);
        name.setText(mUser.name);
        statusesCount.setText(String.valueOf(mUser.statuses_count));
        followersCount.setText(String.valueOf(mUser.followers_count));
        friendsCount.setText(String.valueOf(mUser.friends_count));
        favouritesCount.setText(String.valueOf(mUser.favourites_count));
    }

    public void intercept(View view) {
    }

    public void exit(View view) {
        finish();
    }
}
