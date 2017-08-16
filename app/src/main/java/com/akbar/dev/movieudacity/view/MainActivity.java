package com.akbar.dev.movieudacity.view;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.akbar.dev.movieudacity.R;
import com.akbar.dev.movieudacity.adapter.MoviesAdapter;
import com.akbar.dev.movieudacity.api.ApiClient;
import com.akbar.dev.movieudacity.api.ApiInterface;
import com.akbar.dev.movieudacity.model.Movie;
import com.akbar.dev.movieudacity.model.MovieResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.akbar.dev.movieudacity.utils.Constant.API_KEY;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.my_recycler_view)RecyclerView mRecyclerViewMovie;
    @BindView(R.id.refresh)SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_message_display)TextView mTextViewMessage;
    @BindView(R.id.toolbar)Toolbar toolbar;

    private String[] category;

    private SpinnerAdapter spinnerAdapter;
    private Spinner navigationSpinner;
    private LinearLayoutManager layoutManager;

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        category = getResources().getStringArray(R.array.category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.api_warning, Toast.LENGTH_LONG).show();
            return;
        }

        initComponents();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String selected = navigationSpinner.getSelectedItem().toString();

                Log.d(TAG, selected);

                if (selected.equals(getString(R.string.title_popular))){
                    loadMovieData(0);
                } else if (selected.equals(getString(R.string.title_top_rated))){
                    loadMovieData(1);
                }
            }
        });

    }

    private void loadMovieData(int position){
        showMovieDataView();

        if (position == 0){
            callMovieData(apiService.getPopularMovies(API_KEY));
        }else if (position == 1){
            callMovieData(apiService.getTopRatedMovies(API_KEY));
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void showAbout() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.view_bottom_sheet_about, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void callMovieData(Call<MovieResponse> movieResponseCall){
        mRefreshLayout.setRefreshing(true);
        Call<MovieResponse> call = movieResponseCall;
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                mRecyclerViewMovie.setAdapter(new MoviesAdapter(movies, getApplicationContext()));
                Log.d(TAG, movies.toString());
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                showErrorMessage();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initComponents(){
        layoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns());
        mRecyclerViewMovie.setHasFixedSize(true);
        mRecyclerViewMovie.setLayoutManager(layoutManager);
        mRecyclerViewMovie.setItemAnimator(new DefaultItemAnimator());

        spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.spinner_dropdown_item);
        navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(spinnerAdapter);
        toolbar.addView(navigationSpinner, 0);

        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        loadMovieData(0);
                        break;
                    case 1:
                        loadMovieData(1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mTextViewMessage.setVisibility(View.INVISIBLE);
        // COMPLETED (44) Show mRecyclerView, not mWeatherTextView
        /* Then, make sure the weather data is visible */
        mRecyclerViewMovie.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        // COMPLETED (44) Hide mRecyclerView, not mWeatherTextView
        /* First, hide the currently visible data */
        mRecyclerViewMovie.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mTextViewMessage.setVisibility(View.VISIBLE);
        mTextViewMessage.setText(R.string.error_message);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_about){
            showAbout();
        }
        return super.onOptionsItemSelected(item);
    }
}
