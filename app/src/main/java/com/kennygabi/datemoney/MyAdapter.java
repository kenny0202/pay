package com.kennygabi.datemoney;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyAdapter extends FirestoreRecyclerAdapter<UserPaymentModel, MyAdapter.PaymentHolder> {

    public MyAdapter(@NonNull FirestoreRecyclerOptions<UserPaymentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PaymentHolder holder, int position, @NonNull UserPaymentModel model) {
        holder.textViewTitle.setText(model.getUsername());
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_payment_record,
                parent, false);
        return new PaymentHolder(v);
    }

    class PaymentHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;

        public PaymentHolder(View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewRecycler);
        }
    }

}