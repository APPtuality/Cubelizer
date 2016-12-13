package com.app.apptuality.talentum.cubelizer.cubelizer.persistence;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import com.app.apptuality.talentum.cubelizer.cubelizer.R;
import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.Date;

public class Calendar extends HorizontalScrollView implements View.OnClickListener {

    public final static String TAG = Calendar.class.getSimpleName();

    /**
     * Constants
     */
    //Layouts
    private final int WIDGET_LAYOUT_RES_ID = R.layout.calendar_layout;
    private final int DAY_VIEW_LAYOUT_RES_ID = R.layout.day_layout;
    //Resource ids
    public static final int DAYS_CONTAINER_RES_ID = R.id.days_container;
    public static final int DAY_OF_WEEK_RES_ID = R.id.day_of_week;
    public static final int DAY_NUMBER_RES_ID = R.id.day_number;
    public static final int MONTH_NAME_RES_ID = R.id.month_short_name;
    //Delay
    public static final int DELAY_SELECTION = 300;
    public static final int NO_DELAY_SELECTION = 0;

    /**
     * Variables
     */
    //State
    Context mContext;
    LocalDateTime mStartDate;
    LocalDateTime mEndDate;
    LocalDateTime miSelect;
    int mSelectedDay;

    //Colors
    int mDayTextColor;
    int mSelectedDayTextColor;
    int mDaysContainerBackgroundColor;
    int mSelectedDayBackgroundColor;

    //Titles
    boolean mAlwaysDisplayMonth;
    boolean mDisplayDayOfWeek;

    //Listener
    DayViewOnClickListener mListener;
    public void setDayViewOnClickListener(DayViewOnClickListener listener) {
        mListener = listener;
    }
    public interface DayViewOnClickListener {
        //public String onDaySelected(Date day);
        public String onDaySelected(int day);

    }

    /* MI VARIABLE DEL AÑO */
    int miYear, miMonth;
    public static String fechaSelect;

    //Day View
    DayView mSelectedDayView;

    /**
     * Controls
     */
    Space mLeftSpace;
    LinearLayout mDaysContainer;
    Space mRightSpace;

    /**
     * Constructors
     */
    public Calendar(Context context) {
        super(context);
        init(context, null);
    }

    public Calendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Calendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Initialization
     */
    public void init(Context context, AttributeSet attributeSet) {
        mContext = context;

        //Init JodaTime
        JodaTimeAndroid.init(context);

        //Init Start and End date with current month
        final LocalDateTime currentDateTime = new LocalDateTime();
        Log.d("CurrentDate",currentDateTime.toString());
        miYear = currentDateTime.getYear();
        // int miMonth = currentDateTime.getMonthOfYear();
        int miDay = currentDateTime.getDayOfMonth();

        Log.d("CurrentDateDAY",String.valueOf(miYear+","+miMonth+","+miDay));
        setStartDateWithParts(currentDateTime.getYear(), currentDateTime.monthOfYear().withMinimumValue().getMonthOfYear(), currentDateTime.dayOfMonth().withMinimumValue().getDayOfMonth());
        //setStartDateWithParts(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.dayOfMonth().withMinimumValue().getDayOfMonth());
        setEndDateWithParts(currentDateTime.getYear(),currentDateTime.monthOfYear().withMaximumValue().getMonthOfYear(), currentDateTime.dayOfMonth().withMaximumValue().getDayOfMonth());
        //setEndDateWithParts(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.dayOfMonth().withMaximumValue().getDayOfMonth());

        //Inflate view
        View view = LayoutInflater.from(mContext).inflate(WIDGET_LAYOUT_RES_ID, this, true);

        //Get controls
        mDaysContainer = (LinearLayout) view.findViewById(DAYS_CONTAINER_RES_ID);

        //Get custom attributes
        mDisplayDayOfWeek = true;
        if(attributeSet != null) {
            TypedArray a = mContext.getTheme().obtainStyledAttributes(attributeSet, R.styleable.Calendar, 0, 0);

            try {

                //Colors
                mDayTextColor = a.getColor(R.styleable.Calendar_dayTextColor, getColor(R.color.default_day_text_color));
                mSelectedDayTextColor = a.getColor(R.styleable.Calendar_selectedDayTextColor, getColor(R.color.default_selected_day_text_color));

                mDaysContainerBackgroundColor = a.getColor(R.styleable.Calendar_daysContainerBackgroundColor, getColor(R.color.default_days_container_background_color));
                mSelectedDayBackgroundColor = a.getColor(R.styleable.Calendar_selectedDayBackgroundColor, getColor(R.color.default_selected_day_background_color));

                //Labels
                mAlwaysDisplayMonth = a.getBoolean(R.styleable.Calendar_alwaysDisplayMonth, false);
                mDisplayDayOfWeek = a.getBoolean(R.styleable.Calendar_displayDayOfWeek, true);

            } finally {
                a.recycle();
            }
        }

        //Setup styling
        //Days Container
        mDaysContainer.setBackgroundColor(mDaysContainerBackgroundColor);

        //Render control
        render();

        //Set Selection. Default is today.
        setSelectedDay(
                currentDateTime.getDayOfMonth(),
                false, DELAY_SELECTION);

       /* setSelectedDay(currentDateTime.getYear(),
                currentDateTime.getMonthOfYear(),
                currentDateTime.getDayOfMonth(),
                false, DELAY_SELECTION);
*/
    }

    /***
     * State modification
     */
    public void setStartAndEndDateWithParts(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        setStartDateWithParts(startYear, startMonth, startDay);
        setEndDateWithParts(endYear, endMonth, endDay);

        render();
    }

    private void setStartDateWithParts(int year, int month, int day) {
        mStartDate = new LocalDateTime(year, month, day, 0, 0, 0);
        Log.d("StartDate", mStartDate.toString());
        Log.d("StartDate", mStartDate.toString());

    }

    private void setEndDateWithParts(int year, int month, int day) {
        mEndDate = new LocalDateTime(year, month, day, 0, 0, 0);
    }

    public void setSelectedDay(final int day,final boolean notifyListeners, long delay) {
        //Post delayed 300 ms at most because of redraw
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Deselect day selected
                if(mSelectedDay > 0)
                    unSelectDay(mSelectedDay);

                //Set selected day
                mSelectedDay = day;
                selectDay(mSelectedDay);
                // miMonth = month;
                //Scroll to DayView
                scrollToDayView(mSelectedDayView);

                //Call listener
                if(notifyListeners && mListener != null)
                    mListener.onDaySelected(mSelectedDay);

                //   Log.d("listenerDay",String.valueOf( mListener.onDaySelected(mSelectedDay)));

            }
        }, delay);
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    /**
     * Ui
     */
    private int getColor(int colorResId) {
        return getResources().getColor(colorResId);
    }

    private void render() {
        //Get inflater for view
        LayoutInflater inflater = LayoutInflater.from(mContext);

        //Add left padding
        mLeftSpace = new Space(mContext);
        mDaysContainer.addView(mLeftSpace);

        //Cycle from start day
        LocalDateTime startDate = mStartDate;
        LocalDateTime endDate = mEndDate;

        String week = startDate.dayOfWeek().getAsText();//.getAsShortText().substring(0,3);
        Log.i("DayWeek", week);

        while (startDate.isBefore(endDate.plusDays(1))) {

            //Inflate view
            LinearLayout view = (LinearLayout) inflater.inflate(DAY_VIEW_LAYOUT_RES_ID, mDaysContainer, false);

            //new DayView
            DayView dayView = new DayView(view);

            //Set texts and listener
            dayView.setDayOfWeek(startDate.dayOfWeek().getAsShortText().substring(0,3));
            //String week = startDate.dayOfWeek().getAsShortText().substring(0,3);
            //Log.i("DayWeek", week);
            if(!mDisplayDayOfWeek)
                dayView.hideDayOfWeek();

            dayView.setDay(startDate.getDayOfMonth());
            dayView.setMonthShortName(startDate.monthOfYear().getAsShortText().substring(0,3));
            //MI METODO
            dayView.setMonthNumero(startDate.monthOfYear().get());

            //Hide month if range in same month
            if (!mAlwaysDisplayMonth && startDate.getMonthOfYear() == endDate.getMonthOfYear())
                dayView.hideMonthShortName();

            //Set style
            dayView.setTextColor(mDayTextColor);

            //Set listener
            dayView.setOnClickListener(this);

            //Add to container
            mDaysContainer.addView(dayView.getView());

            //Next day
            startDate = startDate.plusDays(1);



            /* prueba de codigo raro*/
            //  Log.d("viewGetDay", String.valueOf(dayView.getDay()));
            //Log.d("viewGetDay", String.valueOf(dayView.getDay()));
            //dayView.getDay();
        }

        //Add right padding
        mRightSpace = new Space(mContext);
        mDaysContainer.addView(mRightSpace);
    }

    private void unSelectDay(int day) {
        for (int i = 1; i < mDaysContainer.getChildCount() - 1; i++) {
            DayView dayView = new DayView(mDaysContainer.getChildAt(i));
            if(dayView.getDay() == day) {
                dayView.setTextColor(mDayTextColor);
                dayView.setBackgroundColor(0);
                return;
            }
        }
    }

/*
    if(day.isEmpty()) {
        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        day = year + "-" + month + "-" + date.getDate();

    } else {
        day = (String) extras.get("day");
    }
    */

    private void selectDay(int day) {
        for (int i = 1; i < mDaysContainer.getChildCount() - 1; i++) {
            //Log.d("Contenedor",String.valueOf(mDaysContainer.getChildCount()));
            DayView dayView = new DayView(mDaysContainer.getChildAt(i));
            //Log.d("ContenedorDayView",String.valueOf(mDaysContainer.getChildAt(i)));
            if(dayView.getDay() == day) {

                dayView.setTextColor(mSelectedDayTextColor);
                dayView.setBackgroundColor(mSelectedDayBackgroundColor);

                // mSelectedDayView = dayView;
                mSelectedDayView = dayView;

                return;
            }
        }
    }

    public String miSelectionDay(int year, String month, int day) {

        String mes = month;
        String mesNumero = "";
        switch (mes){
            case "Jan":
                mesNumero = "01";
                break;
            case "Feb":
                mesNumero = "02";
                break;
            case "Mar":
                mesNumero = "03";
                break;
            case "Apr":
                mesNumero = "04";
                break;
            case "May":
                mesNumero = "05";
                break;
            case "Jun":
                mesNumero = "06";
                break;
            case "Jul":
                mesNumero = "07";
                break;
            case "Aug":
                mesNumero = "08";
                break;
            case "Sep":
                mesNumero = "09";
                break;
            case "Oct":
                mesNumero = "10";
                break;
            case "Nov":
                mesNumero = "11";
                break;
            case "Dec":
                mesNumero = "12";
                break;
            case "Ene":
                mesNumero = "01";
                break;
            case "Abr":
                mesNumero = "04";
                break;
            case "Ago":
                mesNumero = "08";
                break;
            case "Dic":
                mesNumero = "12";
                break;
        }
        String sYear = String.format("%04d", year);
        String sDia = String.format("%02d", day);
        fechaSelect = String.format(sYear+"-"+mesNumero+"-"+sDia);
        Log.i("SeleccionDAY", fechaSelect);

        return fechaSelect;
    }

    public void scrollToDayView(DayView dayView) {
        int x = dayView.getView().getLeft();
        int y = dayView.getView().getTop();
        smoothScrollTo(x - mLeftSpace.getLayoutParams().width, y);
    }

    /**
     * On DayView click listener
     */
    @Override
    public void onClick(View view) {
        //Get day view
        DayView dayView = new DayView(view);

        //Get selected day and set selection
        int selectedDay = dayView.getDay();
        String sMonth = dayView.getMonthMy();

        miSelectionDay(miYear,sMonth,selectedDay);


        setSelectedDay(selectedDay, true, NO_DELAY_SELECTION);
        // Log.i("setSelectedDay**",String.valueOf(selectedDay)+", "+sMonth);


        // miSelectionDay(iYear,iMonth,selectedDay);
        Log.i("MiSeleccion**",String.valueOf(miYear+","+miMonth+", "+selectedDay));
        Log.i("MiSeleccionCon mont**",String.valueOf(miMonth));
    }

    /**
     * Custom implementation for left and right spaces
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed) {
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

            LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) mLeftSpace.getLayoutParams();
            leftParams.width = getWidth() / 2 - padding;
            mLeftSpace.setLayoutParams(leftParams);

            LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) mRightSpace.getLayoutParams();
            rightParams.width = getWidth() / 2 - padding;
            mRightSpace.setLayoutParams(rightParams);
        }
    }

    /**
     * Configuration change handling
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.setSelectedDay(mSelectedDay);
        savedState.setStartDateString(mStartDate.toString());
        savedState.setEndDateString(mEndDate.toString());

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mSelectedDay = savedState.getSelectedDay();
        mStartDate = LocalDateTime.parse(savedState.getStartDateString());
        mEndDate = LocalDateTime.parse(savedState.getEndDateDateString());

        render();

        //setSelectedDay(mSelectedDay, false, DELAY_SELECTION);
    }

    protected static class SavedState extends BaseSavedState {
        int mSelectedDay;

        String mStartDateString;
        String mEndDateString;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel in) {
            super(in);

            mSelectedDay = in.readInt();
            mStartDateString = in.readString();
            mEndDateString = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mSelectedDay);
            out.writeString(mStartDateString);
            out.writeString(mEndDateString);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public void setSelectedDay(int selectedDay) {
            mSelectedDay = selectedDay;
        }

        public void setStartDateString(String startDateString) {
            mStartDateString = startDateString;
        }

        public void setEndDateString(String endDateString) {
            mEndDateString = endDateString;
        }

        public int getSelectedDay() {
            return mSelectedDay;
        }

        public String getStartDateString() {
            return mStartDateString;
        }

        public String getEndDateDateString() {
            return mEndDateString;
        }
    }

    /**
     * DateView class
     */
    public static class DayView {

        int mDay;

        LinearLayout mView;
        TextView mDayOfWeek;
        TextView mDayNumber;
        TextView mMonthShortName;

        int numberM;

        public DayView(View view) {
            mView = (LinearLayout) view;

            mDayOfWeek = (TextView) mView.findViewById(DAY_OF_WEEK_RES_ID);
            mDayNumber = (TextView) mView.findViewById(DAY_NUMBER_RES_ID);
            mMonthShortName = (TextView) mView.findViewById(MONTH_NAME_RES_ID);
        }

        public int getDay() {
            return Integer.parseInt(mDayNumber.getText().toString());
        }


        public String getYearMy() {
            return mDayOfWeek.getText().toString();
        }

        public String getMonthMy() {

            String miMes = mMonthShortName.getText().toString();

            return mMonthShortName.getText().toString();
        }
        public void setMonthNumero(int monthNumber) {
            // mMonthShortName.setText(monthNumber);
            numberM = monthNumber;

            String month = String.format("%02d", monthNumber);
        }





        public void setDay(int day) {
            mDay = day;
            setDayNumber(String.format("%02d", day));
        }

        public void setDayOfWeek(String dayOfWeek) {
            mDayOfWeek.setText(dayOfWeek);
        }

        public void setDayNumber(String dayNumber) {
            mDayNumber.setText(dayNumber);
        }

        public void setMonthShortName(String monthShortName) {
            mMonthShortName.setText(monthShortName);
        }





        public void setBackgroundColor(int color) {
            mView.setBackgroundColor(color);
        }

        public void setTextColor(int color) {
            mDayOfWeek.setTextColor(color);
            mDayNumber.setTextColor(color);
            mMonthShortName.setTextColor(color);
        }

        public void setOnClickListener(OnClickListener listener) {
            mView.setOnClickListener(listener);
        }

        public View getView() {
            return mView;
        }

        public void hideDayOfWeek() {
            mDayOfWeek.setVisibility(View.GONE);
        }

        public void hideMonthShortName() {
            mMonthShortName.setVisibility(View.GONE);
        }

    }

}