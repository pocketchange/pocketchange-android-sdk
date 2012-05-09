package com.pocketchange.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pocketchange.android.R;

public class DemoApp extends Activity {
	private static final String APP_ID = "test";	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        PocketChange.initialize(this, APP_ID);
    }

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

    public void startGame(final View view) {
        if (PocketChange.takeTurn()) {
            // Note: Ordinarily, this block should actually start a game instead
            // of showing a toast.
            Toast.makeText(this, "Starting game", Toast.LENGTH_SHORT).show();
        }
    }
}
