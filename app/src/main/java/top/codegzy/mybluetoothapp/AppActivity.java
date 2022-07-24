package top.codegzy.mybluetoothapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AppActivity extends AppCompatActivity {

    private long exitTime = 0;


    private Typeface iconFont;
    private TextView allIcon, allBlueName;
    private TextView pairIcon, pairBlueName;
    private TextView unPairIcon, unPairBlueName;
    private ConstraintLayout allBlue, pairBlue, unPairBlue;

    @SuppressLint("MissingPermission")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        iconFont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");

        allIcon = findViewById(R.id.allIcon);
        allBlueName = findViewById(R.id.allBlueName);

        pairIcon = findViewById(R.id.pairIcon);
        pairBlueName = findViewById(R.id.pairBlueName);

        unPairIcon = findViewById(R.id.unPairIcon);
        unPairBlueName = findViewById(R.id.unPairBlueName);

        allBlue = findViewById(R.id.allBlue);
        pairBlue = findViewById(R.id.pairBlue);
        unPairBlue = findViewById(R.id.unPairBlue);

        allIcon.setTypeface(iconFont);
        allIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
        allIcon.setTextColor(Color.WHITE);
        allIcon.setText(getResources().getString(R.string.icon_all_bluetooth));
        allBlueName.setTextColor(Color.WHITE);
        allBlueName.setText("ALL BLUETOOTH");

        pairIcon.setTypeface(iconFont);
        pairIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
        pairIcon.setTextColor(Color.WHITE);
        pairIcon.setText(getResources().getString(R.string.icon_pair_bluetooth));
        pairBlueName.setTextColor(Color.WHITE);
        pairBlueName.setText("PAIRED BLUETOOTH");

        unPairIcon.setTypeface(iconFont);
        unPairIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
        unPairIcon.setTextColor(Color.WHITE);
        unPairIcon.setText(getResources().getString(R.string.icon_unpair_bluetooth));
        unPairBlueName.setTextColor(Color.WHITE);
        unPairBlueName.setText("UNPAIR BLUETOOTH");

        Intent intent = new Intent(AppActivity.this, MainActivity.class);
        allBlue.setOnClickListener(v -> {
            intent.putExtra("type", Constants.ALL);
            startActivity(intent);
        });
        pairBlue.setOnClickListener(v -> {
            intent.putExtra("type", Constants.PAIRED);
            startActivity(intent);
        });
        unPairBlue.setOnClickListener(v -> {
            intent.putExtra("type", Constants.UNPAIRED);
            startActivity(intent);
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Constants.toastLong(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(1);
        }
    }
}