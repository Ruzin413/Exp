package com.example.exp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    private TextView textMonthTotal;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private String userName;

    public DashboardFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());

        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            userName = activity.loggedInUserName;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        textMonthTotal = view.findViewById(R.id.textMonthTotal);
        recyclerView = view.findViewById(R.id.recyclerDailyTotals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateDashboard();
        return view;
    }

    private void updateDashboard() {
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        double monthTotal = dbHelper.getMonthlyTotal(userName, currentMonth);
        textMonthTotal.setText("Total this month: Rs" + monthTotal);

        List<DailyTotal> dailyTotals = dbHelper.getDailyTotals(userName, currentMonth);
        DailyTotalAdapter adapter = new DailyTotalAdapter(dailyTotals);
        recyclerView.setAdapter(adapter);
    }
}
