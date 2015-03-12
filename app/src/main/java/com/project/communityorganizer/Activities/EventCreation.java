package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.project.communityorganizer.Constants;
import com.project.communityorganizer.JSON.models.EventJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.RestService;
import com.project.communityorganizer.Services.SaveSharedPreference;
import com.project.communityorganizer.sqlite.models.Event;
import com.project.communityorganizer.sqlite.models.Geofence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by
 * @author Seshagiri on 26/2/15.
 */
public class EventCreation extends Activity implements View.OnClickListener {
    private EditText eventName, eventDescription, personalFeeling;
    private EditText startDate, startTime, endDate, endTime;
    private Button createButton;
    private Spinner spinner;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);

    public static String EVENT_CREATION_TITLE, EVENT_CREATION_MESSAGE;
    public static boolean EVENT_CREATION_STATUS;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        setActionBar();
        findViewsById();
        addItemsOnSpinner();
        onClickListener();
    }

    /**
     * Adds Geofence details to the spinner
     */
    private void addItemsOnSpinner() {
        List<Geofence> geofence = Geofence.getAllGeofenceDetails();
        List<String> geofenceList = new ArrayList<>();
        for (Geofence geofence1: geofence) {
            geofenceList.add(String.valueOf(geofence1.getGid()) + ", " + geofence1.getFence_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, geofenceList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * Handles the clicks on date/time items
     */
    private void onClickListener() {
        startDate.setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             * @param v
             */
            @Override
            public void onClick(View v) {
                Calendar mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(EventCreation.this,
                        new DatePickerDialog.OnDateSetListener() {
                            /**
                             * {@inheritDoc}
                             * @param view
                             * @param year
                             * @param monthOfYear
                             * @param dayOfMonth
                             */
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        startDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Start Date");
                mDatePicker.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             * @param v
             */
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(EventCreation.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            /**
                             * {@inheritDoc}
                             * @param view
                             * @param hourOfDay
                             * @param minute
                             */
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime.setText(new StringBuilder()
                                .append(pad(hourOfDay)).append(":").append(pad(minute)));
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select Start Time");
                timePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             * @param v
             */
            @Override
            public void onClick(View v) {
                Calendar mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(EventCreation.this,
                        new DatePickerDialog.OnDateSetListener() {
                            /**
                             * {@inheritDoc}
                             * @param view
                             * @param year
                             * @param monthOfYear
                             * @param dayOfMonth
                             */
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        endDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select End Date");
                mDatePicker.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             * @param v
             */
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(EventCreation.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            /**
                             * {@inheritDoc}
                             * @param view
                             * @param hourOfDay
                             * @param minute
                             */
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime.setText(new StringBuilder()
                                .append(pad(hourOfDay)).append(":").append(pad(minute)));
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select Start Time");
                timePickerDialog.show();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (validate()) new CreateEventASync().execute();
            }
        });
    }

    /**
     * Padding '0' if the time is single digit
     */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    /**
     * Validates the input field
     * @return
     */
    private boolean validate() {
        if(eventName.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Fill event name",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (eventDescription.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Fill event name",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * A function to initialize the edit views of the form
     */
    private void findViewsById() {
        eventName = (EditText) findViewById(R.id.eteventname);
        eventDescription = (EditText) findViewById(R.id.etMessage);
        personalFeeling = (EditText) findViewById(R.id.etpersonfeeling);
        startDate = (EditText) findViewById(R.id.create_start_date);
        startTime = (EditText) findViewById(R.id.create_start_time);
        endDate = (EditText) findViewById(R.id.create_end_date);
        endTime = (EditText) findViewById(R.id.create_end_time);
        createButton = (Button) findViewById(R.id.btn_create_event);
        spinner = (Spinner) findViewById(R.id.eventCreationGeofenceSpinner);
    }

    /**
     * Sets action bar
     */
    private void setActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Create an event");
            actionBar.setLogo(R.drawable.ic_action_light_event);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     * @param v
     */
    @Override
    public void onClick(View v) {
    }

    /**
     * Asynchronous task to register an event in the server side
     */
    private class CreateEventASync extends AsyncTask<Void, Void, Integer> {
        ProgressDialog dialog = ProgressDialog.show(
                EventCreation.this,
                "Please wait",
                "Event Creation in progress");

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * {@inheritDoc}
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                createEvent();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 1;
        }

        /**
         * {@inheritDoc}
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                if (dialog.isShowing()) dialog.dismiss();
                showResultDialog();
            }
        }

        /**
         * A function to display the result of creation
         */
        private void showResultDialog() {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    EVENT_CREATION_TITLE + "\n" + EVENT_CREATION_MESSAGE,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            if (EVENT_CREATION_STATUS) {
                EventCreation.this.finish();
            }
        }

        /**
         * Contacts server and does the event creation procedure
         * @return
         */
        private boolean createEvent() throws ParseException {
            final EventJSONModel eventJSONModel = new EventJSONModel();
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat(Constants.DATE_TIME_FORMAT3, Locale.US);

            eventJSONModel.setEvent_name(eventName.getText().toString());
            eventJSONModel.setEvent_description(eventDescription.getText().toString());
            eventJSONModel.setPersonal_feeling(personalFeeling.getText().toString());
            eventJSONModel.setEvent_creator(SaveSharedPreference.getUserEmail(EventCreation.this));

            String start_date = startDate.getText().toString();
            String start_time = startTime.getText().toString();
            String startDateTime = start_date + start_time;
            Date startDate = simpleDateFormat.parse(startDateTime);
            eventJSONModel.setStart_time(startDate);

            String end_date = endDate.getText().toString();
            String end_time = endTime.getText().toString();
            String endDateTime = end_date + end_time;
            Date endDate = simpleDateFormat.parse(endDateTime);
            eventJSONModel.setEnd_time(endDate);

            String geofence = String.valueOf(spinner.getSelectedItem());
            String delimiter = "[,]";
            String[] tokens = geofence.split(delimiter);
            eventJSONModel.setGeofence_id(Integer.parseInt(tokens[0]));

            if (checkEventAlreadyExists(eventJSONModel)) return true;

            final RestService restService = new RestService();
            RestService.CommunityAppWebService appWebService = restService.getService2();
            appWebService.createEvent(eventJSONModel,
                    new Callback<EventJSONModel>() {
                        /**
                         * {@inheritDoc}
                         * @param model
                         * @param response
                         */
                        @Override
                        public void success(EventJSONModel model, Response response) {
                            if (response.getStatus() == 201) {
                                try {
                                    Event event = Event.findOrCreateFromModel(model);
                                    event.save();
                                    EVENT_CREATION_STATUS = true;
                                    EVENT_CREATION_TITLE = "Success";
                                    EVENT_CREATION_MESSAGE = "Event "
                                            + eventJSONModel.getEvent_name() +
                                            " has been successfully created.";
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        /**
                         * {@inheritDoc}
                         * @param error
                         */
                        @Override
                        public void failure(RetrofitError error) {
                            EVENT_CREATION_STATUS = false;
                            EVENT_CREATION_MESSAGE = "Error";
                            EVENT_CREATION_MESSAGE = "Something went wrong";
                        }
                    });
            return true;
        }

        /**
         * Checks if the event is already created by the same user before
         * @param eventJSONModel
         * @return
         */
        private boolean checkEventAlreadyExists(EventJSONModel eventJSONModel) {
            List<Event> events = new Select()
                    .from(Event.class)
                    .where("event_name = ?", eventJSONModel.getEvent_name())
                    .execute();

            if (events != null) {
                for (Event event: events) {
                    if (event.getEvent_name().equals(eventJSONModel.getEvent_name()) &&
                            event.getPersonal_feeling()
                                    .equals(eventJSONModel.getPersonal_feeling()) &&
                            event.getEvent_description()
                                    .equals(eventJSONModel.getEvent_description()) &&
                            event.getStart_time().equals(eventJSONModel.getStart_time()) &&
                            event.getEnd_time().equals(eventJSONModel.getEnd_time())) {
                        EVENT_CREATION_MESSAGE = "Event already exists!";
                        EVENT_CREATION_STATUS = false;
                        EVENT_CREATION_TITLE = "Failed";
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_creation, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
