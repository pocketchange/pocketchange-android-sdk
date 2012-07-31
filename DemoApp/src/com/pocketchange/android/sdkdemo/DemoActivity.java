package com.pocketchange.android.sdkdemo;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.pocketchange.android.PocketChange;
import com.pocketchange.android.R;
import com.pocketchange.android.util.CollectionUtils;

public class DemoActivity extends Activity {
	private static final String APP_ID = "test";	
	private static final String[] productSkus = new String[] {
	    "ninja-1",
	    "pirate-1",
	    "insane_map_pack"
	};
	
	private Dialog mCurrentDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        // Note: The third parameter should be false or omitted for a
        // production release.
        PocketChange.initialize(this, APP_ID, true);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissOptDialog();
    }
    
    private void showDialog(Dialog dialog) {
        dismissOptDialog();
        mCurrentDialog = dialog;
        dialog.show();
    }

    void dismissOptDialog() {
        if (mCurrentDialog != null) {
            mCurrentDialog.dismiss();
            mCurrentDialog = null;
        }
    }

    public void openShop(View view) {
        PocketChange.openShop();
    }

    public void showProductInventory(View view) {
        List<String> inventoryList = new LinkedList<String>();
        for (String sku : productSkus) {
            int quantity = PocketChange.getQuantityPurchasedForProduct(sku);
            if (quantity > 0) {
                inventoryList.add(sku + ": " + Integer.toString(quantity));
            }
        }
        String inventoryString = (inventoryList.isEmpty() ? "No entries" : CollectionUtils.join(inventoryList, "\n"));
        Dialog inventoryDialog = new AlertDialog.Builder(this)
        .setTitle("Product Inventory")
        .setMessage(inventoryString)
        .setCancelable(true)
        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissOptDialog();
            }
        })
        .create();
        showDialog(inventoryDialog);
    }
}
