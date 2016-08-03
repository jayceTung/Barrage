package com.asuper.barrage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.asuper.library.BarrageBean;
import com.asuper.library.BarrageView;

public class MainActivity extends AppCompatActivity {

    private BarrageView view;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (BarrageView) findViewById(R.id.View);
        textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.addBarrage(new BarrageBean(textView.getText().toString()));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view.onDestory();
    }
}
