package ru.sergeykamyshov.fivecards.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.sergeykamyshov.fivecards.R;
import ru.sergeykamyshov.fivecards.asynctasks.CommentAsyncTask;
import ru.sergeykamyshov.fivecards.asynctasks.PostAsyncTask;
import ru.sergeykamyshov.fivecards.model.CardType;
import ru.sergeykamyshov.fivecards.model.CommentType;
import ru.sergeykamyshov.fivecards.model.ImageType;
import ru.sergeykamyshov.fivecards.model.PostType;
import ru.sergeykamyshov.fivecards.model.TodoType;
import ru.sergeykamyshov.fivecards.model.UsersType;

public class CardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_POST = 1;
    private static final int VIEW_TYPE_COMMENT = 2;
    private static final int VIEW_TYPE_USERS = 3;
    private static final int VIEW_TYPE_IMAGE = 4;
    private static final int VIEW_TYPE_TODO = 5;

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
            case VIEW_TYPE_IMAGE:
                View imageView = LayoutInflater.from(mContext).inflate(R.layout.image_item_layout, parent, false);
                return new ImageTypeHolder(imageView);
            case VIEW_TYPE_TODO:
                View todoView = LayoutInflater.from(mContext).inflate(R.layout.todo_item_layout, parent, false);
                return new TodoTypeHolder(todoView);
            default:
                View defaultView = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
                return new CardHolder(defaultView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_POST:
                final PostTypeHolder postHolder = (PostTypeHolder) holder;
                PostType postType = (PostType) mData.get(position);
                postHolder.mPostTitle.setText(postType.getTitle());
                postHolder.mPostBody.setText(postType.getBody());
                postHolder.mSubmitAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String postNumber = postHolder.mPostNumber.getText().toString();
                        if (postNumber.isEmpty()) {
                            return;
                        } else if (Integer.valueOf(postNumber) > 100) {
                            Toast.makeText(mContext, mContext.getString(R.string.toast_msg_post_number), Toast.LENGTH_LONG).show();
                            return;
                        }
                        new PostAsyncTask(CardViewAdapter.this).execute(postNumber);
                    }
                });
                break;
            case VIEW_TYPE_COMMENT:
                final CommentTypeHolder commentTypeHolder = (CommentTypeHolder) holder;
                CommentType commentType = (CommentType) mData.get(position);
                commentTypeHolder.mName.setText(commentType.getName());
                commentTypeHolder.mEmail.setText(commentType.getEmail());
                commentTypeHolder.mBody.setText(commentType.getBody());
                commentTypeHolder.mSubmitAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String commentNumber = commentTypeHolder.mCommentNumber.getText().toString();
                        if (commentNumber.isEmpty()) {
                            return;
                        } else if (Integer.valueOf(commentNumber) > 500) {
                            Toast.makeText(mContext, mContext.getString(R.string.toast_msg_comment_number), Toast.LENGTH_LONG).show();
                            return;
                        }
                        new CommentAsyncTask(CardViewAdapter.this).execute(commentNumber);
                    }
                });
                break;
            case VIEW_TYPE_USERS:
                UsersTypeHolder usersTypeHolder = (UsersTypeHolder) holder;
                UsersType usersType = (UsersType) mData.get(position);
                List<String> users = usersType.getUsers();
                usersTypeHolder.mUsers.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, users));
                break;
            case VIEW_TYPE_IMAGE:
                ImageTypeHolder imageTypeHolder = (ImageTypeHolder) holder;
                ImageType imageType = (ImageType) mData.get(position);
                imageTypeHolder.mImageView.setImageBitmap(imageType.getImage());
                break;
            case VIEW_TYPE_TODO:
                TodoTypeHolder todoTypeHolder = (TodoTypeHolder) holder;
                TodoType todoType = (TodoType) mData.get(position);
                todoTypeHolder.mTitle.setText(todoType.getTitle());
                todoTypeHolder.mCompleted.setChecked(todoType.isCompleted());
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
            case 3:
                return VIEW_TYPE_IMAGE;
            case 4:
                return VIEW_TYPE_TODO;
            default:
                return VIEW_TYPE_DEFAULT;
        }
    }

    public List<CardType> getData() {
        return mData;
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
        TextView mSubmitAction;
        EditText mPostNumber;

        public PostTypeHolder(View postView) {
            super(postView);
            mPostTitle = postView.findViewById(R.id.text_post_title);
            mPostBody = postView.findViewById(R.id.text_post_body);
            mSubmitAction = postView.findViewById(R.id.text_post_submit_action);
            mPostNumber = postView.findViewById(R.id.edit_text_post_number);
        }
    }

    /**
     * Holder для комментариев
     */
    private static class CommentTypeHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mEmail;
        TextView mBody;
        TextView mSubmitAction;
        EditText mCommentNumber;

        public CommentTypeHolder(View commentView) {
            super(commentView);
            mName = commentView.findViewById(R.id.text_comment_name);
            mEmail = commentView.findViewById(R.id.text_comment_email);
            mBody = commentView.findViewById(R.id.text_comment_body);
            mSubmitAction = commentView.findViewById(R.id.text_comment_submit_action);
            mCommentNumber = commentView.findViewById(R.id.edit_text_comment_number);
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

    /**
     * Holder для изображения
     */
    private static class ImageTypeHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ImageTypeHolder(View imageView) {
            super(imageView);
            mImageView = imageView.findViewById(R.id.image_view);
        }
    }

    /**
     * Holder для списка дел
     */
    private static class TodoTypeHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        CheckBox mCompleted;

        public TodoTypeHolder(View todoView) {
            super(todoView);
            mTitle = todoView.findViewById(R.id.text_todo_title);
            mCompleted = todoView.findViewById(R.id.checkbox_todo_completed);
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
