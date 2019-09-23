package com.example.Moodle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import static java.lang.Thread.sleep;

public class Splash_screen_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);              //Asked to remove the title bar..
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen_activity);       //This is to load the activity specified...

        getSupportActionBar().hide();                                     //This hides the action bar...


        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();                                            //Gave the command to start the process...
    }

    private class LogoLauncher extends Thread{
        public void run(){
            try{
                sleep(2000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
        }
            Intent intent = new Intent(Splash_screen_activity.this,MainActivity.class);
            startActivity(intent);
            Splash_screen_activity.this.finish();                               //To prevent coming to the same activity again...Hence destroying it...
    }
    }
}
