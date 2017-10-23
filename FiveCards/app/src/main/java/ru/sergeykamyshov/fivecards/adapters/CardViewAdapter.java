package ru.sergeykamyshov.fivecards.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.sergeykamyshov.fivecards.R;
import ru.sergeykamyshov.fivecards.model.CardType;
import ru.sergeykamyshov.fivecards.model.CommentType;
import ru.sergeykamyshov.fivecards.model.PostType;
import ru.sergeykamyshov.fivecards.model.UsersType;

public class CardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_POST = 1;
    private static final int VIEW_TYPE_COMMENT = 2;
    private static final int VIEW_TYPE_USERS = 3;

    private Context mContext;
    private List<CardType> mData;

    public CardViewAdapter(Context context, List<CardType> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_POST:
                View postView = LayoutInflater.from(mContext).inflate(R.layout.post_item_layout, parent, false);
                return new PostTypeHolder(postView);
            case VIEW_TYPE_COMMENT:
                View commentView = LayoutInflater.from(mContext).inflate(R.layout.comment_item_layout, parent, false);
                return new CommentTypeHolder(commentView);
            case VIEW_TYPE_USERS:
                View usersView = LayoutInflater.from(mContext).inflate(R.layout.users_item_layout, parent, false);
                return new UsersTypeHolder(usersView);
            default:
                View defaultView = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
                return new CardHolder(defaultView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_POST:
                PostTypeHolder postHolder = (PostTypeHolder) holder;
                PostType postType = (PostType) mData.get(position);
                postHolder.mPostTitle.setText(postType.getTitle());
                postHolder.mPostBody.setText(postType.getBody());
                break;
            case VIEW_TYPE_COMMENT:
                CommentTypeHolder commentTypeHolder = (CommentTypeHolder) holder;
                CommentType commentType = (CommentType) mData.get(position);
                commentTypeHolder.mName.setText(commentType.getName());
                commentTypeHolder.mEmail.setText(commentType.getEmail());
                commentTypeHolder.mBody.setText(commentType.getBody());
                break;
            case VIEW_TYPE_USERS:
                UsersTypeHolder usersTypeHolder = (UsersTypeHolder) holder;
                UsersType usersType = (UsersType) mData.get(position);
                List<String> users = usersType.getUsers();
                usersTypeHolder.mUsers.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, users));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_POST;
            case 1:
                return VIEW_TYPE_COMMENT;
            case 2:
                return VIEW_TYPE_USERS;
            default:
                return VIEW_TYPE_DEFAULT;
        }
    }

    /**
     * Обновляет данные адаптера
     *
     * @param data - новый список карточек, который необходимо отобразить
     */
    public void updateData(List<CardType> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Holder для постов
     */
    private static class PostTypeHolder extends RecyclerView.ViewHolder {
        TextView mPostTitle;
        TextView mPostBody;

        public PostTypeHolder(View postView) {
            super(postView);
            mPostTitle = postView.findViewById(R.id.text_post_title);
            mPostBody = postView.findViewById(R.id.text_post_body);
        }
    }

    /**
     * Holder для комментариев
     */
    private static class CommentTypeHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mEmail;
        TextView mBody;

        public CommentTypeHolder(View commentView) {
            super(commentView);
            mName = commentView.findViewById(R.id.text_comment_name);
            mEmail = commentView.findViewById(R.id.text_comment_email);
            mBody = commentView.findViewById(R.id.text_comment_body);
        }
    }

    /**
     * Holder для списка пользователей
     */
    private static class UsersTypeHolder extends RecyclerView.ViewHolder {
        ListView mUsers;

        public UsersTypeHolder(View usersView) {
            super(usersView);
            mUsers = usersView.findViewById(R.id.list_users);
        }
    }

    private static class CardHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public CardHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_card_title);
        }
    }
}
