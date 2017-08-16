package com.akbar.dev.movieudacity.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akbar.dev.movieudacity.R;
import com.akbar.dev.movieudacity.model.Movie;
import com.akbar.dev.movieudacity.utils.Constant;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.akbar.dev.movieudacity.utils.Constant.EXTRA_MOVIE;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.img_poster_detail)ImageView mImageViewPoster;
    @BindView(R.id.backdrop)ImageView mImageViewBackdrop;
    @BindView(R.id.year)TextView mTextViewYear;
    @BindView(R.id.rating)TextView mTextViewRating;
    @BindView(R.id.overview)TextView mTextViewOverview;

    private Movie mMovie;

    private String extraMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDataIntent();

        bindData();
    }

    private void getDataIntent(){
        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            extraMovie = getIntent().getStringExtra(EXTRA_MOVIE);
        } else {
            throw new IllegalArgumentException(getString(R.string.intent_warning));
        }

        mMovie = new Gson().fromJson(extraMovie, Movie.class);
    }

    private void bindData(){
        setTitle(mMovie.getOriginalTitle());
        Picasso.with(this).load(Constant.MOVIE_POSTER_LINK + mMovie.getPosterPath())
                .placeholder(R.drawable.backdrop)
                .into(mImageViewPoster);
        Picasso.with(this).load(Constant.MOVIE_BACKDROP_LINK + mMovie.getBackdropPath())
                .placeholder(R.drawable.backdrop)
                .into(mImageViewBackdrop);
        mTextViewYear.setText(mMovie.getFormattedDate());
        mTextViewRating.setText(String.valueOf(mMovie.getVoteAverage()));
        mTextViewOverview.setText(mMovie.getOverview());
    }

    @OnClick(R.id.fab)
    public void doSomething(View view){
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}