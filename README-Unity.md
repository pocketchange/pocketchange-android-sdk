# Pocket Change's Android SDK Plugin for Unity.

Follow the instructions below to integrate the SDK plugin.

Prerequisites:

- <a href="http://www.eclipse.org/downloads/">Android SDK</a> (version 17 or later)
- Unity (version 3.4 or later)

**Note that version 17 of the Android SDK was released in 03/2012; if you experience build errors, please ensure that you have an appropriate version.**

## Step 1: Obtain an id for your game

In order to integrate the Pocket Change Android SDK plugin, you must first obtain an APP\_ID from your account manager. Each game will have a separate APP\_ID.

## Step 2: Download the Plugin

Download the plugin from <http://github.com/pocketchange/pocketchange-android-sdk/blob/master/pocketchange/PocketChangeAndroid.unitypackage?raw=true>.

## Step 3: Import the Plugin in Unity

To use the plugin, you must import its corresponding package in Unity: From within your Unity project, select Assets » Import Package » Custom Package and pick the location of the package you downloaded in step 2.

<img src="http://dl.dropbox.com/u/68268326/unity-plugin-doc-images/import_package.png" alt="Import Package" width="476" height="384" />

## Step 4: Create an AndroidManifest.xml

If your project does not already contain an AndroidManifest.xml file in its Plugins/Android directory, you will need to create one.

For your convenience, the Pocket Change plugin includes sample manifest files for Unity 3.4 (Unity34AndroidManifest.xml) and Unity 3.5 (Unity35AndroidManifest.xml); you can find these sample files in Plugins/PocketChangeAndroid/manifest. When using the sample files, make sure to change the package attribute of the &lt;manifest&gt; element to a unique identifier for your game.

For additional information on Android manifest files, see <http://developer.android.com/guide/topics/manifest/manifest-intro.html>.

## Step 5: Modify your AndroidManifest.xml

Update your Plugins/Android/AndroidManifest.xml file to declare the components and permissions required by the SDK plugin. For instructions on modifying your manifest, see the <a href="http://github.com/pocketchange/pocketchange-android-sdk#readme-android-manifest-modifications">SDK documentation</a>.

## Step 6: Integrate the plugin with your game

To initialize the plugin and its corresponding visual elements, use one of the prefabs located in Plugins/PocketChangeAndroid/prefabs.

- **PocketChangeAndroidPlayerInitializer**: Persists for the lifetime of your game and automatically includes the required visual elements in every scene. To use this prefab, include it in the first scene of your game.    
- **PocketChangeAndroidSceneInitializer**: Adds the plugin's visual elements to a single game scene. To use this prefab, include it in one or more scenes. At a minimum, you should include the prefab in the first scene of your game (for example, the menu scene) so that players can easily determine their token balances and purchase more tokens.

To configure the plugin, edit the PocketChangeAndroidControl script:

- Change the APP\_ID constant to match the APP\_ID you obtained in step 1.
- (Optional) Alter the position of the token counter to match your game's layout by modifying the DisplayTokenCounter method. By default, this method displays the counter at the top left of the screen by calling:

```C#
PocketChangeAndroid.DisplayTokenCounter(0, 0);
```

To provide custom left and top margins for the counter, alter the first and second parameters, respectively, in the PocketChangeAndroid.DisplayTokenCounter method invocation.

When a user takes a turn, call:

```C#
PocketChangeAndroid.TakeTurn();
```

Your account manager can set the number of tokens per turn on the server.

The plugin also provides a way to check if the player can continue playing. The following method will check if the user has enough tokens for a turn OR if the game has been unlocked.

```C#
PocketChangeAndroid.CanPlay();
```

## <a name="testing"></a>Testing Instructions

You can use test mode to try out your integration in a safe environment. Transactions will not by processed by your credit card provider, and you can use the following test CC credentials: #4242424242424242 CVC: 999 Exp: 12/2013.

To enable test mode, in the PocketChangeAndroidControl script, set the ENABLE\_TEST\_MODE constant to true.

Note: when the bank comes up there will be a banner indicating that you're in a test environment. **You must change the init call back before you release your app, otherwise transactions will not go through.**

In addition to test mode, the plugin includes a test scene (Plugins/PocketChangeAndroid/testSupport/PocketChangeAndroidPlayerControlTestScene) that allows you to perform various actions to confirm that you have a sane configuration. When using the test scene, be sure to enable test mode, or you will quickly run out of tokens and be unable to continue testing.
