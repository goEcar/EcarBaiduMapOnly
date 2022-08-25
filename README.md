# EcarBaiduMapOnly

1.加入了MapUtil类  使用openNavigation方法可开启导航(兼容百度)

2.百度地图 sdk版本为4.0

3.library已经添加了，
        <service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:permission="android.permission.BAIDU_LOCATION_SERVICE"
            android:process=":remote">
            >
        </service>
  不需要在项目里添加 !
