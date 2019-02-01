package app.hamcr7.mapr.cadinz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NxListAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<String> mainTitle;
    private Animation animation = null;
    private LayoutInflater mLayoutInflater;

    public NxListAdapter(Context context, ArrayList<String> mainTitle) {

        this.mainTitle=mainTitle;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public String getItem(int position) {
        return mainTitle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mainTitle.size();
    }
    @Override
    public View getView(int position,View view,ViewGroup parent) {
        // create a ViewHolder reference
        ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();

            view = mLayoutInflater.inflate(R.layout.blog_row, parent, false);

            // get all views you need to handle from the cell and save them in the view holder
            holder.itemName =  view.findViewById(R.id.cutTextView);

            // save the view holder on the cell view to get it back latter
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }

        //get the string item from the position "position" from array list to put it on the TextView
        String stringItem = mainTitle.get(position);
        if (stringItem != null) {
            //set the item name on the TextView
            holder.itemName.setText(stringItem);
        } else {
            // make sure that when you have an if statement that alters the UI, you always have an else that sets a default value back, otherwise you will find that the recycled items will have the same UI changes
            holder.itemName.setText("Unknown");
        }

        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
        animation.setDuration(1000);
        view.startAnimation(animation);
        //this method must return the view corresponding to the data at the specified position.
        return view;




    };

    private class ViewHolder {

        private TextView itemName;

    }
}
