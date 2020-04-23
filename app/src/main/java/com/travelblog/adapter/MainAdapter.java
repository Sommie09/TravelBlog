package com.travelblog.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.travelblog.R;
import com.travelblog.http.Blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Specify model and view-holder
public class MainAdapter extends ListAdapter<Blog, MainAdapter.MainViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(Blog blog);
    }

    private OnItemClickListener clickListener;

    private List<Blog> originalList = new ArrayList<>();

    public MainAdapter(OnItemClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    //Executes according to the number of list items that can appear on the screen
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_name, parent, false);
        return new MainViewHolder(view, clickListener);
    }

    //Bind model to view holder
    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    public void setData(@Nullable List<Blog> list) {
        originalList = list;
        super.submitList(list);
    }

    public void filter(String query) {
        List<Blog> filteredList = new ArrayList<>();
        for (Blog blog : originalList) {  //Iterate through the original list
            if (blog.getTitle().toLowerCase().contains(query.toLowerCase())) { //Compare blog title to the query
                filteredList.add(blog);
            }
        }
        submitList(filteredList); //Submit matched item tot he adapter
    }

    public void sortByTitle() {
        List<Blog> currentList = new ArrayList<>(getCurrentList());
        Collections.sort(currentList,
                (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
        submitList(currentList);
    }

    public void sortByDate() {
        List<Blog> currentList = new ArrayList<>(getCurrentList());
        Collections.sort(currentList,
                (o1, o2) -> o2.getDateMillis().compareTo(o1.getDateMillis()));
        submitList(currentList);
    }

    //ViewHolder
    static class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textDate;
        private ImageView imageAvatar;
        private Blog blog;

        MainViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(v -> listener.onItemClicked(blog));
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
        }

        void bindTo(Blog blog) {
            this.blog = blog;
            textTitle.setText(blog.getTitle());
            textDate.setText(blog.getDate());

            Glide.with(itemView)
                    .load(blog.getAuthor().getAvatarURL())
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageAvatar);
        }
    }


    private static final DiffUtil.ItemCallback<Blog> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Blog>() {
                @Override
                public boolean areItemsTheSame(@NonNull Blog oldData,
                                               @NonNull Blog newData) {
                    return oldData.getId() == newData.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull Blog oldData,
                                                  @NonNull Blog newData) {
                    return oldData.equals(newData);
                }
            };



















}
