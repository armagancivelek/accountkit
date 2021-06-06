package com.armagancivelek.accountkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.armagancivelek.accountkit.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.VideoConfiguration;
import com.huawei.hms.ads.VideoOperator;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.service.AccountAuthService;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "deneme";
    private Toolbar toolbar;
    private BannerView topBannerView, bottomBannerView;
    private ActivityHomeBinding binding = null;
    private AccountAuthService service;
    private AccountAuthParams authParams;
    private AdListener adListener;
    private static final String AD_ID = "testx9dtjwj8hp";
    private RewardAd rewardAd;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        init();
        setUpAds();
        eventHandler();
        createRewardAd();
       loadRewardAD();





    }

    private void rewardAdShow() {
        if (rewardAd.isLoaded()) {
            rewardAd.show(HomeActivity.this, new RewardAdStatusListener() {
                @Override
                public void onRewardAdOpened() {

                  //Rewarded ad opened
                }

                @Override
                public void onRewardAdFailedToShow(int errorCode) {
                    // Failed to display the rewarded ad.

                }

                @Override
                public void onRewardAdClosed() {
                    // Rewarded ad closed.

                }

                @Override
                public void onRewarded(Reward reward) {


                }
            });
        }
    }

    private void loadRewardAD() {
        if (rewardAd == null) {
            createRewardAd();
        }
        RewardAdLoadListener listener = new RewardAdLoadListener() {
            @Override
            public void onRewardedLoaded() {

            }

            @Override
            public void onRewardAdFailedToLoad(int errorCode) {

            }
        };
        rewardAd.loadAd(new AdParam.Builder().build(), listener);
    }

    private void createRewardAd() {

        rewardAd = new RewardAd(this, AD_ID);
    }




    private void setUpAds() {


        //  Add Banner View use xml mode
        bottomBannerView = findViewById(R.id.hw_banner_view);
        AdParam adParam = new AdParam.Builder().build();
        bottomBannerView.loadAd(adParam);

        //Add Banner View use coding  mode
        topBannerView = new BannerView(this);
        topBannerView.setAdId("testw6vs28auh3");
        topBannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        topBannerView.setBannerRefresh(3000L);
        topBannerView.loadAd(adParam);

        RelativeLayout rootView = findViewById(R.id.root_view);
        rootView.addView(topBannerView);
    }

    private void eventHandler() {
        adListener = new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailed(int i) {
                super.onAdFailed(i);
            }
        };
        topBannerView.setAdListener(adListener);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.push_notification : {
                        getToken();
                       
                    }

                    case R.id.data_message : {

                    }
                    default: return true;
                }
            }
        });



        binding.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Map.class));
            }
        });




    }

    private void getToken() {
        new Thread(){
            @Override
            public void run() {
                try {
                    String appId = AGConnectServicesConfig.fromContext(HomeActivity.this)
                            .getString("client/app_id");
                    String token = HmsInstanceId.getInstance(HomeActivity.this)
                            .getToken(appId, "HCM");

                    Log.i(TAG, "token: " + token);

                }catch (ApiException e){
                    Log.e(TAG, "getToken() failure: " + e.getMessage());
                }
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void init() {

        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hoşgeldiniz  " + AccountAuthManager.getAuthResult().displayName);
        authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .createParams();
        service = AccountAuthManager.getService(HomeActivity.this, authParams);
        HwAds.init(this);
        navigationView =binding.navigationView;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

           
            case R.id.signOut: {
                service.cancelAuthorization();
                finish();
            }
            case R.id.rewardedAds: {

                rewardAdShow();

            }




            default:
                return super.onOptionsItemSelected(item);
        }
    }












}

