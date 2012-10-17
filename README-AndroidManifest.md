# AndroidManifest.xml Entries for the Pocket Change SDK

If your manifest file does not already include the permissions to connect to the internet, access network state, obtain account information, and read telephony state, add them inside the &lt;manifest&gt; block. We only use account information for simplifying the login and purchasing flows. We only use telephone state in cases where the phone does not have a valid device ID.

```xml

    <uses-permission android:name="android.permission.GET_ACCOUNTS"></uses-permission>
    <uses-permission android:name="android.permission.INTERNET"></uses-permission>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
```

Finally, declare the application components the SDK requires inside of the &lt;application&gt; block. For applications targeting SDK versions 14 and higher (by setting minSdkVersion or targetSdkVersion &gt;= 14 in the uses-sdk element):

```xml
    <activity
        android:name="com.pocketchange.android.rewards.DisplayRewardActivity"
        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
        android:hardwareAccelerated="false">
    </activity>
    <activity
        android:name="com.pocketchange.android.rewards.ShopActivity"
        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
        android:hardwareAccelerated="false">
    </activity>
    
    <service android:name="com.pocketchange.android.http.AsyncHttpRequestService" />
```

When targeting these newer SDK versions, you may also need to set your project's target (the target property in project.properties) to android-11 or higher, or the build tools will not recognize the android:hardwareAccelerated attribute.

For applications not matching the aforementioned criterion:

```xml
    <activity
        android:name="com.pocketchange.android.rewards.DisplayRewardActivity"
        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
    </activity>
    <activity
        android:name="com.pocketchange.android.rewards.ShopActivity"
        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
    </activity>
    
    <service android:name="com.pocketchange.android.http.AsyncHttpRequestService" />
```

Only use one of the provided component declarations. If you're unsure which SDK version your application targets, try the manifest entries in the order listed; if the first set of entries causes build errors due to unrecognized XML elements, use the second set.
