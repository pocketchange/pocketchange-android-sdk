# Pocket Change Android SDK

If you're using Unity go <a data-href="/documentation/unity" href="https://github.com/pocketchange/pocketchange-android-sdk-unity-plugin">here</a>.

Follow the instructions below to integrate the SDK.

Prerequisites: [Eclipse][1], [Android SDK][2] (version 17 or later), and the [Android Plugin][3] (version 17 or later).

**Note that version 17 of the Android SDK and Android Plugin were released in 03/2012; if you experience build errors, please ensure that you have appropriate versions of these components.**

**You must disable test mode before submiting your app for QA, a production build of an apk with test mode enabled will throw a fatal error.** (This ensures apks that are released to the Play Store do not point to our sandbox environment)

## Step 1: Obtain an API Key for Your Application

In order to integrate the Pocket Change Android SDK, you must first obtain an API key (formerly referred to as an "App ID") from your account manager. Each application will have a separate key.

## Step 2: Download the SDK

You can either clone the GitHub repository:

```sh
git clone git://github.com/pocketchange/pocketchange-android-sdk.git
```

Or download and extract the files here: <http://github.com/pocketchange/pocketchange-android-sdk/zipball/master>.

## Step 3: Import the Pocket Change SDK Project in Eclipse

To reference the Pocket Change SDK you must import the Android library project in Eclipse. To do this select File » Import. In the dialog window (see below), select General » Existing Projects into Workspace and click "Next."

<img src="https://dl.dropbox.com/u/68268326/sdk-doc-images/import_project_dialog_step1.png" alt="Import Project, Step 1" width="525" height="550" />

On the following screen, select the directory containing the SDK project that you downloaded in step 2, and Eclipse should automatically infer the project name and structure:

<img src="https://dl.dropbox.com/u/68268326/sdk-doc-images/import_project_dialog_step2.png" alt="Import Project, Step 2" width="525" height="619" />

## Step 4: Add a reference to the Pocket Change SDK

Open the properties window for your app (File » Properties » Android), press the *Add...* button and select the pocketchange-android-sdk library.

<img src="https://dl.dropbox.com/u/68268326/sdk-doc-images/add_library_dialog.png" alt="Add Library Reference" width="801" height="614" />

<a name="readme-android-manifest-modifications"></a>

## Step 5: Modify your AndroidManifest.xml

In order to make the SDK components accessible to your application, follow the instructions in <a data-href="/documentation/android_manifest" href="https://github.com/pocketchange/pocketchange-android-sdk/blob/master/README-AndroidManifest.md" target="_blank">"AndroidManifest.xml Entries for the Pocket Change SDK"</a>.

## Step 6: Integrate the SDK code

Follow these instructions exactly as written, paying close attention to the placement of SDK calls in activity lifecycle methods. **If an SDK method invocation appears within an activity lifecycle method (one of `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, or `onDestroy`) in the documentation, do not attempt to call it from a different method.**

First, import the Pocket Change SDK in your `android.app.Activity` subclasses:

```java
import com.pocketchange.android.PocketChange;
```

Next, initialize the SDK as the first statement in each activity's `onStart` method: 

```java
protected void onStart() {
   PocketChange.initialize(this, API_KEY);
   ...
}
```

Do not attempt to guard against duplicate initialization, as doing so will break your integration.

Visual notifications may accompany certain rewards. In order to avoid interfering with your application, the SDK queues these notifications so that you can deliver them at convenient times. Your application must periodically display these notifications, or users will be unaware of their rewards.

To retrieve an `Intent` for an activity which displays the next pending notification, at the end of the `onStart` method, call the `PocketChange.getPendingNotificationIntent` method. This method returns `null` if you should not display any notification; always check for a `null` return value, as intents may be removed from the queue automatically at any time. The following code launches the next pending notification from an existing activity:

```java
protected void onStart() {
  ...
  Intent notificationIntent = PocketChange.getPendingNotificationIntent();
  if (notificationIntent != null) {
      startActivity(notificationIntent);
  }
}
```

## Step 7: Add a Shop Button

Please use the assets <a href="https://www.dropbox.com/s/aivv76wo7kk4j34/pocket_change_tokens.png">here</a>. When a user clicks the button, call:

```java
PocketChange.openShop();
```

## Step 8: Add Event-Based Rewards

In order to reward users based on events specific to your application, you must provide your sales representative with a listing of events. Once your representative has configured the events, you can start testing the related functionality in your application.

As soon as an event occurs, invoke the following method:

```java
void grantReward(String referenceID, int amount)
```

This method informs the SDK that the event with the provided `referenceID` occurred `amount` times. Typically, `amount` should be 1; a value greater than 1 indicates multiple occurrences of the event. **Until your application goes live with events, this method will only have an effect in test mode.**

Visual notifications may accompany certain rewards. In order to avoid interfering with your application, the SDK queues these notifications so that you can deliver them at convenient times. Your application must periodically display these notifications, or users will be unaware of their rewards.

Please ensure the call to `getPendingNotficationIntent` does not immediately follow the call to `grantReward` or `initalize`, these methods require a small delay in which to validate the rewards with our servers before the application can display them.

Next ensure that you are displaying the queued notifications at a spot that makes sense for you app (i.e. in a level-based game at the end of each level displaying pending rewards makes sense). After invoking initialize, call the `getPendingNotificationIntent` method to display any rewards accumulated since the last call to this method. This method returns `null` if you should not display any notification; always check for a `null` return value, as intents may be removed from the queue automatically at any time. The following code launches the next pending notification from an existing activity:

```java
Intent notificationIntent = PocketChange.getPendingNotificationIntent();
if (notificationIntent != null) {
    startActivity(notificationIntent);
}
```

## Step 9: Add Loyalty Rewards

### Loyalty Rewards Data Flow
When the SDK detects that a user has completed a transaction for loyalty rewards available in your application, it sends a broadcast, local to your application, indicating the presence of new data. Your application should respond to this broadcast by updating the user's item inventory with the data provided by the SDK.

In certain cases, the SDK may send repeated broadcasts for the same transaction. These repeated broadcasts comprise part of the delivery retry mechanism, and do not indicate a malfunction. In case of a duplicate broadcast, the item inventory returned by the SDK will not contain any duplicate data. Because your inventory management system should simply update all product quantities using the most recent data provided by the SDK, duplicate broadcasts should not affect your inventory logic.

### Requirements
In order to setup loyalty rewards you must have a SKU for each loyalty reward you are offering, this is provided by Pocket Change with your integration info.  Contact your sales representative if you have any questions.

### Add the Loyalty Rewards Components to your AndroidManifest.xml
Add the following entry to your AndroidManifest.xml:

```xml
<receiver android:name="com.pocketchange.android.ProductTransactionsReceiver" android:exported="false">
    <intent-filter>
        <action android:name="com.pocketchange.android.rewards.NOTIFY_PRODUCT_TRANSACTION" />
    </intent-filter>
</receiver>
```

### Subscribe to Transaction Broadcasts
In order to receive broadcasts when users purchase loyalty rewards available in your application, you must register a receiver in your application's manifest with the following intent filter.

```xml
<intent-filter>
    <action android:name="com.pocketchange.android.rewards.NOTIFY_PRODUCT_TRANSACTION" />
</intent-filter>
```

Dynamically registered receivers (receivers registered via the `android.content.Context.registerReceiver` method) will not receive transaction broadcasts, as only recent versions of Android permit sending local broadcasts to dynamically registered receivers.

The manifest entry for your receiver should include the `android:exported="false"` attribute to ensure that external applications cannot access it.

In your `BroadcastReceiver` implementation, you should, depending on your application's design, either synchronously update the user's item inventory or, if updates involve blocking operations, queue an asynchronous update. To access the user's current inventory, after initializing the SDK by invoking the `com.pocketchange.android.PocketChange.initialize` method, you can use the following non-blocking, static SDK methods located in the `com.pocketchange.android.PocketChange` class.

**boolean hasPurchasedProduct(String sku)**
Returns true if the user has purchased the product identified by `sku`, false otherwise. This method exists primarily as a convenience for identifying purchases of single-quantity, non-consumable items, such as level packs.

**int getQuantityPurchasedForProduct(String sku)**
Returns the total quantity purchased for the product identified by `sku`. You can use this method to create a complete item inventory by iterating over each SKU in your game.

If you fail to call the `PocketChange.initialize` method before invoking any of the aforementioned methods, the SDK may throw an exception.

Note that the SDK does not track item usage. Instead, your application must maintain a record of item debits and obtain current balances by computing the difference between credits (aggregated from all vendors selling your goods) and debits. This design permits you to accept purchases from any number of sources, such as the Google Play store, the Pocket Change shop, and third party web storefronts.

### Synchronize Your Item Inventory with the SDK's Inventory

To account for failures in your broadcast receiver implementation (for example, the device losing power during execution of your onReceive method), your application should periodically synchronize its item inventories with the inventories provided by the SDK. You can cover such abnormal scenarios by initiating a synchronization in the onCreate method of your application's main activities (activities matching the intent filter `<action android:name="android.intent.action.MAIN" />`).

You should be able to reuse the logic from your receiver to implement periodic synchronizations, as in both cases the SDK provides the same query interface.

## Update Your ProGuard Configuration

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
PocketChange.initialize(this, API_KEY, true);
```

Each time you switch between production and test modes, you must uninstall the application to ensure correct behavior.

**Disable test mode before submiting your app for review.** Production builds of APKs in test mode throw fatal errors upon initialization in order to guard against accidental releases of test builds to marketplaces such as Google Play.

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

## Known Issues
### Link errors (ClassNotFoundException) with ADT 22
There is a bug in the latest release of Android Developer Tools that can result in a ClassNotFoundException error when attempting to build your project after upgrading to ADT 22.  This issue should be resolved when ADT 22.0.1 is released.  Full details of the issue are available here: https://code.google.com/p/android/issues/detail?id=55304

To work around this issue, go to Properties->Order and export on the app project and check 'Android Private Libraries'.

[1]: http://www.eclipse.org/downloads/
[2]: http://developer.android.com/sdk/index.html
[3]: http://developer.android.com/sdk/eclipse-adt.html
[4]: http://developer.android.com/tools/projects/projects-cmdline.html
