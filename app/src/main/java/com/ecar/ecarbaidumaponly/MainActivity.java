package com.ecar.ecarbaidumaponly;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ecar.baidumaplib.maputil.MapUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.baidu.mapapi.model.LatLng pt1 = new com.baidu.mapapi.model.LatLng(22.573328, 113.874083);
                com.baidu.mapapi.model.LatLng pt2 = new com.baidu.mapapi.model.LatLng(22.621644, 114.130809);


                //吊起百度导航的方法
                MapUtil.openNavigation(pt1,pt2,"广东省深圳市宝安区宝源路","深圳市宝安区西乡街道永丰综合楼",MainActivity.this);
            }
        });
    }
}
