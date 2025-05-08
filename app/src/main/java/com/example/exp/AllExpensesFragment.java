package com.example.exp;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import java.util.ArrayList;
import java.util.List;

public class AllExpensesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList;
    private String userName;

    private DatabaseHelper db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_expenses, container, false);

        recyclerView = view.findViewById(R.id.expenseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseList = new ArrayList<>();
        adapter = new ExpenseAdapter(expenseList);
        recyclerView.setAdapter(adapter);
        db = new DatabaseHelper(requireContext());
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            userName = activity.loggedInUserName;
        }
        loadExpenses();

        return view;
    }

    private void loadExpenses() {
        expenseList.clear();
        expenseList.addAll(db.getAllExpenses(userName)); // Implement this method below
        adapter.notifyDataSetChanged();
    }
}
