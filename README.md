# Pocket Change's Android SDK.

If you're using Unity go <a href="https://github.com/pocketchange/pocketchange-android-sdk-unity-plugin/blob/master/README.md">here</a>.

Follow the instructions below to integrate the SDK.

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

When targeting these newer SDK version, you may also need to set your project's target (the target property in project.properties) to android-11 or higher, or the build tools will not recognize the android:hardwareAccelerated attribute.

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

To retrieve an Intent for an Activity which displays the next pending notification, after invoking initialize, call the `PocketChange.getDisplayRewardIntent` method. This method returns null if you should not display any notification; always check for a null return value, as Intents may be removed from the queue automatically at any time. The following code launches the next pending notification from an existing Activity:

```java
Intent rewardIntent = PocketChange.getDisplayRewardIntent();
if (rewardIntent != null) {
    startActivity(rewardIntent);
}
```


### Add a Button to Open the Pocket Change Shop (Optional)

We encourage developers to add a Pocket Change button which opens our shop. Please use the assets <a href="https://www.dropbox.com/s/aivv76wo7kk4j34/pocket_change_tokens.png">here</a>. When the user clicks the button, call:

```java
PocketChange.openShop();
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


## <a name="testing"></a>Testing

You can use test mode to validate your integration: The SDK will grant unlimited rewards so that you can confirm your application's behavior after a reward has been granted. To enable test mode, replace your initialize statement with:

```java
PocketChange.initialize(this, APP_ID, true);
```

**You must disable test mode before releasing your app, otherwise users will not receive real rewards.**

The SDK only works properly on real devices. Do not use emulators for testing or you will get faulty test results.


## <a name="upgrading"></a>Upgrading

To upgrade from an earlier release of the SDK:

1. Pull the latest version of the SDK code from GitHub.
2. Refresh the SDK project in Eclipse (with the project selected, navigate to File » Refresh).
3. Delete all of the previous SDK entries from your AndroidManifest.xml.
4. Complete steps 5 and 6 of the integration instructions.


## <a name="command-line-build-instructions"></a>Building from the Command Line

If you compile your application with the Android SDK Ant build scripts instead of Eclipse, you can use the Android SDK tools to automatically build the Pocket Change SDK and package it with your application. To update your build, instead of following steps 3-4, perform the following tasks.

1. Ensure that your shell's search path (usually stored in the PATH environment variable) includes the Android SDK tools directory. To verify your configuration, execute:

```sh
android --help
```

from any directory, and you should see usage instructions for the Android tools.

2. Generate a build file for the Pocket Change SDK library by executing the following command from the SDK's root directory:

```sh
android update lib-project --path sdk
```

3. Add the Pocket Change SDK to your project as a library dependency by executing the following command from your project's root directory:

```sh
android update project --path . --library <path to Pocket Change SDK>/sdk
```

For further information on adding libraries to your command-line build, see the [Android command-line tools reference][4].


[1]: http://www.eclipse.org/downloads/
[2]: http://developer.android.com/sdk/index.html
[3]: http://developer.android.com/sdk/eclipse-adt.html
[4]: http://developer.android.com/tools/projects/projects-cmdline.html
