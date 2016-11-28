package com.app.talentum.cubelizer.cubelizer.calendario;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import com.app.talentum.cubelizer.cubelizer.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daboom on 28/11/2016.
 */

public class CalendarActivity extends AppCompatActivity  {
    private Button btnSelectedDay;
    private Button btnCalendar;
    private GridView calendarViewScroll;
    private GridCellAdapter gridCellAdapter;
    private TextView txtV;
    private Calendar _calendar;
    private int dayOfWeek, month, year;

    private static String dateComplet;

    private final DateFormat dateFormater = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private final SimpleDateFormat dateFormatDayWeek = new SimpleDateFormat("EEEE");
    private static final String templateDayWeek = "EEEE";

    private String dayWeek;
    Date date = new Date();
    private Date parsedDate;
    private final String dayName = dateFormatDayWeek.format(date);

    private static final String tag = "CalendarHorizontal";
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        setContentView(R.layout.grid_day_calendar);
        context = this;

        //btnSelectedDay = (Button)findViewById(R.id.btnSelectedDay);
        btnCalendar = (Button)findViewById(R.id.btnCalendar);

        calendarViewScroll = (GridView)findViewById(R.id.gridViewCalendar);
        txtV = (TextView)findViewById(R.id.txtViewP);

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        dayOfWeek = _calendar.get(Calendar.DAY_OF_WEEK);

        // Initialised
        gridCellAdapter = new GridCellAdapter(getApplicationContext(),
                R.id.btncalendar_day_gridcell, month, year);
        gridCellAdapter.notifyDataSetChanged();

        calendarViewScroll.setAdapter(gridCellAdapter);
    }
    private void setGridCellAdapterToDate(int month, int year) {
        gridCellAdapter = new GridCellAdapter(getApplicationContext(),
                R.id.btncalendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        Log.d("fechaaaaa",dateComplet.toString());

        txtV.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        gridCellAdapter.notifyDataSetChanged();
        calendarViewScroll.setAdapter(gridCellAdapter);
    }

   // @Override
    public void onClick(View v) {

        if (v == btnCalendar) {

            DialogFragment newFragment = new DateFragmentPicker();
            newFragment.show(context.getFragmentManager(), "datePicker");
            //Llama a la clase showCalendar mostrarCalendar();
        }
    }

    @SuppressLint("ValidFragment")
    protected class DateFragmentPicker extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Usar del defecto la fecha actual
            final Calendar c = Calendar.getInstance();
            try {
                // Si en algun momento se ha informado la fecha se recupera
                String format = "MM-dd-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                c.setTime(sdf.parse(String.valueOf(txtV.getText())));
            } catch (Exception e) {
                // Si falla utilizaremos la fecha actual
            }

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        /**
         * Recupera el valor seleccionado en el componente DatePicker e inserta el valor en el
         * TextView tvDate
         *
         * @param view
         * @param year
         * @param month
         * @param day
         */
        public void onDateSet(DatePicker view, int year, int month, int day) {
            try{
                final Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                String format = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                txtV.setText(sdf.format(c.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* Clase adaptador de las celdas */
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {

        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Dom", "Lun", "Mar",
                "Mie", "Jue", "Vie", "Sab"};
        String weekday = new DateFormatSymbols().getShortWeekdays()[dayOfWeek];

        private final String[] months = {"January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button btnGridcell;
        private TextView txtV_num_day;
        private TextView txtV_nom_dayOfWeek;
        private final HashMap<String, Integer> eventsPerMonthMap;

        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");
        // Days in Current Month

        /* Constructor de la clase */
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

            Log.d("pruebaGridCEllAdapter", "New Calendar:= " + calendar.getTime().toString());
            Log.d("pruebaGridCEllAdapter", "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d("pruebaGridCEllAdapter", "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);

        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                for(int i=0;i<8;i++){
                    LayoutInflater inflater = (LayoutInflater) _context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.grid_day_calendar, parent, false);
                }
            }

            // Get a reference to the Day gridcell
            btnGridcell = (Button) row.findViewById(R.id.btncalendar_day_gridcell);
            btnGridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            // Log.d("Num dias", String.valueOf(day_color[0].length()));
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    txtV_num_day = (TextView) row
                            .findViewById(R.id.txtV_num_day_month);

                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    txtV_num_day.setText(numEvents.toString());

                  /*  txtV_nom_dayOfWeek = (TextView) row
                            .findViewById(R.id.txtV_nom_day_week);
                    txtV_nom_dayOfWeek.setText(weekdays[getCurrentWeekDay()]);
                    */


                   /* int currentWeekDay = _calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    weekday = getWeekDayAsString(currentWeekDay);
                    txtV_nom_dayOfWeek.setText(weekday);
                    Log.d("weeeeee",weekday);
                    */
                }
            }

            // Set the Day GridCell
            btnGridcell.setText(theday);
            btnGridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);

            if (day_color[1].equals("GREY")) {
                btnGridcell.setTextColor(getResources()
                        .getColor(R.color.lightgray));
            }
            if (day_color[1].equals("WHITE")) {
                btnGridcell.setTextColor(getResources().getColor(
                        R.color.lightgray02));
            }
            if (day_color[1].equals("BLUE")) {
                btnGridcell.setTextColor(getResources().getColor(R.color.orrange));
            }
            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            txtV.setText("Selected: " + date_month_year);
            Log.e("Selected date", date_month_year);
            try {
                setParsedDate(simpleDateFormat.parse(date_month_year));
                Log.d(tag, "Parsed Date: " + parsedDate.toString());
                setDateComplet(getParsedDate().toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }

    }

    public static String getDateComplet() {
        return dateComplet;
    }

    public static void setDateComplet(String dateComplet) {
        CalendarActivity.dateComplet = dateComplet;
    }

    public Date getParsedDate() {
        return parsedDate;
    }

    public void setParsedDate(Date parsedDate) {
        this.parsedDate = parsedDate;
    }

    public String sendDate(){
        String fecha;
        if (getDateComplet() == ""){
            fecha = "2016-09-10";
        } else {
            fecha = getDateComplet();
        }
        return fecha;
    }
}
