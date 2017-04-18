package sh1457.test.com.grand;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionHolder> {
    private ArrayList<Action> mActions = new ArrayList<>();

    class ActionHolder extends RecyclerView.ViewHolder {
        //ImageView gIcon;
        TextView aString;

        ActionHolder(View itemView) {
            super(itemView);
            //gIcon = (ImageView) itemView.findViewById(R.id.gesture_icon);
            aString = (TextView) itemView.findViewById(R.id.action_string);
        }

        void bindAction(Action action) {
            aString.setText(action.getString());
        }
    }

    void add(int position, Action item) {
        mActions.add(position, item);
        notifyItemInserted(position);
    }

    private void remove(Action item) {
        int position = mActions.indexOf(item);
        mActions.remove(position);
        notifyItemRemoved(position);
    }

    ActionAdapter(ArrayList<Action> myActions) {
        mActions = myActions;
    }

    @Override
    public ActionHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_list_row, parent, false);
        return new ActionHolder(view);
    }

    @Override
    public void onBindViewHolder(ActionHolder holder, int pos) {
        final Action action = mActions.get(pos);
        holder.bindAction(action);
        holder.aString.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(action);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }
}
