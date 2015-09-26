package app.com.example.android.popularmoviesfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    // Will contain the raw JSON response as a string.
    String popularMoviesJsonStr = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    private void updateMovieList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.sort_order_key),"popularity" );
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(sortOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            String sortOrder = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = null;
                if(sortOrder.equalsIgnoreCase("popularity")){
                    url = new URL("http://api.themoviedb.org/3/discover/movie?api_key=<API_KEY>&sort_by=popularity.desc");
                }
                else{
                    url = new URL("http://api.themoviedb.org/3/discover/movie?api_key=<API_KEY>&sort_by=vote_average.desc");
                }


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                try{
                    urlConnection.connect();
                }
                catch (Exception e){
                    throw e;
                }

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popularMoviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return popularMoviesJsonStr;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            ArrayList<Movie> movies = Movie.getMovies(result);
            if(movies == null) {
                Intent intent = new Intent(getActivity(), UnableToRetrieveData.class);
                startActivity(intent);
            }
            else{
                ArrayList<String> posters = new ArrayList<String>();
                for(int i=0;i<movies.size();i++){
                    posters.add(movies.get(i).getPoster_path());
                }
                GridView gridview = (GridView) getActivity().findViewById(R.id.MoviesGrid);
                gridview.setAdapter(new GridAdapter(posters));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class)
                                .putExtra("MovieString", popularMoviesJsonStr);
                        intent.putExtra("Position",String.valueOf(position));
                        startActivity(intent);
                    }
                });
            }
        }


    }
}
