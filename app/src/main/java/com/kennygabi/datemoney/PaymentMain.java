package com.kennygabi.datemoney;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class PaymentMain extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView GabiSumTextView;
    private TextView KennySumTextView;
    private TextView PayNextTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_tab, container, false);

        mRecyclerView = v.findViewById(R.id.my_recycler_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        GabiSumTextView = v.findViewById(R.id.GabiSumValue);
        final ArrayList<UserPaymentModel> gabiArrayList = new ArrayList<>();
        db.collection("userspayment").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<UserPaymentModel> types = queryDocumentSnapshots.toObjects(UserPaymentModel.class);

                    gabiArrayList.addAll(types);

                    double sum = 0;
                    for (UserPaymentModel u : gabiArrayList) {
                        if (u.getName().equals("Gabi")) {
                            sum += Double.valueOf(u.getAmount());
                        }
                    }
                    Log.d(TAG, "onSuccess: " + sum);
                    GabiSumTextView.setText("Gabi spent $" + String.valueOf(sum));
                }
            }
        });

        KennySumTextView = v.findViewById(R.id.KennySumValue);
        final ArrayList<UserPaymentModel> kennyArrayList = new ArrayList<>();
        db.collection("userspayment").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<UserPaymentModel> types = queryDocumentSnapshots.toObjects(UserPaymentModel.class);

                    kennyArrayList.addAll(types);

                    double sum = 0;
                    for (UserPaymentModel u : kennyArrayList) {
                        if (u.getName().equals("Kenny")) {
                            sum += Double.valueOf(u.getAmount());
                        }
                    }
                    Log.d(TAG, "onSuccess: " + sum);
                    KennySumTextView.setText("Kenny spent $" + String.valueOf(sum));
                }
            }
        });


        //Logic to calculate Gabi and Kenny difference
        PayNextTextView = v.findViewById(R.id.WhoPayTextView);
        final ArrayList<UserPaymentModel> kennyCompareArrayList = new ArrayList<>();
        final ArrayList<UserPaymentModel> gabiCompareArrayList = new ArrayList<>();
        db.collection("userspayment").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<UserPaymentModel> types = queryDocumentSnapshots.toObjects(UserPaymentModel.class);

                    kennyCompareArrayList.addAll(types);
                    gabiCompareArrayList.addAll(types);

                    double kennySum = 0;
                    for (UserPaymentModel u : kennyCompareArrayList) {
                        if (u.getName().equals("Kenny")) {
                            kennySum += Double.valueOf(u.getAmount());
                        }
                    }

                    Log.d(TAG, "onSuccess: KennySum " + kennySum);

                    double gabiSum = 0;
                    for (UserPaymentModel u : gabiCompareArrayList) {
                        if (u.getName().equals("Gabi")) {
                            gabiSum += Double.valueOf(u.getAmount());
                        }
                    }
                    Log.d(TAG, "onSuccess: GabiSUm " + gabiSum);

                    if (gabiSum > kennySum) {
                        PayNextTextView.setText("Kenny Pays Next");
                        Log.d(TAG, "onSuccess: Kenny Pays ");
                    } else if (gabiSum == kennySum) {
                        PayNextTextView.setText("Kenny Pays Next");
                        Log.d(TAG, "onSuccess: Kenny Pays ");
                    } else {
                        PayNextTextView.setText("Gabi Pays Next");
                        Log.d(TAG, "onSuccess: Gabi Pays ");
                    }
                }
            }
        });

        //PayNextTextView.setText();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Query query = db.collection("userspayment").orderBy("timestamp", Query.Direction.DESCENDING).limit(10);
        FirestoreRecyclerOptions<UserPaymentModel> options = new FirestoreRecyclerOptions.Builder<UserPaymentModel>()
                .setQuery(query, UserPaymentModel.class)
                .build();

        mAdapter = new

                MyAdapter(options);
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
