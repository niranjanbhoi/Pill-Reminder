package com.developer.kimy.alarm;

import com.developer.kimy.BasePresenter;
import com.developer.kimy.BaseView;
import com.developer.kimy.data.source.PillHistory;
import com.developer.kimy.data.source.MedicineAlarm;

/**
 * Created by gautam on 13/07/17.
 */

public interface ReminderContract {

    interface View extends BaseView<Presenter> {

        void showMedicine(MedicineAlarm medicineAlarm);

        void showNoData();

        boolean isActive();

        void onFinish();

    }

    interface Presenter extends BasePresenter {

        void finishActivity();

        void onStart(long id);

        void loadMedicineById(long id);

        void addPillsToHistory(PillHistory pillHistory);

    }
}
