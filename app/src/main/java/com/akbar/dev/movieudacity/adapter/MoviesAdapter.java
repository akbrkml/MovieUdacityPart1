package com.akbar.dev.movieudacity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.akbar.dev.movieudacity.R;
import com.akbar.dev.movieudacity.model.Movie;
import com.akbar.dev.movieudacity.model.MovieResponse;
import com.akbar.dev.movieudacity.utils.Constant;
import com.akbar.dev.movieudacity.view.DetailMovieActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.akbar.dev.movieudacity.utils.Constant.EXTRA_MOVIE;

/**
 * Created by kamal on 08/02/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    private List<Movie> mMovies;
    private Context mContext;

    public class MoviesHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.gambar_poster) ImageView mImageViewPosterMovie;

        public MoviesHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MoviesAdapter(List<Movie> movies, Context context){
        this.mContext = context;
        this.mMovies = movies;
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);
        MoviesHolder moviesHolder = new MoviesHolder(rowView);
        return moviesHolder;
    }

    @Override
    public void onBindViewHolder(MoviesHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Picasso.with(mContext).load(Constant.MOVIE_POSTER_LINK + mMovies.get(position).getPosterPath())
                .placeholder(R.drawable.backdrop)
                .into(holder.mImageViewPosterMovie);

        final String extraMovie = new Gson().toJson(movie, Movie.class);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailMovieActivity.class);
                intent.putExtra(EXTRA_MOVIE, extraMovie);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
