# Pocket Change's Android SDK.

If you're using Unity go <a href="https://github.com/pocketchange/pocketchange-android-sdk-unity-plugin/blob/master/README.md">here</a>.

Follow the instructions below to integrate the SDK. For a working demo application, see the demo-app directory.

Prerequisites: [Eclipse][1], [Android SDK][2] (version 17 or later), and the [Android Plugin][3] (version 17 or later).

**Note that version 17 of the Android SDK and Android Plugin were released in 03/2012; if you experience build errors, please ensure that you have appropriate versions of these components.**

## Step 1: Obtain an id for your game

In order to integrate the Pocket Change Android SDK, you must first obtain an APP_ID from your account manager. Each game will have a separate APP_ID.

## Step 2: Download the SDK

You can either clone the GitHub repository:

```sh
git clone git://github.com/pocketchange/pocketchange-android-sdk.git
```

Or download and extract the files here: <http://github.com/pocketchange/pocketchange-android-sdk/zipball/master>.

## Step 3: Import the Pocket Change SDK Project in Eclipse

To reference the Pocket Change SDK you must import the Android library project in Eclipse. To do this select File » Import. In the dialog window (see below), select General » Existing Projects into Workspace and click "Next."

<img src="http://dl.dropbox.com/u/68268326/sdk-doc-images/import_project_dialog_step1.png" alt="Import Project, Step 1" width="525" height="550" />

On the following screen, select the directory containing the SDK project that you downloaded in step 2, and Eclipse should automatically infer the project name and structure:

<img src="http://dl.dropbox.com/u/68268326/sdk-doc-images/import_project_dialog_step2.png" alt="Import Project, Step 2" width="525" height="619" />

## Step 4: Add a reference to the Pocket Change SDK

Open the properties window for your app (File » Properties » Android), press the *Add...* button and select the pocketchange-android-sdk library.

<img src="http://dl.dropbox.com/u/68268326/sdk-doc-images/add_library_dialog.png" alt="Add Library Reference" width="801" height="614" />

<a name="readme-android-manifest-modifications"></a>
## Step 5: Modify your AndroidManifest.xml

If your manifest file does not already include the permissions to connect to the internet, access network state, obtain account information, and read telephony state, add them inside the &lt;manifest&gt; block. We only use account information for simplifying the login and purchasing flows. We only use telephone state in cases where the phone does not have a valid device ID.

```xml
    <uses-permission android:name="android.permission.GET_ACCOUNTS"></uses-permission>
    <uses-permission android:name="android.permission.INTERNET"></uses-permission>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
```

Finally, declare the application components the SDK requires inside of the &lt;application&gt; block:

```xml
        <activity
            android:name="com.pocketchange.android.rewards.DisplayRewardActivity"
            android:theme="@android:style/Theme.Light.NoTitleBar.Fullscreen">
        </activity>
        <activity
            android:name="com.pocketchange.android.rewards.ShopActivity"
            android:theme="@android:style/Theme.Light.NoTitleBar.Fullscreen">
        </activity>
        
        <service android:name="com.pocketchange.android.http.AsyncHttpRequestService" />
```

## Step 6: Integrate the SDK in your app

First import the Pocket Change SDK in your Activity subclasses:

```java
import com.pocketchange.android.PocketChange;
```

Next initialize the SDK in each Activity's onStart() method:

```java
PocketChange.initialize(this, APP_ID);
```

Do not attempt to guard against duplicate initialization, as doing so will break your integration.

Visual notifications may accompany certain rewards. In order to avoid interfering with your application, the SDK queues these notifications so that you can deliver them at convenient times. Your application must periodically display these notifications, or users will be unaware of their rewards.

To retrieve an Intent that launches an Activity which displays the next pending notification, after invoking initialize, call:
```java
PocketChange.getDisplayRewardIntent();
```

The `getDisplayRewardIntent` method returns null if you should not display any notification; always check for a null return value, as Intents may be removed from the queue automatically at any time.

When the SDK adds a notification to the queue, it sends a broadcast with action com.pocketchange.android.rewards.NOTIFY\_PENDING\_DISPLAY\_REWARD\_INTENT and data matching your application package's URI (i.e. package:&lt;your application package&gt;). To subscribe to these broadcasts, use the following intent filter:
```xml
<intent-filter>
    <action android:name="com.pocketchange.android.rewards.NOTIFY_PENDING_DISPLAY_REWARD_INTENT" />
    <data android:scheme="package" />
</intent-filter>
```

Be sure to validate that the data field matches your application's package name, or you may end up responding to broadcasts intended for other applications. The following sample BroadcastReceiver automatically displays the next notification in the queue immediately upon receipt.

```java
package com.pocketchange.android.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.pocketchange.android.PocketChange;

public class PendingRewardDisplayReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!PocketChange.isInitialized()) {
            return;
        }
        String action = intent.getAction();
        if (action == null || !action.equals(PocketChange.ACTION_NOTIFY_PENDING_DISPLAY_REWARD_INTENT)) {
            return;
        }
        Uri data = intent.getData();
        if (data == null
            || !data.getScheme().equals("package")
            || !data.getSchemeSpecificPart().equals(context.getPackageName())) {
            return;
        }
        Intent displayRewardIntent = PocketChange.getDisplayRewardIntent();
        if (displayRewardIntent != null) {
            displayRewardIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            );
            context.startActivity(displayRewardIntent);
        }
    }
}
```

### Update Your ProGuard Configuration
If you use ProGuard to obfuscate your application's source code, you must update your configuration or the application will either fail to build or malfunction. You can find the configuration the SDK requires in sdk/proguard.cfg. Merge this configuration into your application's proguard.cfg file, and your application should build and function correctly.

In cases where your application contains a conflicting or duplicate obfuscation setting, select the most permissive combination of settings. For example, if your proguard.cfg file contains:

```
-keep public class * extends AnInterface;
```

and the SDK configuration contains:

```
-keep class * extends AnInterface;
```

then the merged version should use the keep directive from the SDK configuration, as the SDK preserves all classes extending AnInterface, regardless of their visibility, whereas your application only preserves public classes implementing the interface.


## <a name="testing"></a>Testing Instructions

You can use test mode to validate your integration: The SDK will grant unlimited rewards so that you can confirm your application's behavior after a reward has been granted. To enable test mode, replace your initialize statement with:

```java
PocketChange.initialize(this, APP_ID, true);
```

**You must change the initialize call back before you release your app, otherwise users will not receive real rewards.**


[1]: http://www.eclipse.org/downloads/
[2]: http://developer.android.com/sdk/index.html
[3]: http://developer.android.com/sdk/eclipse-adt.html
