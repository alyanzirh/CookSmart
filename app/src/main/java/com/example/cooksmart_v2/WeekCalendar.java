package com.example.cooksmart_v2;

import static com.example.cooksmart_v2.CalendarUtils.daysInWeekArray;
import static com.example.cooksmart_v2.CalendarUtils.monthYearFromDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WeekCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView, listLunch, listDinner, listSnack;
    // private Button generateMP;
    private ImageView btnBwd, btnFwd, btnBack;
    List<MealPlanEntry> dataList;
    // mpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_calendar);
        initWidgets();
        setWeekView();

        // generateMP = (Button) findViewById(R.id.generateMP);
        btnBwd = (ImageView) findViewById(R.id.btnBwd);
        btnFwd = (ImageView) findViewById(R.id.btnFwd);
        btnBack = (ImageView) findViewById(R.id.btnBack);

        /* generateMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateMealPlan.class);
                startActivity(intent);
            }
        }); */

        eventListView = findViewById(R.id.eventListView);
        listLunch = findViewById(R.id.listLunch);
        listDinner = findViewById(R.id.listDinner);
        listSnack = findViewById(R.id.listSnack);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mealPlanDatabase = FirebaseDatabase.getInstance().getReference().child("mealPlan").child(userId);

        /* mealPlanDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MealPlanEntry> mealPlanEntries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                    if (mealPlanEntry != null) {
                        mealPlanEntry.setKey(snapshot.getKey());
                        mealPlanEntries.add(mealPlanEntry);
                    }
                }

                EventAdapter eventAdapter = new EventAdapter(WeekCalendar.this, mealPlanEntries);
                eventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        }); */

        btnBwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        btnFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        listLunch = findViewById(R.id.listLunch);
        listDinner = findViewById(R.id.listDinner);
        listSnack = findViewById(R.id.listSnack);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // Load meal plan entries for the selected date
        loadMealPlanEntriesForDate(CalendarUtils.selectedDate);
    }

    private void loadMealPlanEntriesForDate(LocalDate selectedDate) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mealPlanDatabase = FirebaseDatabase.getInstance().getReference().child("mealPlan").child(userId);

        // Convert the LocalDate to a string format that matches the database structure
        String dateString = selectedDate.toString(); // Assuming your database uses ISO date format

        DatabaseReference dateRef = mealPlanDatabase.child(dateString);
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MealPlanEntry> breakfastEntries = new ArrayList<>();
                List<MealPlanEntry> lunchEntries = new ArrayList<>();
                List<MealPlanEntry> dinnerEntries = new ArrayList<>();
                List<MealPlanEntry> snackEntries = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                    if (mealPlanEntry != null) {
                        mealPlanEntry.setKey(snapshot.getKey());
                        if ("Breakfast".equals(mealPlanEntry.getMeal())) {
                            breakfastEntries.add(mealPlanEntry);
                        }
                        if ("Lunch".equals(mealPlanEntry.getMeal())) {
                            lunchEntries.add(mealPlanEntry);
                        }
                        if ("Dinner".equals(mealPlanEntry.getMeal())) {
                            dinnerEntries.add(mealPlanEntry);
                        }
                        if ("Snack".equals(mealPlanEntry.getMeal())) {
                            snackEntries.add(mealPlanEntry);
                        }
                    }
                }

                // Set the meal plan entries to the adapter
                EventAdapter eventAdapter = new EventAdapter(WeekCalendar.this, breakfastEntries);
                eventListView.setAdapter(eventAdapter);

                EventAdapter lunchAdapter = new EventAdapter(WeekCalendar.this, lunchEntries);
                listLunch.setAdapter(lunchAdapter);

                EventAdapter dinnerAdapter = new EventAdapter(WeekCalendar.this, dinnerEntries);
                listDinner.setAdapter(dinnerAdapter);

                EventAdapter snackAdapter = new EventAdapter(WeekCalendar.this, snackEntries);
                listSnack.setAdapter(snackAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    /* private void loadMealPlanEntriesForDate(LocalDate selectedDate) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mealPlanDatabase = FirebaseDatabase.getInstance().getReference().child("mealPlan").child(userId);

        // Convert the LocalDate to a string format that matches the database structure
        String dateString = selectedDate.toString(); // Assuming your database uses ISO date format

        DatabaseReference dateRef = mealPlanDatabase.child(dateString);
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MealPlanEntry> mealPlanEntries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                    if (mealPlanEntry != null) {
                        mealPlanEntry.setKey(snapshot.getKey());
                        mealPlanEntries.add(mealPlanEntry);
                    }
                }

                // Set the meal plan entries to the adapter
                EventAdapter eventAdapter = new EventAdapter(WeekCalendar.this, mealPlanEntries);
                eventListView.setAdapter(eventAdapter);
                listLunch.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    } */

    // old
    /* private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        // setEventAdpater();
    } */

    // unuse
    /* private void loadRecipesForDate(Calendar calendar) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mealPlanDatabase = FirebaseDatabase.getInstance().getReference().child("mealPlan").child(userId);

        // Pass the calendar object to the ValueEventListener
        mealPlanDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MealPlanEntry> mealPlanEntries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                    if (mealPlanEntry != null) {
                        mealPlanEntry.setKey(snapshot.getKey());
                        // Compare the selected date with the date in the MealPlanEntry
                        Calendar entryDate = Calendar.getInstance();
                        entryDate.setTime(Date.from(Instant.parse(mealPlanEntry.getDate())));
                        if (calendar.get(Calendar.YEAR) == entryDate.get(Calendar.YEAR)
                                && calendar.get(Calendar.MONTH) == entryDate.get(Calendar.MONTH)
                                && calendar.get(Calendar.DAY_OF_MONTH) == entryDate.get(Calendar.DAY_OF_MONTH)) {
                            mealPlanEntries.add(mealPlanEntry);
                        }
                    }
                }

                EventAdapter eventAdapter = new EventAdapter(WeekCalendar.this, mealPlanEntries);
                eventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    } */


    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // setEventAdpater();
    }

    /* private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    } */
}

/* public class WeekCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private static final String TAG = "WeekCalendar";

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Button generateMP;
    private ImageView btnBwd, btnFwd;

    private List<MealPlanEntry> mealPlanEntries;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_calendar);

        initWidgets();
        setWeekView();

        // generateMP.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreateMealPlan.class)));

        btnBwd = (ImageView) findViewById(R.id.btnBwd);
        btnFwd = (ImageView) findViewById(R.id.btnFwd);

        btnBwd.setOnClickListener(view -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
            setWeekView();
        });

        btnFwd.setOnClickListener(view -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
            setWeekView();
        });

        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            // You can handle the item click event here
            // For example, you can show a dialog with more details about the event
            Toast.makeText(this, "Event clicked: " + mealPlanEntries.get(position).getRecipeName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // Convert LocalDate to Calendar
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(
                CalendarUtils.selectedDate.getYear() + 1900, // Year
                CalendarUtils.selectedDate.getMonthValue() - 1, // Month (0-based)
                CalendarUtils.selectedDate.getDayOfMonth()); // Day of the month

        loadRecipesForDate(selectedDate);
    }

    private void loadRecipesForDate(Calendar calendar) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mealPlanDatabase = FirebaseDatabase.getInstance().getReference().child("mealPlan").child(userId);

        mealPlanDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mealPlanEntries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                    if (mealPlanEntry != null) {
                        mealPlanEntry.setKey(snapshot.getKey());
                        Calendar entryDate = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date parsedDate = dateFormat.parse(mealPlanEntry.getDate());
                            entryDate.setTime(parsedDate);
                        } catch (ParseException e) {
                            // Handle parsing exception
                        }
                        if (calendar.get(Calendar.YEAR) == entryDate.get(Calendar.YEAR)
                                && calendar.get(Calendar.MONTH) == entryDate.get(Calendar.MONTH)
                                && calendar.get(Calendar.DAY_OF_MONTH) == entryDate.get(Calendar.DAY_OF_MONTH)) {
                            mealPlanEntries.add(mealPlanEntry);
                        }
                    }
                }

                eventAdapter = new EventAdapter(WeekCalendar.this, mealPlanEntries);
                eventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        // Convert LocalDate to Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());

        // Save the selected date
        // CalendarUtils.selectedDate = calendar;

        // Load recipes for the selected date
        loadRecipesForDate(calendar);

        // Update the displayed week view
        setWeekView();
    }

} */