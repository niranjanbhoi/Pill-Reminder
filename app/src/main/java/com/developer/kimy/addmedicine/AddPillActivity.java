package com.developer.kimy.addmedicine;

import android.os.PersistableBundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.developer.kimy.Injection;
import com.developer.kimy.R;
import com.developer.kimy.utils.ActivityUtils;

public class AddPillActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    public static final String EXTRA_TASK_ID = "task_extra_id";
    public static final String EXTRA_TASK_NAME = "task_extra_name";

    private AddPillPresenter mAddPillPresenter;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        //Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        AddPillFragment addPillFragment = (AddPillFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        int medId = getIntent().getIntExtra(AddPillFragment.ARGUMENT_EDIT_MEDICINE_ID,0);
        String medName = getIntent().getStringExtra(AddPillFragment.ARGUMENT_EDIT_MEDICINE_NAME);

        setToolbarTitle(medName);


        if (addPillFragment == null) {
            addPillFragment = AddPillFragment.newInstance();

            if (getIntent().hasExtra(AddPillFragment.ARGUMENT_EDIT_MEDICINE_ID)) {
                Bundle bundle = new Bundle();
                bundle.putInt(AddPillFragment.ARGUMENT_EDIT_MEDICINE_ID, medId);
                addPillFragment.setArguments(bundle);
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addPillFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;
        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

//        // Create the presenter
        mAddPillPresenter = new AddPillPresenter(
                medId,
                Injection.provideMedicineRepository(getApplicationContext()),
                addPillFragment,
                shouldLoadDataFromRepo);

    }

    public void setToolbarTitle(String medicineName) {
        if (medicineName == null) {
            mActionBar.setTitle(getString(R.string.new_medicine));
        } else {
            mActionBar.setTitle(medicineName);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddPillPresenter.isDataMissing());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
