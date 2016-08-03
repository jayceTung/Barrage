package com.asuper.library;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joker on 2016/8/3.
 */
public class BarrageView extends LinearLayout {
    public static final String TAG = "BarrageView";

    private Context mContext;
    private RelativeLayout mFirLayout;
    private RelativeLayout mSecLayout;
    private TextView mFirView;
    private TextView mSecView;
    private static int SCREEN_WIDTH;

    private Queue<BarrageBean> mQueue;
    private TranslateAnimation mFirAnimation;
    private TranslateAnimation mSecAnimation;
    private boolean isFirDMFlag = true;//弹幕可以的标志(0,可以运动  1.不可以运动)
    private boolean isSecDMFlag = true;
    private Timer mTimer;
    private LinearLayout.LayoutParams params;

    private static final int MSG_RUN = 0x000001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RUN:
                    BarrageBean bean = (BarrageBean) msg.obj;
                    Log.i(TAG, "run === " + bean.getContent());
                    if (isFirDMFlag) {
                        mFirView.setText(bean.getContent());
                        mFirLayout.startAnimation(mFirAnimation);
                        break;
                    }
                    if (isSecDMFlag) {
                        mSecView.setText(bean.getContent());
                        mSecLayout.startAnimation(mSecAnimation);
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
    };



    public BarrageView(Context context) {
        this(context, null);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOrientation(VERTICAL);
        init();
    }

    private void init() {
        mQueue = new LinkedList<BarrageBean>();
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        mFirLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_barrage, null);
        mSecLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_barrage, null);
        mFirView = (TextView) mFirLayout.findViewById(R.id.content2);
        mSecView = (TextView) mSecLayout.findViewById(R.id.content2);
        mFirLayout.setVisibility(View.INVISIBLE);
        mSecLayout.setVisibility(View.INVISIBLE);
        addView(mFirLayout);
        addView(mSecLayout);
        mFirAnimation = new TranslateAnimation(SCREEN_WIDTH, -SCREEN_WIDTH, 0, 0);
        mFirAnimation.setDuration(4000);
        mFirAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isFirDMFlag = false;
                mFirLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isFirDMFlag = true;
                mFirLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSecAnimation = new TranslateAnimation(SCREEN_WIDTH, -SCREEN_WIDTH, 0, 0);
        mSecAnimation.setDuration(4000);
        mSecAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isSecDMFlag = false;
                mSecLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isSecDMFlag = true;
                mSecLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isFirDMFlag || isSecDMFlag) {
                    BarrageBean bean = mQueue.poll();
                    if (bean != null) {
                        Message message = mHandler.obtainMessage();
                        message.what = MSG_RUN;
                        message.obj = bean;
                        mHandler.sendMessage(message);
                    }
                }
            }
        }, 500, 500);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (getHeight() > 0 && params == null) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , getHeight() / 2);
            Log.i(TAG, "getHeight = " + getHeight());
            mFirLayout.setLayoutParams(params);
            mSecLayout.setLayoutParams(params);
        }
    }

    public void addBarrage(BarrageBean bean) {
        mQueue.offer(bean);
    }

    public void onDestory() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mQueue != null) {
            mQueue.clear();
            mQueue = null;
        }
        if (mContext != null) {
            mContext = null;
        }
        if (mFirAnimation != null) {
            mFirAnimation.cancel();
            mFirAnimation = null;
        }
        if (mSecAnimation != null) {
            mSecAnimation.cancel();
            mSecAnimation = null;
        }
    }

}
