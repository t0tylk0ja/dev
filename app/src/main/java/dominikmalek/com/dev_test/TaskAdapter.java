package dominikmalek.com.dev_test;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {
    private List<Task> mTaskList;
    private List<Task> mTaskListFiltered;
    private Context mContext;
    private String mFilter;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DatabaseObserver {
        // each data item is just a string in this case
        TextView idTV;
        TextView nameTV;
        TextView statusTV;
        Button actionButton;
        LinearLayout linearLayout;
        private TaskDBHelper dbHelper;
        private List<Task> mTaskList;
        Task taskToShow;
        Task mTask;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_row_layout, parent, false));

            idTV = itemView.findViewById(R.id.idTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            actionButton = itemView.findViewById(R.id.actionButton);
            linearLayout = itemView.findViewById(R.id.LL);

            actionButton.setOnClickListener(this);
            dbHelper = TaskDBHelper.getInstance(mContext);
            dbHelper.addListener(this);//dodanie obserwatora
        }

        void bind(Task task) {
            mTask = task;
            idTV.setText(String.valueOf(task.getId()));
            nameTV.setText(task.getName());
            statusTV.setText(task.getStatus());
            if(isWorking()==true){
                actionButton.setVisibility(View.GONE);
            }else{
                actionButton.setVisibility(View.VISIBLE);
            }

            switch (mTask.getStatus()) {
                case "TRAVELLING":
                    actionButton.setBackgroundResource(R.color.buttonK2);
                    linearLayout.setBackgroundResource(R.color.buttonK2L);
                    break;
                case "WORKING":
                    actionButton.setBackgroundResource(R.color.buttonK3);
                    linearLayout.setBackgroundResource(R.color.buttonK3L);
                    break;
                default:
                    actionButton.setBackgroundResource(R.color.buttonK1);
                    linearLayout.setBackgroundResource(R.color.buttonK1L);
                    break;
            }
        }

        @Override
        public void onClick (View view) {
            if (view.getId() == actionButton.getId()) {
                handleStatusChangeOnClick();
            }
        }

        private void handleStatusChangeOnClick() {
            //if (isWorking()==true){
             //   Toast.makeText(mContext,"Another task is working !",Toast.LENGTH_LONG).show();
            //}
            //else {
                switch (mTask.getStatus()) {
                    case "OPEN":
                        dbHelper.updateStatus(mTask.getId(), "TRAVELLING");
                        actionButton.setText("START WORK");
                        break;
                    case "TRAVELLING":
                        dbHelper.updateStatus(mTask.getId(), "WORKING");
                        actionButton.setText("STOP");
                        break;
                    default:
                        dbHelper.updateStatus(mTask.getId(), "OPEN");
                        actionButton.setText("START TRAVEL");
                        break;
                }
                notifyDataSetChanged();
                mTaskList = dbHelper.taskList("");

                for (Task t : mTaskList) {
                    if (mTask.getId() == t.getId()) {
                        taskToShow = t;
                    }
                }
            //}
        }

        private boolean isWorking(){
            mTaskList = dbHelper.taskList("");
            boolean working = false;
            for (Task t : mTaskList) {
                if (t.getId() != mTask.getId()) {
                    if (t.getStatus().equals("WORKING") || t.getStatus().equals("TRAVELLING")) {
                        working = true;
                    }

                }
            }
            return working;
        }

        @Override
        public void onDatabaseChanged() {
            mTaskList = dbHelper.taskList(mFilter);
            mTaskListFiltered = dbHelper.taskList(mFilter);
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    TaskAdapter(List<Task> myDataset, Context context, String filter) {
        mTaskList = myDataset;
        mTaskListFiltered = myDataset;
        mFilter = filter;
        mContext = context;
        setHasStableIds(true);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater, parent);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Task task = mTaskListFiltered.get(position);
        holder.bind(task);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTaskListFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mTaskListFiltered = mTaskList;
                } else {
                    List<Task> filteredList = new ArrayList<>();
                    for (Task row : mTaskList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mTaskListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTaskListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTaskListFiltered = (List<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}