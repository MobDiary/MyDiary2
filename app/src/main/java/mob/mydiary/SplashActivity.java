package mob.mydiary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import mob.mydiary.MainActivity;

public class SplashActivity extends AppCompatActivity {
    public final int DelayTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainActivity);
                SplashActivity.this.finish();
            }
        },DelayTime);
    }
}
