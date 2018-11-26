package dominikmalek.com.dev_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TaskDBHelper dbHelper;
    private TaskAdapter adapter;
    private String filter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //populate recyclerview
        populateRecyclerView(filter);
    }

    private void populateRecyclerView(String filter){
        dbHelper = TaskDBHelper.getInstanse(this);
        //dbHelper.addSomeFlats();
        adapter = new TaskAdapter(dbHelper.taskList(filter),this,mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        populateRecyclerView(filter);
    }
}
