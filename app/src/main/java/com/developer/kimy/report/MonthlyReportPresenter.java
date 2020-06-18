package com.developer.kimy.report;

import androidx.annotation.NonNull;

import com.developer.kimy.data.source.PillHistory;
import com.developer.kimy.data.source.MedicineDataSource;
import com.developer.kimy.data.source.MedicineRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautam on 13/07/17.
 */

public class MonthlyReportPresenter implements MonthlyReportContract.Presenter {


    private final MedicineRepository mMedicineRepository;

    private final MonthlyReportContract.View mMonthlyReportView;

    private FilterType mCurrentFilteringType = FilterType.ALL_MEDICINES;

    public MonthlyReportPresenter(@NonNull MedicineRepository medicineRepository, MonthlyReportContract.View monthlyReportView) {
        this.mMedicineRepository = medicineRepository;
        this.mMonthlyReportView = monthlyReportView;
        mMonthlyReportView.setPresenter(this);
    }


    @Override
    public void start() {
        loadHistory(true);
    }


    @Override
    public void loadHistory(boolean showLoading) {
        loadHistoryFromDb(showLoading);
    }

    private void loadHistoryFromDb(final boolean showLoading) {
        if (showLoading) {
            mMonthlyReportView.setLoadingIndicator(true);
        }
        mMedicineRepository.getMedicineHistory(new MedicineDataSource.LoadHistoryCallbacks() {
            @Override
            public void onHistoryLoaded(List<PillHistory> pillHistoryList) {
                List<PillHistory> pillHistoryShowList = new ArrayList<>();

                //We will filter the PillHistory based on request type
                for (PillHistory pillHistory : pillHistoryList) {
                    switch (mCurrentFilteringType) {
                        case ALL_MEDICINES:
                            pillHistoryShowList.add(pillHistory);
                            break;
                        case TAKEN_MEDICINES:
                            if (pillHistory.getAction() == 1) {
                                pillHistoryShowList.add(pillHistory);
                            }
                            break;
                        case IGNORED_MEDICINES:
                            if (pillHistory.getAction() == 2) {
                                pillHistoryShowList.add(pillHistory);
                            }
                            break;
                    }
                }
                processHistory(pillHistoryShowList);
                if (!mMonthlyReportView.isActive()) {
                    return;
                }
                if (showLoading) {
                    mMonthlyReportView.setLoadingIndicator(false);
                }

            }

            @Override
            public void onDataNotAvailable() {
                if (!mMonthlyReportView.isActive()) {
                    return;
                }
                if (showLoading) {
                    mMonthlyReportView.setLoadingIndicator(false);
                }
                mMonthlyReportView.showLoadingError();
            }
        });

    }

    private void processHistory(List<PillHistory> pillHistoryList) {

        if (pillHistoryList.isEmpty()) {
            // Show a message indicating there are no history for that filter type.
            processEmptyHistory();
        } else {
            //Show the list of history
            mMonthlyReportView.showHistoryList(pillHistoryList);
            //Set filter label's text
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFilteringType) {
            case ALL_MEDICINES:
                mMonthlyReportView.showAllFilterLabel();
                break;
            case TAKEN_MEDICINES:
                mMonthlyReportView.showTakenFilterLabel();
                break;
            case IGNORED_MEDICINES:
                mMonthlyReportView.showIgnoredFilterLabel();
                break;
            default:
                mMonthlyReportView.showAllFilterLabel();
        }
    }


    private void processEmptyHistory() {
        switch (mCurrentFilteringType) {
            case ALL_MEDICINES:
                mMonthlyReportView.showNoHistory();
                break;
            case TAKEN_MEDICINES:
                mMonthlyReportView.showNoTakenHistory();
                break;
            case IGNORED_MEDICINES:
                mMonthlyReportView.showNoIgnoredHistory();
                break;
            default:
                mMonthlyReportView.showNoHistory();
                break;
        }
    }


    @Override
    public void setFiltering(FilterType filterType) {
        mCurrentFilteringType = filterType;
    }

    @Override
    public FilterType getFilterType() {
        return mCurrentFilteringType;
    }
}
