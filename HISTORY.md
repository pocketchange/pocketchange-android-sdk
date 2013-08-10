## 6.0.5 (2013-08-09)

* Added a work-around for an Android API bug occasionally causing the exception: "java.lang.IllegalArgumentException: Service not registered: android.speech.tts.TextToSpeech..."

## 6.0.4 (2013-08-08)

* Fixed implementation of visual components required by the Unity plugin

## 6.0.3 (2013-08-07)

* Made core functionality contingent on the user opting in
* Eliminated the android.permission.GET_ACCOUNTS permission requirement

## 6.0.2 (2013-06-03)

* Made the default notification background fully transparent
* Fixed rare NullPointerExceptions in the com.pocketchange.android.PCSingleton class

## 6.0.1 (2013-04-02)

* Added a work-around for an Android API bug manifesting itself as a never-ending "Loading" spinner in certain activities
* Improved the performance of UI components
* Reduced the SDK's maximum resource requirements

## 6.0.0 (2012-12-04)

* Updated notification UI and API

## 5.1.8 (2012-10-17)

* Enhanced recovery logic and user-facing messaging for rare exceptions

## 5.1.7 (2012-09-20)

* Improved analytics reporting

## 5.1.6 (2012-09-05)

* Added methods required to achieve visual parity between the Unity plugin and the SDK

## 5.1.5 (2012-09-04)

* Unified visual styles of rewards activities and added a background dimming effect to them

## 5.1.4 (2012-08-30)

* Improved support for hardware acceleration
* Made UI elements render transparently above the application

## 5.1.3 (2012-08-09)

* Fixed intermittent AndroidRuntimeException caused by web view preloading
* Minor improvements to keyboard interaction in the store

## 5.1.2 (2012-08-08)

* Added validation to guard against production releases with test mode enabled

## 5.1.1 (2012-08-07)

* Minor bug fix for rewards response parser

## 5.1.0 (2012-08-06)

* Added hooks to control timing of rewards activity launches

## 5.0.0 (2012-07-30)

* Added support for rewards
* Removed token-based play
* Removed consumable products

## 4.2.1 (2012-06-13)

* Fixed bugs in pause/resume logic for fragment UI components
* Implemented asynchronous request batching to improve network performance and minimize battery drain

## 4.2.0 (2012-06-05)

* Added support for products

## 4.1.1 (2012-05-29)

* Improved stability of purchasing UI components

## 4.1.0 (2012-05-25)

* Added background installer
* Improved API request performance

## 4.0.0 (2012-05-08)

* Substantial improvements to client-server synchronization
* Fixed bug in offer redemption UI

## 3.0.4 (2012-04-26)

* Minor bug fixes for installer

## 3.0.3 (2012-04-23)

* Updated demo app to match README
* Added heartbeat ping

## 3.0.2 (2012-04-18)

* Changed to binary distribution
* Improved stability of installer and purchasing activities

## 3.0.1 (2012-04-05)

* Changed name of PC Store

## 3.0.0 (2012-04-05)

* Added Google in-app

## 2.5.0 (2012-03-26)

* Added support for the daily gift

## 2.4.2 (2012-03-20)

* Bumped version

## 2.4.1 (2012-03-20)

* Fixed syncing tokens in the background on startup

## 2.4.0 (2012-03-16)

* Moved tokens per turn to the server
* Added more debugging fields

## 2.3.0 (2012-03-09)

* Fixed Paypal cancel button
* Added test mode

## 2.1.1 (2012-03-06)

* Error message in the Bank when there is no network connection

## 2.1 (2012-03-02)

* Speed improvements for the Bank
* Added sessions

## 2.0 (2012-02-24)

* Bank loads in a separate Activity
* Changed the displayTokenCounter API

## 1.3.2 (2012-02-20)

* Fixed NPE in properties

## 1.3.1 (2012-02-17)

* Fixed a bug in isUnlocked()

## 1.3.0 (2012-02-17)

* Added unlock feature

## 1.2.1 (2012-02-10)

* Fixed external url handling

## 1.2 (2012-02-08)

* Added the token counter
* Fixed sync issue between local counts and the bank

## 1.1 (2012-01-26)

* Added auto-login

## 1.0 (2012-01-16)

* Initial release
