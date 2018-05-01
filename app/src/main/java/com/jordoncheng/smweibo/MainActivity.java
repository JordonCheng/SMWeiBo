package com.jordoncheng.smweibo;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jordoncheng.smweibo.views.WBEditActivity;
import com.jordoncheng.smweibo.views.WBHomeActivity;
import com.jordoncheng.smweibo.models.AccessTokenKeeper;
import com.jordoncheng.smweibo.models.Constants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

public class MainActivity extends AppCompatActivity {

    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WbSdk.install(this, new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
        mSsoHandler = new SsoHandler(this);
        findViewById(R.id.authorize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new WbAuthListener() {
                    @Override
                    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                        AccessTokenKeeper.writeAccessToken(MainActivity.this, oauth2AccessToken);
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                        Snackbar.make(getWindow().getDecorView(), wbConnectErrorMessage.getErrorMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
        findViewById(R.id.status_button2).setOnClickListener(new LoginBottonListner());
        findViewById(R.id.status_button_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WBEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class LoginBottonListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, WBHomeActivity.class));
        }
    }
}
