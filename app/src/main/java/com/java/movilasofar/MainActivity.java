package com.java.movilasofar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    private TabLayout tablayout;
    private ViewPager viewPager;
    private TabItem producto, compra, pedido;
    public PagerController pagerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablayout = (TabLayout)findViewById(R.id.tablayout);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        viewPager= findViewById(R.id.viewPager);

        producto = findViewById(R.id.producto);
        compra = findViewById(R.id.compra);
        pedido = findViewById(R.id.pedido);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 100){
                    mProgressStatus++;
                    SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(mProgressStatus);

                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        pagerController = new PagerController(getSupportFragmentManager(), tablayout.getTabCount());
                        viewPager.setAdapter(pagerController);
                        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                viewPager.setCurrentItem(tab.getPosition());

                                if (tab.getPosition()==0){
                                    pagerController.notifyDataSetChanged();
                                }else if (tab.getPosition()==1){
                                    pagerController.notifyDataSetChanged();
                                }else if (tab.getPosition()==2){
                                    pagerController.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });

                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
                    }
                });
            }
        }).start();

    }
}