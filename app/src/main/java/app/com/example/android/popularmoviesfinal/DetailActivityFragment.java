package app.com.example.android.popularmoviesfinal;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String posterUl = "http://image.tmdb.org/t/p/w185/";

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MovieString")&& intent.hasExtra("Position")) {
            String movieString = intent.getStringExtra("MovieString");
            String position = (intent.getStringExtra("Position"));
            int positionInt = Integer.valueOf(position);
            ArrayList<Movie> movies = Movie.getMovies(movieString);
            ((TextView) rootView.findViewById(R.id.MovieTitle)).setText(movies.get(positionInt).getTitle());
            ImageView imageView = ((ImageView) rootView.findViewById(R.id.MoviePoster));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            Picasso.with(getActivity()).load(posterUl + movies.get(positionInt).getPoster_path()).into(imageView);
            ((TextView) rootView.findViewById(R.id.Plot)).setText(movies.get(positionInt).getOverview());
            ((TextView) rootView.findViewById(R.id.UserRating)).setText("User Rating:   " + movies.get(positionInt).getVote_average());
            ((TextView) rootView.findViewById(R.id.ReleaseDate)).setText("Release Date:   " + movies.get(positionInt).getRelease_date());
        }
        return rootView;
    }
}
