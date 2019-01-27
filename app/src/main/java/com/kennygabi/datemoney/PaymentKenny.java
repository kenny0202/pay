package com.kennygabi.datemoney;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PaymentKenny extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.kenny_tab, container, false);


        mRecyclerView = v.findViewById(R.id.my_recycler_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Query query = db.collection("userspayment").orderBy("timestamp", Query.Direction.DESCENDING).limit(10);

        Query query = db.collection("userspayment").whereEqualTo("name", "Kenny").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<UserPaymentModel> options = new FirestoreRecyclerOptions.Builder<UserPaymentModel>()
                .setQuery(query, UserPaymentModel.class)
                .build();

        mAdapter = new MyAdapter(options);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
