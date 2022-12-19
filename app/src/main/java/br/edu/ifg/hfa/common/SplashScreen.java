package br.edu.ifg.hfa.common;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.auth.patient.RetailerStartUpScreen;
import br.edu.ifg.hfa.common.dashboard.patient.PatientDashboard;
import br.edu.ifg.hfa.common.dashboard.pharmacy.PharmacyDashboard;
import br.edu.ifg.hfa.db.DbConnection;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIMER = 2000;

    ImageView backgroundImage;
    TextView poweredByLine;

    Animation sideAnim, bottomAnim;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        backgroundImage = findViewById(R.id.backgound_image);
        poweredByLine = findViewById(R.id.powered_by_line);

        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        backgroundImage.setAnimation(sideAnim);
        poweredByLine.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser mUser = DbConnection.getAuth().getCurrentUser();

                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                Intent intent = new Intent(getApplicationContext(), RetailerStartUpScreen.class);

                if (isFirstTime) {
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply();
                } else {
                    if (mUser != null) {
                        if (mUser.getPhoneNumber() != null) {
                            if (!mUser.getPhoneNumber().isEmpty())
                                intent = new Intent(getApplicationContext(), PatientDashboard.class);
                        }
                        if (mUser.getEmail() != null) {
                            if (!mUser.getEmail().isEmpty())
                                intent = new Intent(getApplicationContext(), PharmacyDashboard.class);
                        }
                    }
                }

                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMER);
    }
}