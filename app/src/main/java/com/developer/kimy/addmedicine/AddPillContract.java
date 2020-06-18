package com.developer.kimy.addmedicine;

import com.developer.kimy.BasePresenter;
import com.developer.kimy.BaseView;
import com.developer.kimy.data.source.MedicineAlarm;
import com.developer.kimy.data.source.Pills;

import java.util.List;

/**
 * Created by gautam on 12/07/17.
 */

public interface AddPillContract {

    interface View extends BaseView<Presenter> {

        void showEmptyMedicineError();

        void showMedicineList();

        boolean isActive();

    }

    interface  Presenter extends BasePresenter{


        void saveMedicine(MedicineAlarm alarm, Pills pills);


        boolean isDataMissing();

        boolean isMedicineExits(String pillName);

        long addPills(Pills pills);

        Pills getPillsByName(String pillName);

        List<MedicineAlarm> getMedicineByPillName(String pillName);

        List<Long> tempIds();

        void deleteMedicineAlarm(long alarmId);

    }
}
