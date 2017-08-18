package com.suntechnology.churchapp.fragment;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Created by Flexy on 10/6/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    DateCommunicator dateUp;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dateUp = (DateCommunicator) getActivity();
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //Get yesterday's date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);

//Set yesterday time milliseconds as date pickers minimum date
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, this, year, month, day);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {


        // Do something with the date chosen by the user
        Calendar setDateAndTimeCalender = Calendar.getInstance();
        setDateAndTimeCalender.set(Calendar.YEAR, year);
        setDateAndTimeCalender.set(Calendar.MONTH, month);
        setDateAndTimeCalender.set(Calendar.DAY_OF_MONTH, day);

            dateUp.updateDateTxt(setDateAndTimeCalender);


    }

   public interface DateCommunicator{
        void updateDateTxt(Calendar cal);

    }


}
