package phone.ktv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import phone.ktv.app.App;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResumeonResumeonResumeonResume");
        sendBroadcast(new Intent(App.UPDATEPLAYER));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestartonRestartonRestart");
    }
}
