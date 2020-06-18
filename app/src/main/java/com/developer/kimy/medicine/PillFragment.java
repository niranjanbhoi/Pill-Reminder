package com.developer.kimy.medicine;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.developer.kimy.R;
import com.developer.kimy.addmedicine.AddPillActivity;
import com.developer.kimy.data.source.MedicineAlarm;
import com.developer.kimy.views.RobotoLightTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by gautam on 13/07/17.
 */

public class PillFragment extends Fragment implements PillContract.View, PillAdapter.OnItemClickListener {

    @BindView(R.id.medicine_list)
    RecyclerView rvMedList;

    Unbinder unbinder;

    @BindView(R.id.noMedIcon)
    ImageView noMedIcon;

    @BindView(R.id.noMedText)
    RobotoLightTextView noMedText;

    @BindView(R.id.add_med_now)
    TextView addMedNow;

    @BindView(R.id.no_med_view)
    View noMedView;

    @BindView(R.id.progressLoader)
    ProgressBar progressLoader;


    private PillContract.Presenter presenter;

    private PillAdapter pillAdapter;


    public PillFragment() {

    }

    public static PillFragment newInstance() {
        Bundle args = new Bundle();
        PillFragment fragment = new PillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pillAdapter = new PillAdapter(new ArrayList<>(0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        unbinder = ButterKnife.bind(this, view);
        setAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(v -> presenter.addNewMedicine());
    }

    private void setAdapter() {
        rvMedList.setAdapter(pillAdapter);
        rvMedList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedList.setHasFixedSize(true);
        pillAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        presenter.onStart(day);
    }

    @Override
    public void setPresenter(PillContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        progressLoader.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMedicineList(List<MedicineAlarm> medicineAlarmList) {
        pillAdapter.replaceData(medicineAlarmList);
        rvMedList.setVisibility(View.VISIBLE);
        noMedView.setVisibility(View.GONE);
    }

    @Override
    public void showAddMedicine() {
        Intent intent = new Intent(getContext(), AddPillActivity.class);
        startActivityForResult(intent, AddPillActivity.REQUEST_ADD_TASK);
    }


    @Override
    public void showMedicineDetails(long taskId, String medName) {
        Intent intent = new Intent(getContext(), AddPillActivity.class);
        intent.putExtra(AddPillActivity.EXTRA_TASK_ID, taskId);
        intent.putExtra(AddPillActivity.EXTRA_TASK_NAME, medName);
        startActivity(intent);
    }


    @Override
    public void showLoadingMedicineError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoMedicine() {
        showNoTasksViews(
                getResources().getString(R.string.no_medicine_added)
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_me_message));
    }

    @Override
    public void showMedicineDeletedSuccessfully() {
        showMessage(getString(R.string.successfully_deleted_message));
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        presenter.onStart(day);
    }

    private void showMessage(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.add_med_now)
    void addMedicine() {
        showAddMedicine();
    }

    private void showNoTasksViews(String mainText) {
        rvMedList.setVisibility(View.GONE);
        noMedView.setVisibility(View.VISIBLE);
        noMedText.setText(mainText);
        noMedIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_my_health));
        addMedNow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void onMedicineDeleteClicked(MedicineAlarm medicineAlarm) {
        presenter.deleteMedicineAlarm(medicineAlarm, getActivity());
    }
}

