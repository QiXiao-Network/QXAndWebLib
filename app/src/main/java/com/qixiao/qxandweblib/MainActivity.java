package com.qixiao.qxandweblib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.qixiao.qxweblib.activitys.WebActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) this.findViewById(R.id.et_url);
        editText.setText("http://app.qq.com");
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
        String url = editText.getText().toString();
        WebActivity.start(this, url, "正在打开链接");
    }


}
