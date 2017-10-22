package ru.sergeykamyshov.fivecards.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.sergeykamyshov.fivecards.R;

public class CardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mData;

    public CardViewAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardHolder cardHolder = (CardHolder) holder;
        cardHolder.mTextView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CardHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public CardHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_card_title);
        }
    }
}
