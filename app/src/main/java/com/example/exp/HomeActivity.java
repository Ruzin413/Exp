package com.example.exp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {
    String loggedInUserName;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Receive logged-in user name
        loggedInUserName = getIntent().getStringExtra("loggedInUserName");

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new ScreenSlidePagerAdapter(this, loggedInUserName));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Dashboard");
                    break;
                case 1:
                    tab.setText("Add Expense");
                    break;
                case 2:
                    tab.setText("All Expenses");
                    break;
                case 3:
                    tab.setText("Profile");
                    break;
            }
        }).attach();
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        private final String userName;

        public ScreenSlidePagerAdapter(@NonNull AppCompatActivity fa, String userName) {
            super(fa);
            this.userName = userName;
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new  DashboardFragment();
                case 1:
                    return AddExpenseFragment.newInstance(userName);
                case 2:
                    return new  AllExpensesFragment();
                case 3:
                    return  new ProfileFragment();
                default:
                    return new Fragment();
            }
        }
        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
