package com.qixiao.qxweblib.actionbar;


import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiao.qxweblib.R;

/**
 * Created by liu jun on 2017/5/12.
 */

public class ActionbarManager{

    private static final String TAG = ActionbarManager.class.getSimpleName();

    private RelativeLayout toolbar;
    private ProgressBar progressBar;
    private Context context;
    private TextView title;

    private static final ActionbarManager actionbarManager = new ActionbarManager();

    public static ActionbarManager getInstance(){
        return actionbarManager;
    }

    public void init(Context context, RelativeLayout toolbar, View.OnClickListener onClickListener){
        this.context = context;
        this.toolbar = toolbar;
        progressBar = (ProgressBar) toolbar.findViewById(R.id.web_progressbar);
        title = (TextView) toolbar.findViewById(R.id.title);

        toolbar.findViewById(R.id.toolbar_back).setOnClickListener(onClickListener);
        toolbar.findViewById(R.id.toolbar_refresh).setOnClickListener(onClickListener);
    }

    public void setProgressBarVisible(boolean visible){
        if(visible){
            Animation showAction = new AlphaAnimation(0, 1);
            showAction.setDuration(200);
            progressBar.startAnimation(showAction);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Animation hiddenAction = new AlphaAnimation(1, 0);
            hiddenAction.setDuration(500);
            progressBar.startAnimation(hiddenAction);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setProgress(int progress){
        if(progress > progressBar.getProgress() && progress <= 100) {
            progressBar.setProgress(progress);
        }

    }

    public void setTitle(CharSequence sequence){
        title.setText(sequence);
    }

}
