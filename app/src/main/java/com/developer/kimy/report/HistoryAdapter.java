package com.developer.kimy.report;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.developer.kimy.R;
import com.developer.kimy.data.source.PillHistory;
import com.developer.kimy.views.RobotoBoldTextView;
import com.developer.kimy.views.RobotoRegularTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gautam on 13/07/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {


    private List<PillHistory> mPillHistoryList;

    HistoryAdapter(List<PillHistory> pillHistoryList) {
        setList(pillHistoryList);
    }

    void replaceData(List<PillHistory> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    private void setList(List<PillHistory> pillHistoryList) {
        this.mPillHistoryList = pillHistoryList;
    }


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        PillHistory pillHistory = mPillHistoryList.get(position);
        if (pillHistory == null) {
            return;
        }
        holder.tvMedDate.setText(pillHistory.getFormattedDate());
        setMedicineAction(holder, pillHistory.getAction());
        holder.tvMedicineName.setText(pillHistory.getPillName());
        holder.tvDoseDetails.setText(pillHistory.getFormattedDose());

    }

    private void setMedicineAction(HistoryViewHolder holder, int action) {
        switch (action) {
            case 0:
            default:
                holder.ivMedicineAction.setVisibility(View.GONE);
                break;
            case 1:
                holder.ivMedicineAction.setVisibility(View.VISIBLE);
                holder.ivMedicineAction.setImageResource(R.drawable.image_reminder_taken);
                break;
            case 2:
                holder.ivMedicineAction.setImageResource(R.drawable.image_reminder_not_taken);
                holder.ivMedicineAction.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mPillHistoryList != null && !mPillHistoryList.isEmpty()) ? mPillHistoryList.size() : 0;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_med_date)
        RobotoBoldTextView tvMedDate;

        @BindView(R.id.tv_medicine_name)
        RobotoBoldTextView tvMedicineName;

        @BindView(R.id.tv_dose_details)
        RobotoRegularTextView tvDoseDetails;

        @BindView(R.id.iv_medicine_action)
        ImageView ivMedicineAction;

        HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
