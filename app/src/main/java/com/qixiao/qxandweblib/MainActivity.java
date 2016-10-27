package com.qixiao.qxandweblib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qixiao.qxweblib.activitys.WebActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_web:
                openWeb();
                break;
        }
    }

    private void openWeb() {
        WebActivity.start(this, "https://github.com", "github");
    }


}
