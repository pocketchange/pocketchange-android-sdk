# Pocket Change's Android SDK.

If you're using Unity go [here][5].

See DemoApp for a working demo application. Follow the instructions below to integrate the SDK. 

Prerequisites: [Eclipse][2], [Android SDK][3] (version 17 or later), and the [Android Plugin][4] (version 17 or later).

**Note that version 17 of the Android SDK and Android Plugin were released in
03/2012; if you experience build errors, please ensure that you have appropriate
versions of these components.**

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

<img src="http://dl.dropbox.com/u/68268326/sdk-doc-images/import_project_dialog_step2.png" alt="Import Project, Step 2" width="525" height="617" />

## Step 4: Add a reference to the Pocket Change SDK

Open the properties window for your app (File » Properties » Android), press the *Add...* button and select the pocketchange-android-sdk library.

![Add Reference][1]

<a name="readme-android-manifest-modifications"></a>
## Step 5: Modify your AndroidManifest.xml

If your manifest file does not already include the permissions to make network calls, access, manage, and use account information, and read telephony state, add them inside the &lt;mainifest&gt; block. We only use account information for simplifying the login and purchasing flows. We only use telephone state in cases where the phone does not have a valid device id.

```xml
<uses-permission android:name="android.permission.GET_ACCOUNTS"></uses-permission>
<uses-permission android:name="android.permission.MANAGE_ACCOUNTS"></uses-permission>
<uses-permission android:name="android.permission.USE_CREDENTIALS"></uses-permission>
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
<uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
```

Finally, declare the application components the SDK requires inside of the &lt;application&gt; block:

```xml
<activity
    android:name="com.pocketchange.android.BankActivity"
    android:configChanges="orientation|keyboardHidden"
    android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
</activity>
<activity
    android:name="com.pocketchange.android.installer.AcceptPermissionsActivity"
    android:theme="@android:style/Theme.NoTitleBar">
</activity>
<activity
    android:name="com.pocketchange.android.installer.AppInstallActivity"
    android:theme="@android:style/Theme.NoTitleBar">
</activity>
<activity
    android:name="com.pocketchange.android.installer.BackgroundAppInstallActivity"
    android:theme="@android:style/Theme.NoTitleBar">
</activity>
<activity
    android:name="com.pocketchange.android.purchasing.client.PurchasingActivity"
    android:theme="@android:style/Theme.NoTitleBar">
</activity>

<service android:name="com.pocketchange.android.installer.AppInstallService" />
<receiver android:name="com.pocketchange.android.installer.AppInstallService$AccountsChangedBroadcastReceiver">
    <intent-filter>
        <action android:name="android.accounts.LOGIN_ACCOUNTS_CHANGED" />
    </intent-filter>
</receiver>
<receiver android:name="com.pocketchange.android.installer.AppInstallService$AppInstalledBroadcastReceiver">
    <intent-filter>
        <action android:name="android.intent.action.PACKAGE_ADDED" />
        <data android:scheme="package" />
    </intent-filter>
</receiver>

<service android:name="com.pocketchange.android.purchasing.client.PurchasingReceiverService" />
<receiver android:name="com.pocketchange.android.purchasing.client.PurchasingReceiver">
    <intent-filter>
        <action android:name="com.pocketchange.android.purchasing.PURCHASING_SUPPORTED" />
    </intent-filter>
</receiver>
```

## Step 6: Integrate the SDK in your app

First import the Pocket Change SDK in your main Activity:

```java
import com.pocketchange.android.PocketChange;
```

Next initialize the SDK in the main Activity's onCreate() method:

```java
PocketChange.initialize(this, APP_ID);
```

You must call the `PocketChange.initialize` method in the `onCreate` method of every Activity that uses the SDK before calling any other SDK methods. Do not worry about duplicate initialization; the SDK automatically prevents it.
 
Use the supplied token counter UI code which displays the number of tokens in the player's account. To show the counter in the upper right corner in your Activity:

```java
@Override
public void onResume() {
    super.onResume();
    PocketChange.displayTokenCounter(this);
}

@Override
public void onPause() {
    super.onPause();
    PocketChange.removeTokenCounter(this);
}
```

The `displayTokenCounter` method also takes left and top margins as arguments to show the counter in a particular location:

```java
PocketChange.displayTokenCounter(this, 20, 40);
```

The SDK ensures that the counter will only appear once in each Activity.

When the user triggers a UI event to start the game, call:

```java
if (PocketChange.takeTurn()) {
  // begin the game
}
```

The `takeTurn` method will return true if the user's account was successfully debited for the number of tokens required to play, in which case you should begin the turn. If the method returns false, do not perform any action; the SDK will automatically render appropriate UI components informing the user that more tokens are required and offering the option to purchase tokens.

If you would like to add any events when our SDK successfully contacts the server or experiences a connection failure, please add a custom listener:

```java
PocketChange.addListener(new PCListener() {
    public void onCurrencyUpdate() {
       // called after the SDK successfully contacts the server
    }
    public void onConnectionFailure() {
      // called after the SDK fails to contact the server
    }
});
```

### Update Your ProGuard Configuration
If you use ProGuard to obfuscate your application's source code, you must
update your configuration or the application will either fail to build or
malfunction. You can find the configuration the SDK requires in
pocketchange/proguard.cfg. Merge this configuration into your application's
proguard.cfg file, and your application should build and function correctly.

In cases where your application contains a conflicting or duplicate obfuscation
setting, select the most permissive combination of settings. For example, if your
proguard.cfg file contains:

```
-keep public class * extends AnInterface;
```

and the SDK configuration contains:

```
-keep class * extends AnInterface;
```

then the merged version should use the keep directive from the SDK configuration,
as the SDK preserves all classes extending AnInterface, regardless of their
visibility, whereas your application only preserves public classes implementing
the interface.


## <a name="testing"></a>Testing Instructions

You can use test mode to try out your integration in a safe environment. Transactions will not by processed by your credit card provider and you can use the following test CC credentials: #4242424242424242 CVC: 999 Exp: 12/2013. To enable test mode replace your initialize statement with:

```java
PocketChange.initializeInTestMode(this, APP_ID);
```

Note: when the bank comes up there will be a banner indicating that you're in a test environment. **You must change the initialize call back before you
release your app to the store, otherwise transactions will not go through.**

[1]: http://dl.dropbox.com/u/68268326/sdk-doc-images/add_library_dialog.png
[2]: http://www.eclipse.org/downloads/
[3]: http://developer.android.com/sdk/index.html
[4]: http://developer.android.com/sdk/eclipse-adt.html
[5]: https://github.com/pocketchange/pocketchange-android-sdk-unity-plugin/blob/master/README.md
