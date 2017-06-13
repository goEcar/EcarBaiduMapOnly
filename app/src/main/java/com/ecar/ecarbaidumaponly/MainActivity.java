package com.ecar.ecarbaidumaponly;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.model.LatLng;
import com.ecar.baidumaplib.maputil.MapUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button auto_btn = (Button) findViewById(R.id.btn);
        Button baidu_btn = (Button) findViewById(R.id.btn_baidu);
        Button gaodu_btn = (Button) findViewById(R.id.btn_gaode);
        Button tengxun_btn = (Button) findViewById(R.id.btn_tengxun);
        //初始化安装状态

        baidu_btn.setText(baidu_btn.getText().toString() +
                (MapUtil.isBaiduInstalled() ? "(已安装)" : "(未安装)"));
        gaodu_btn.setText(gaodu_btn.getText().toString() +
                (MapUtil.isGaodeInstalled() ? "(已安装)" : "(未安装)"));
        tengxun_btn.setText(tengxun_btn.getText().toString() +
                (MapUtil.isTengxunInstalled() ? "(已安装)" : "(未安装)"));

        //点击事件
        auto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng pt1 = new LatLng(22.573328, 113.874083);
                LatLng pt2 = new LatLng(22.621644, 114.130809);

                //调起百度或高德导航的方法
                MapUtil.openNavigation(pt1, pt2, "广东省深圳市宝安区宝源路", "深圳市宝安区西乡街道永丰综合楼", MainActivity.this);
            }
        });
        baidu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng pt1 = new LatLng(22.573328, 113.874083);
                LatLng pt2 = new LatLng(22.621644, 114.130809);

                //调起百度导航的方法
                MapUtil.openBaiDuNavigation(pt1, pt2, "广东省深圳市宝安区宝源路", "深圳市宝安区西乡街道永丰综合楼", MainActivity.this);
            }
        });
        gaodu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng pt1 = new LatLng(22.573328, 113.874083);
                LatLng pt2 = new LatLng(22.621644, 114.130809);

                //调起高德导航的方法
                MapUtil.openGaoDeNavigation(pt1, pt2, "广东省深圳市宝安区宝源路", "深圳市宝安区西乡街道永丰综合楼", MainActivity.this);
            }
        });
        tengxun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng pt1 = new LatLng(22.573328, 113.874083);
                LatLng pt2 = new LatLng(22.621644, 114.130809);

                //调起腾讯地图导航的方法
                MapUtil.openTxNavigation(pt1, pt2, "广东省深圳市宝安区宝源路", "深圳市宝安区西乡街道永丰综合楼", MainActivity.this);
            }
        });


    }
}
