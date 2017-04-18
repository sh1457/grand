package sh1457.test.com.grand;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class GestureAdapter extends RecyclerView.Adapter<GestureAdapter.GestureHolder> {
    private ArrayList<Gesture> mGestures = new ArrayList<>();

    class GestureHolder extends RecyclerView.ViewHolder {
        //    private final ImageView gIcon;
        TextView gString;

        GestureHolder(View itemView) {
            super(itemView);
//            gIcon = (ImageView) itemView.findViewById(R.id.gesture_icon);
            gString = (TextView) itemView.findViewById(R.id.gesture_string);
        }

        void bindGesture(Gesture gesture) {
//            gIcon.setImageResource(gesture.getIcon());
            gString.setText(gesture.getString());
        }
    }

    void add(int position, Gesture item) {
        mGestures.add(position, item);
        notifyItemInserted(position);
    }

    private void remove(Gesture item) {
        int position = mGestures.indexOf(item);
        mGestures.remove(position);
        notifyItemRemoved(position);
    }

    GestureAdapter(ArrayList<Gesture> myGestures) {
        mGestures = myGestures;
    }

    @Override
    public GestureHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gesture_list_row, parent, false);
        return new GestureHolder(view);
    }

    @Override
    public void onBindViewHolder(GestureHolder holder, int pos) {
        final Gesture gesture = mGestures.get(pos);
        holder.bindGesture(gesture);
        holder.gString.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(gesture);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGestures.size();
    }
}
