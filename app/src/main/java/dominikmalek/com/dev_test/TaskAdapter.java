package dominikmalek.com.dev_test;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {
    private List<Task> mTaskList;
    private List<Task> mTaskListFiltered;
    private Context mContext;
    private RecyclerView mRecyclerV;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView idTV;
        public TextView nameTV;
        public TextView statusTV;
        public Button actionButton;
        private TaskDBHelper dbHelper;
        private List<Task> mTaskList;
        Task taskToShow;
        Task mTask;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_row_layout, parent, false));

            idTV = (TextView) itemView.findViewById(R.id.idTV);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            statusTV=(TextView) itemView.findViewById(R.id.statusTV);
            actionButton = (Button) itemView.findViewById(R.id.actionButton);



        }

        public void bind(Task task) {
            mTask = task;
            idTV.setText(String.valueOf(task.getId()));
            nameTV.setText(task.getName());
            statusTV.setText(task.getStatus());
            dbHelper = TaskDBHelper.getInstanse(mContext);


            actionButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(mTask.getStatus().equals("OPEN")){
                        dbHelper.updateStatus(mTask.getId(),"TRAVELLING");
                    }else if(mTask.getStatus().equals("TRAVELLING")){
                        dbHelper.updateStatus(mTask.getId(),"WORKING");
                    }else{
                        dbHelper.updateStatus(mTask.getId(),"OPEN");
                    }
                    notifyDataSetChanged();
                    mTaskList=dbHelper.taskList("");

                    for(Task t:mTaskList){
                        if(mTask.getId()==t.getId()){
                            taskToShow=t;
                        }
                    }

                    statusTV.setText(mTask.getStatus());
                }

            });

        }


        }



    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskAdapter(List<Task> myDataset, Context context, RecyclerView recyclerView) {
        mTaskList = myDataset;
        mTaskListFiltered=myDataset;
        mRecyclerV = recyclerView;
        mContext=context;
        setHasStableIds(true);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater, parent);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
                    mTaskListFiltered=mTaskList;
                } else {
                    List<Task> filteredList = new ArrayList<Task>();
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
                mTaskListFiltered= (List<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}



