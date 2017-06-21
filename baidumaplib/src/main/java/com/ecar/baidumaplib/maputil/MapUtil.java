package com.ecar.baidumaplib.maputil;/*
 *===============================================
 *
 * 文件名:${type_name}
 *
 * 描述: 
 *
 * 作者:
 *
 * 版权所有:深圳市亿车科技有限公司
 *
 * 创建日期: ${date} ${time}
 *
 * 修改人:   金征
 *
 * 修改时间:  ${date} ${time} 
 *
 * 修改备注: 
 *
 * 版本:      v1.0 
 *
 *===============================================
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.ecar.baidumaplib.maputil.ui.MapTranslateUtils;

import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.GAODE_PACKNAME;
import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.baidu2Gao;
import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.isBaduInstalled;
import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.isGaodeInstalled;
import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.isHuawei;
import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.isTengxunInstalled;
import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.openNaviByIntent;
import static com.ecar.baidumaplib.maputil.ui.MapTranslateUtils.openNaviByWeb;

public class MapUtil {
    public static boolean isOpenedMap;//是否打开过地图

    public static boolean isBaiduInstalled() {
        return isBaduInstalled;
    }

    public static boolean isGaodeInstalled() {
        return isGaodeInstalled;
    }

    public static boolean isTengxunInstalled() {
        return isTengxunInstalled;
    }


    public static void openBaiDuNavigation(LatLng startla, LatLng endla, String startAdd, String endAdd, final Activity context) {
        final NaviParaOption para = new NaviParaOption().startPoint(startla)
                .endPoint(endla).startName(startAdd)
                .endName(endAdd);
        //处理华为手机
        if (isHuawei()) {
            if (isBaduInstalled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
                    openNaviByIntent(context, para);
                    isOpenedMap = true;

                } else {
                    BaiduMapNavigation.openBaiduMapNavi(para, context);
                }
            } else {
//                openNaviByWeb(context, para);
//                isOpenedMap = true;
                Toast.makeText(context, "当前未安装百度地图", Toast.LENGTH_SHORT).show();

            }
        } else {
            if (isBaduInstalled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
                    if (isOpenedMap) {
                        BaiduMapNavigation.openBaiduMapNavi(para, context);
                    } else {
                        openNaviByIntent(context, para);
                        isOpenedMap = true;
                    }
                } else {
                    if (isBaduInstalled) { //打开百度地图
                        BaiduMapNavigation.openBaiduMapNavi(para, context);
                        return;
                    } else {
                        openNaviByIntent(context, para);
                        isOpenedMap = true;
                    }
                }
            } else {
//                openNaviByWeb(context, para);
//                isOpenedMap = true;
                Toast.makeText(context, "当前未安装百度地图", Toast.LENGTH_SHORT).show();


            }
        }

    }


    public static void openGaoDeNavigation(LatLng startla, LatLng endla, String startAdd, String endAdd, final Activity context) {
        if (isGaodeInstalled) { //打开高德地图
            startNativeGaode(context, String.valueOf(baidu2Gao(endla).latitude), String.valueOf(baidu2Gao(endla).longitude), endAdd);
        } else {
//                openNaviByWeb(context, para);
//                isOpenedMap = true;
            Toast.makeText(context, "当前未安装高德地图", Toast.LENGTH_SHORT).show();


        }

    }


    public static void openNavigation(LatLng startla, LatLng endla, String startAdd, String endAdd, final Activity context) {
        final NaviParaOption para = new NaviParaOption().startPoint(startla)
                .endPoint(endla).startName(startAdd)
                .endName(endAdd);
        //处理华为手机
        if (isHuawei()) {
//            Toast.makeText(context,"这是华为手机",Toast.LENGTH_SHORT).show();
            if (isBaduInstalled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
//                    Toast.makeText(context,"系统是6.0",Toast.LENGTH_SHORT).show();

                    openNaviByIntent(context, para);
                    isOpenedMap = true;

                } else {
//                    Toast.makeText(context,"系统是6.0以下",Toast.LENGTH_SHORT).show();

                    BaiduMapNavigation.openBaiduMapNavi(para, context);
                }
            } else {
                if (isGaodeInstalled) { //打开高德地图
                    startNativeGaode(context, String.valueOf(baidu2Gao(endla).latitude), String.valueOf(baidu2Gao(endla).longitude), endAdd);
                } else {
                    openNaviByWeb(context, para);
                    isOpenedMap = true;

                }
            }
        } else {
            if (isBaduInstalled) {
//                Toast.makeText(context,"安装了百度",Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
                    if (isOpenedMap) {
                        BaiduMapNavigation.openBaiduMapNavi(para, context);
                    } else {
                        openNaviByIntent(context, para);
                        isOpenedMap = true;
                    }
                } else {
                    if (isBaduInstalled) { //打开百度地图
                        BaiduMapNavigation.openBaiduMapNavi(para, context);
                        return;
                    }

                    if (isGaodeInstalled) { //打开高德地图
                        startNativeGaode(context, String.valueOf(baidu2Gao(endla).latitude), String.valueOf(baidu2Gao(endla).longitude), endAdd);
                    }
                }
            } else {
//                Toast.makeText(context,"没安装百度",Toast.LENGTH_SHORT).show();

                if (isGaodeInstalled) { //打开高德地图
                    startNativeGaode(context, String.valueOf(baidu2Gao(endla).latitude), String.valueOf(baidu2Gao(endla).longitude), endAdd);
                } else {
                    openNaviByWeb(context, para);
                    isOpenedMap = true;
                }
            }
        }

    }

    @SuppressLint("NewApi")
    private static void startNativeGaode(Context context, String endLat, String endLng, String address) {
        if (TextUtils.isEmpty(endLat) || TextUtils.isEmpty(endLng)) {
            return;
        }
        if (TextUtils.isEmpty(address)) {
            address = "目的地";
        }
        try {
            String uri = "androidamap://navi?sourceApplication=app"
                    .concat("&poiname=").concat(address)
                    .concat("&lat=")
                    .concat(endLat)
                    .concat("&lon=")
                    .concat(endLng)
                    .concat("&dev=1&style=2");
            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse(uri));
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setPackage(GAODE_PACKNAME);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }


    //腾讯地图导航

    public static void openTxNavigation(LatLng startla, LatLng endla, String startAdd, String endAdd, final Activity context) {
        if(isTengxunInstalled()){
            double[] txNowLatLng = MapTranslateUtils.map_bd2hx(startla.latitude, startla.longitude);
            double[] txDesLatLng = MapTranslateUtils.map_bd2hx(endla.latitude, endla.longitude);
            String url = "http://apis.map.qq.com/uri/v1/routeplan?type=drive&from=" + startAdd + "&fromcoord=" + txNowLatLng[0] + "," + txNowLatLng[1] + "&to=" + endAdd + "&tocoord=" + txDesLatLng[0] + "," + txDesLatLng[1] + "&policy=1&referer=myapp";

//        TengXunMapActivity.launch(context, url, "网页版地图导航");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }   else{
            Toast.makeText(context, "腾讯地图未安装", Toast.LENGTH_SHORT).show();
        }

    }

}
