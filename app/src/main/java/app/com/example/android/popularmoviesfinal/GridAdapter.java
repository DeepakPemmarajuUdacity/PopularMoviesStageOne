package app.com.example.android.popularmoviesfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public final class GridAdapter extends BaseAdapter {
    private static final String posterUl = "http://image.tmdb.org/t/p/w185/";

    // Assume it's known
    private static final int ROW_ITEMS = 3;
    final ArrayList<String> mItems;
    final int mCount;

    /**
     * Default constructor
     * @param items to fill data to
     */
    public GridAdapter(final ArrayList<String> items) {

        mCount = items.size();// * ROW_ITEMS;
        mItems = new ArrayList<String>(mCount);

        // for small size of items it's ok to do it here, sync way
        for (String item : items) {
            // get separate string parts, divided by ,
            //final String[] parts = item.split(",");

            // remove spaces from parts
            //for (String part : parts) {
              //  part.replace(" ", "");
                mItems.add(item);
            //}
        }
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        if(position < mItems.size()){
            Picasso.with(parent.getContext()).load(posterUl + mItems.get(position)).into(imageView);
        }

        return imageView;
    }
}
