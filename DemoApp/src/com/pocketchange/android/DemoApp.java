package com.pocketchange.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pocketchange.android.R;

public class DemoApp extends Activity {
	
	private final String TAG = this.getClass().getSimpleName();	
	private static final String APP_ID = "test";	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        PocketChange.initialize(getApplicationContext(), APP_ID);
        PocketChange.displayTokenCounter(this);
        //PocketChange.enableDebug();
    }
    
    public void takeTurn(View view) {
      Log.i(TAG, "taking turn"); 

      if (PocketChange.canPlay()) {
          PocketChange.takeTurn();
      }else {
          new AlertDialog.Builder(this)
            .setMessage("You must buy more tokens to play!")
            .setCancelable(true)
            .setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton){}
                })
          .show();
      }
    }
    
    public void onDestroy() {
        super.onDestroy();
        PocketChange.removeTokenCounter(this);
    }
}
