# Pocket Change's Android SDK (Products).

## Step 1: Complete the basic Android SDK integration

Follow all of the steps in the <a href="https://github.com/pocketchange/pocketchange-android-sdk/blob/master/README.md">README</a>. Once your application functions correctly with the the basic SDK components, you can add products to it.

## Step 2: Integrate products into your app

**REMINDER**: You must call the `PocketChange.initialize` method in the `onCreate` method of every Activity that uses the SDK before calling any other SDK methods. Do not worry about duplicate initialization; the SDK automatically prevents it.

To initiate an item purchase, call the `purchaseItem` method with the item's SKU as an argument:

```java
PocketChange.purchaseItem("an-item-sku");
```

When the user triggers a UI event to consume an item, call:

```java
if (PocketChange.useItem("an-item-sku", amountToUse)) {
    // use the item
}
```

The `useItem` method accepts two arguments:

1. sku (String): The SKU of the item to use
2. amount (int): The amount to use

The `useItem` method returns true if the user's account was successfully debited, in which case you should credit the user for consuming the given amount of the item; if the method returns false, update the UI as appropriate and do not credit the user.

To determine the user's current inventory for an item, call:

```java
PocketChange.getItemCount("an-item-sku")
```

The `getItemCount` method returns an int representing the current balance for the item with the provided SKU. This method allows your game to:

* Check for the presence of non-consumable items.
* Show a complete inventory to the user by iterating over all of the SKUs in a given release.

When rendering an inventory, be sure to update the inventory in your Activity's onResume method, as it may change due to item purchases and client-server synchronization events; for example:

```java
@Override
protected void onResume() {
    super.onResume();
    for (String sku : ITEM_SKUS) {
        setItemCount(sku, PocketChange.getItemCount(sku));
    }
}
```

