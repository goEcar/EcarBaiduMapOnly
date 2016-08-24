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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class MapUtil {
    public static boolean isOpenedMap;//是否打开过地图

    public static void openNavigation(LatLng startla, LatLng endla, String startAdd, String endAdd, final Activity context) {
        final NaviParaOption para = new NaviParaOption().startPoint(startla)
                .endPoint(endla).startName(startAdd)
                .endName(endAdd);
        //处理华为手机
        if (isHuawei()) {
            if (isInstallByread("com.baidu.BaiduMap")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
                    openNaviByIntent(context, para);
                } else {
                    BaiduMapNavigation.openBaiduMapNavi(para, context);
                }
            } else {
                BaiduMapNavigation.openBaiduMapNavi(para, context);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
                if (isOpenedMap) {
                    BaiduMapNavigation.openBaiduMapNavi(para, context);
                } else {
                    openNaviByIntent(context, para);
                    isOpenedMap = true;
                }
            } else {
                BaiduMapNavigation.openBaiduMapNavi(para, context);
            }
        }

    }

    /**
     * 方法描述：打开浏览器
     * <p>
     *
     * @param
     * @return
     */

    private static void openNaviByWeb(Activity context, NaviParaOption para) {
        if (hasBrowser(context)) {
            BaiduMapNavigation.openWebBaiduMapNavi(para, context); //打开网页导航
        } else {
            Toast.makeText(context, "抱歉，未检测到浏览器无法开启导航。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 方法描述：打开浏览器
     * <p>
     *
     * @param startla 起始经纬度     endla   结束经纬度     sAdd 开始地址名称       eAdd 结束地址名称
     * @return
     */
    private static void openNaviByIntent(Activity context, NaviParaOption para) {
        //调起百度地图客户端
        Intent intent;
        try {
            //结构说明地址 ：http://developer.baidu.com/map/uri-introandroid.htm
            String url = addString("intent://map/direction?origin=latlng:",
                    String.valueOf(para.getStartPoint().latitude), ",",
                    String.valueOf(para.getStartPoint().longitude), "|name:", para.getStartName(),
                    "&destination=latlng:",
                    String.valueOf(para.getEndPoint().latitude), ",",
                    String.valueOf(para.getEndPoint().longitude), "|name:", para.getEndName(),
                    "&mode=driving#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end) ");
//                            intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&longitude:34.264642646862,108.95108518068&mode=driving®ion=西安&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Log.e("map", url);
            intent = Intent.getIntent(url);
            context.startActivity(intent); //启动调用
//                Log.e("GasStation", "百度地图客户端已经安装");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法描述：拼接字符串
     * <p>
     *
     * @param strs字符串的可变集合
     * @return
     */
    private static String addString(String... strs) {
        String lastString = "";
        if (strs != null) {
            if (strs.length == 1) {
                return strs[0];
            } else {
                for (String str : strs) {
                    lastString = lastString.concat(str);
                }
            }
        }
        return lastString;
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 方法描述：判断是否为华为手机
     * <p>
     *
     * @param
     * @return
     */
    public static boolean isHuawei() {
        String bland = android.os.Build.MANUFACTURER;
        return !TextUtils.isEmpty(bland) && (bland.contains("HUAWEI") || bland.contains("华为") || bland.contains("huawei"));
    }

    /**
     * 方法描述： 是否安装了浏览器
     * <p>
     *
     * @param
     * @return
     */
    @SuppressWarnings("WrongConstant")
    public static boolean hasBrowser(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://"));

        List<ResolveInfo> list = pm.queryIntentActivities(intent,
                PackageManager.GET_INTENT_FILTERS);
        final int size = (list == null) ? 0 : list.size();
        return size > 0;
    }


}
