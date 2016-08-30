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

import com.amap.api.maps2d.CoordinateConverter;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

public class MapUtil {
    public static boolean isOpenedMap;//是否打开过地图
    public static String BAIDU_PACKNAME = "com.baidu.BaiduMap";//百度地图包名
    public static String GAODE_PACKNAME = "com.autonavi.minimap";//高德地图包名


    public static void openNavigation(LatLng startla, LatLng endla, String startAdd, String endAdd, final Activity context) {
        final NaviParaOption para = new NaviParaOption().startPoint(startla)
                .endPoint(endla).startName(startAdd)
                .endName(endAdd);
        //处理华为手机
        if (isHuawei()) {
//            Toast.makeText(context,"这是华为手机",Toast.LENGTH_SHORT).show();

            if (isInstallByread(BAIDU_PACKNAME)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
//                    Toast.makeText(context,"系统是6.0",Toast.LENGTH_SHORT).show();

                    openNaviByIntent(context, para);
                } else {
//                    Toast.makeText(context,"系统是6.0以下",Toast.LENGTH_SHORT).show();

                    BaiduMapNavigation.openBaiduMapNavi(para, context);
                }
            } else {
                if (isInstallByread(GAODE_PACKNAME)) { //打开高德地图
                    startNativeGaode(context, String.valueOf(baidu2Gao(endla).latitude), String.valueOf(baidu2Gao(endla).longitude), endAdd);
                } else {
                    openNaviByWeb(context, para);
                }
            }
        } else {
            if (isInstallByread(BAIDU_PACKNAME)) {
//                Toast.makeText(context,"安装了百度",Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //版本高于等于6.0
                    if (isOpenedMap) {
                        BaiduMapNavigation.openBaiduMapNavi(para, context);
                    } else {
                        openNaviByIntent(context, para);
                        isOpenedMap = true;
                    }
                } else {
                    if (isInstallByread(BAIDU_PACKNAME)) { //打开百度地图
                        BaiduMapNavigation.openBaiduMapNavi(para, context);
                        return;
                    }

                    if (isInstallByread(GAODE_PACKNAME)) { //打开高德地图
                        startNativeGaode(context, String.valueOf(baidu2Gao(endla).latitude), String.valueOf(baidu2Gao(endla).longitude), endAdd);
                    }
                }
            } else {
//                Toast.makeText(context,"没安装百度",Toast.LENGTH_SHORT).show();

                if (isInstallByread(GAODE_PACKNAME)) { //打开高德地图
                    startNativeGaode(context, String.valueOf(baidu2Gao(endla).latitude), String.valueOf(baidu2Gao(endla).longitude), endAdd);
                } else {
                    openNaviByWeb(context, para);
                }

            }
        }

    }

    public static void startNativeGaode(Context context, String endLat, String endLng, String address) {
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
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

    /**
     * 方法描述：将百度定位转为高德定位
     * <p>
     *
     * @param 百度LatLng
     * @return
     */
    private static com.amap.api.maps2d.model.LatLng baidu2Gao(LatLng latLng) {
//        latLng = convertBaiduToGPS(latLng);
        //转为临时高德定位对象
//        com.amap.api.maps2d.model.LatLng oldLath = new com.amap.api.maps2d.model.LatLng(latLng.latitude, latLng.longitude);
//
//        //开始转换
//        CoordinateConverter coordinateConverter = new CoordinateConverter();
//        coordinateConverter.from(CoordinateConverter.CoordType.BAIDU);
//        com.amap.api.maps2d.model.LatLng newLatLng;
//        try {
//            coordinateConverter.coord(oldLath);
//            newLatLng = coordinateConverter.convert();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = latLng.longitude - 0.0065, y = latLng.latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        com.amap.api.maps2d.model.LatLng  newLatLng=new com.amap.api.maps2d.model.LatLng(z * Math.sin(theta)+ 0.0030,z * Math.cos(theta)-0.0049) ;
        return newLatLng;
    }

    public static LatLng convertBaiduToGPS(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        com.baidu.mapapi.utils.CoordinateConverter converter = new com.baidu.mapapi.utils.CoordinateConverter();
        converter.from(com.baidu.mapapi.utils.CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
        double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
        BigDecimal bdLatitude = new BigDecimal(latitude);
        bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdLongitude = new BigDecimal(longitude);
        bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
    }

}
