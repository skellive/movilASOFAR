package com.java.movilasofar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tablayout;
    private ViewPager viewPager;
    private TabItem producto, compra, pedido;
    public PagerController pagerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager= findViewById(R.id.viewPager);

        producto = findViewById(R.id.producto);
        compra = findViewById(R.id.compra);
        pedido = findViewById(R.id.pedido);

        //pagerController = new PagerController(getSupportFragmentManager(), (Integer) tablayout.getTag());
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
}