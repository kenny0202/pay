package com.kennygabi.datemoney;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RadioButton radioSelectedButton;
    private RadioGroup group;
    private EditText input;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PaymentViewPagerAdapter adapter = new PaymentViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                group = dialogView.findViewById(R.id.radioPersonGroup);
                builder.setTitle("Who Paid")
                        .setView(dialogView)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int selectedId = group.getCheckedRadioButtonId();
                                radioSelectedButton = dialogView.findViewById(selectedId);

                                input = dialogView.findViewById(R.id.editText2);

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                String date = sdf.format(System.currentTimeMillis());

                                Map<String, Object> userPaymentCollection = new HashMap<>();
                                userPaymentCollection.put("name", radioSelectedButton.getText().toString());
                                userPaymentCollection.put("amount", input.getText().toString());
                                userPaymentCollection.put("timestamp", date);

                                db.collection("userspayment").document()
                                        .set(userPaymentCollection)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("MainActivity", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("MainActivity", "Error writing document", e);
                                            }
                                        });

                                Toast.makeText(MainActivity.this,
                                        radioSelectedButton.getText() + " paid $" + input.getText().toString(), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    static class PaymentViewPagerAdapter extends FragmentStatePagerAdapter {

        public PaymentViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PaymentMain();
                case 1:
                    return new PaymentGabi();
                case 2:
                    return new PaymentKenny();
                case 3:
                    return new PaymentGraph();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Summary";
                case 1:
                    return "Gabi";
                case 2:
                    return "Kenny";
                case 3:
                    return "Graph";
            }
            return null;
        }
    }
}
