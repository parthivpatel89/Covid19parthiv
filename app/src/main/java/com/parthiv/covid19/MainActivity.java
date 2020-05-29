package com.parthiv.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


///
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Looper;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    private static String url = "https://api.covid19api.com/summary";

    ArrayList<HashMap<String, Integer>> countryList;

    ArrayList<HashMap<HashMap<String, String>, HashMap<String, Integer>>> countryLists;


    ArrayList<CovidCases> cclist = new ArrayList<>();
    TextView txtRecovered, txtDeath, txtTotalCases, txtCountry;

    String TOTALCASE = "";
    String DEATHS = "";
    String RECOVERED = "";

    Button btnRefresh, btnSort, btnFilter, btnDeath, btnRecover, btnCase;
    RecyclerView recyclerView;
    CaseAdapter cadapter;
    HashMap<String, Integer> countryy = new HashMap<>();

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    CovidCases cps = new CovidCases();
    CovidCases cps1 = new CovidCases();
    String sortCases = "asc";
    String sortDeaths = "asc";
    String sortRecovers = "asc";

    String countrycode = "";


    int fcno = 0;
    int fdno = 0;
    int frno = 0;

    Button btnApply, btnClose, btnReset;
    Spinner sp;
    RadioGroup radioGroup;
    RadioButton statradioButton;

    RadioGroup radioup;
    RadioButton thenradioButton;

    PopupWindow popupWindow;
    LinearLayout linearLayout1;
    EditText edtCase, edtDeath, edtRecover, edtFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
//        btnSort = (Button) findViewById(R.id.btnSort);
        btnFilter = (Button) findViewById(R.id.btnFilter);

        btnDeath = (Button) findViewById(R.id.btnDeath);
        btnRecover = (Button) findViewById(R.id.btnRecover);
        btnCase = (Button) findViewById(R.id.btnCases);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        btnCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCase();
            }
        });
        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortRecover();
            }
        });
        btnDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortDeath();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                filterCase(fcno);
//                filterDeath(fdno);
//                filterRecovered(frno);


                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customView = layoutInflater.inflate(R.layout.popup, null);

                btnClose = (Button) customView.findViewById(R.id.btnClose);
                btnReset = (Button) customView.findViewById(R.id.btnReset);
                btnApply = (Button) customView.findViewById(R.id.btnApply);

//                edtCase = (EditText) customView.findViewById(R.id.edtCase);
//                edtDeath = (EditText) customView.findViewById(R.id.edtDeath);
//                edtRecover = (EditText) customView.findViewById(R.id.edtRecover);
//                sp=(Spinner)customView.findViewById(R.id.spinner1);
                radioGroup = (RadioGroup) customView.findViewById(R.id.radiostat);
                radioup = (RadioGroup) customView.findViewById(R.id.radioup);

                edtFilter = (EditText) customView.findViewById(R.id.edtFilter);
//                edtFilter.setText(fcno);

                popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);


                popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);


                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        statradioButton = (RadioButton) customView.findViewById(selectedId);

                        int selectedIds = radioup.getCheckedRadioButtonId();
                        thenradioButton = (RadioButton) customView.findViewById(selectedIds);
                        if (selectedIds == -1) {
                            Toast.makeText(MainActivity.this, "Please select any option", Toast.LENGTH_SHORT).show();
                        } else if (selectedId == -1) {
                            Toast.makeText(MainActivity.this, "Please select any option", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(MainActivity.this,genderradioButton.getText(), Toast.LENGTH_SHORT).show();
                            fcno = Integer.parseInt(edtFilter.getText().toString());
                            if (Integer.parseInt(edtFilter.getText().toString()) != 0) {
                                if (statradioButton.getText().equals("Case")) {
                                    if (thenradioButton.getText().equals("Greater then")) {
                                        filterCase(Integer.parseInt(edtFilter.getText().toString()), ">");
                                    } else {
                                        filterCase(Integer.parseInt(edtFilter.getText().toString()), "<");
                                    }
                                }
                                if (statradioButton.getText().equals("Death")) {
                                    if (thenradioButton.getText().equals("Greater then")) {
                                        filterDeath(Integer.parseInt(edtFilter.getText().toString()), ">");
                                    } else {
                                        filterDeath(Integer.parseInt(edtFilter.getText().toString()), "<");
                                    }
                                }
                                if (statradioButton.getText().equals("Recover")) {
                                    if (thenradioButton.getText().equals("Greater then")) {
                                        filterRecovered(Integer.parseInt(edtFilter.getText().toString()), ">");
                                    } else {
                                        filterRecovered(Integer.parseInt(edtFilter.getText().toString()), "<");
                                    }
                                }
                                popupWindow.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Please Enter number", Toast.LENGTH_SHORT).show();
                            }
                        }

//                        popupWindow.dismiss();
                    }
                });
                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fcno = 0;
                        frno = 0;
                        fdno = 0;
                        // edtFilter.setText(0);
                        cadapter = new CaseAdapter(cclist);
                        recyclerView.setAdapter(cadapter);
                        cadapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

//                edtCase.setText(fcno);
//                edtDeath.setText(fdno);
//                edtRecover.setText(frno);
            }
        });



        countryList = new ArrayList<>();
        countryLists = new ArrayList<>();


        txtTotalCases = (TextView) findViewById(R.id.txtTotalCases);
        txtRecovered = (TextView) findViewById(R.id.txtRecovered);
        txtDeath = (TextView) findViewById(R.id.txtDeath);
        txtCountry = (TextView) findViewById(R.id.txtCountry);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetStats().execute();

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 2 * 60 * 1000);

                new GetStats().execute();
            }
        }, 0);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();


//        cps.setTotalRecovered(12);
//        cps.setTotalDeaths(14);
//        cps.setTotalConfirmed(54);
//        cps.setCountry("India");
//        cclist.add(cps);
    }


    private class GetStats extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);
            //Log.e(TAG, "Response : " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject global = jsonObj.getJSONObject("Global");

                    TOTALCASE = global.getString("TotalConfirmed");
                    DEATHS = global.getString("TotalDeaths");
                    RECOVERED = global.getString("TotalRecovered");


                    JSONArray countries = jsonObj.getJSONArray("Countries");


                    cclist = new ArrayList<>();

                    for (int i = 0; i < countries.length(); i++) {
                        JSONObject c = countries.getJSONObject(i);
                        String countrycodes = c.getString("CountryCode");
                        int totalConfirmed = c.getInt("TotalConfirmed");
                        if (countrycodes.equals(countrycode)) {
                            if (totalConfirmed != 0) {
                                cps1 = new CovidCases();
                                cps1.setCountry(c.getString("Country"));
                                cps1.setTotalConfirmed(c.getInt("TotalConfirmed"));
                                cps1.setTotalDeaths(c.getInt("TotalDeaths"));
                                cps1.setTotalRecovered(c.getInt("TotalRecovered"));
                                //countryList.add(countryy);
                                cclist.add(cps1);

                            }
                        }

                    }


                    for (int i = 0; i < countries.length(); i++) {
                        JSONObject c = countries.getJSONObject(i);
                        Log.d(TAG, "c object: " + c.toString());
                        String country = c.getString("Country");
                        int totalConfirmed = c.getInt("TotalConfirmed");
                        int totalDeaths = c.getInt("TotalDeaths");
                        int totalRecovered = c.getInt("TotalRecovered");

                        if (totalConfirmed != 0) {
                            cps = new CovidCases();
                            cps.setCountry(c.getString("Country"));
                            cps.setTotalConfirmed(c.getInt("TotalConfirmed"));
                            cps.setTotalDeaths(c.getInt("TotalDeaths"));
                            cps.setTotalRecovered(c.getInt("TotalRecovered"));
                            //countryList.add(countryy);
                            cclist.add(cps);

                        }

                        Log.d(TAG, "before countrycaseslist: " + cclist.toString());

//                        HashMap<String, Integer> countryy = new HashMap<>();
                        HashMap<String, Integer> countryCase = new HashMap<>();
                        HashMap<String, Integer> countryDeath = new HashMap<>();
                        HashMap<String, Integer> countryRecovered = new HashMap<>();

                        HashMap<String, String> countrys = new HashMap<>();


                        countrys.put("country", country);


                    }
                    Collections.sort(cclist, new Comparator<CovidCases>() {
                        @Override
                        public int compare(CovidCases o1, CovidCases o2) {
                            return o2.compareToTotalConfirmed(o1);
                        }
                    });
                    Log.d(TAG, "countrycaseslist: " + cclist.toString());

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Oops something went wrong, Please try after some time.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Oops something went wrong, Please try after some time !!!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            txtTotalCases.setText(TOTALCASE);
            txtDeath.setText(DEATHS);
            txtRecovered.setText(RECOVERED);


            cadapter = new CaseAdapter(cclist);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL));
            recyclerView.setAdapter(cadapter);
            cadapter.notifyDataSetChanged();

        }

    }

    private String getCountryFromLocation(Context context, Location location) {
        final Geocoder geocoder = new Geocoder(context);
        String country = null;
        try {
            final List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                country = addresses.get(0).getCountryCode();
            }
        } catch (IOException e) {
            Log.w(TAG, "Exception occurred when getting geocoded country from location");
        }
        return country;
    }

    private static HashMap ascsortByValues(HashMap map, final boolean order) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {

                if (order) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                } else {
                    return ((Comparable) ((Map.Entry) (o2)).getValue())
                            .compareTo(((Map.Entry) (o1)).getValue());

                }
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    private static HashMap ascsortByValue(HashMap map, final boolean order) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {

                if (order) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                } else {
                    return ((Comparable) ((Map.Entry) (o2)).getValue())
                            .compareTo(((Map.Entry) (o1)).getValue());

                }
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    Location location;

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
//                                    latTextView.setText(location.getLatitude()+"");
//                                    lonTextView.setText(location.getLongitude()+"");
//                                    getCountryFromLocation(getApplicationContext(),location);
                                    txtCountry.setText("Country :" + getCountryFromLocation(getApplicationContext(), location));
                                    countrycode = getCountryFromLocation(getApplicationContext(), location);
//                                    Toast.makeText(MainActivity.this, ""+ getCountryFromLocation(getApplicationContext(),location), Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(MainActivity.this, ""+location.getLatitude()+""+location.getLongitude()+"", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            txtCountry.setText("Country :" + getCountryFromLocation(getApplicationContext(), location));
            //Toast.makeText(MainActivity.this, ""+mLastLocation.getLatitude()+""+mLastLocation.getLongitude()+"", Toast.LENGTH_SHORT).show();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }


    public void ascSort() {
        Map<Integer, String> map = ascsortByValues(countryy, true);
        System.out.println("After Sorting: ascending");
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator2.next();
            System.out.print(me2.getKey() + ": ");
            System.out.println(me2.getValue());
        }
    }

    public void descSort() {
        Map<Integer, String> map = ascsortByValues(countryy, false);
        System.out.println("After Sorting: descending");
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();

        while (iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator2.next();
            System.out.print(me2.toString());
            System.out.print(me2.getKey() + ": " + me2.getValue());

        }
    }


    //////////final sort
    private void sortDeath() {
        if (sortDeaths.equals("asc")) {
            Collections.sort(cclist, new Comparator<CovidCases>() {
                @Override
                public int compare(CovidCases o1, CovidCases o2) {
                    return o1.compareToTotalDeaths(o2);
                }
            });
            Log.d(TAG, "sortDeath: " + cclist.toString());
            cadapter = new CaseAdapter(cclist);
            recyclerView.setAdapter(cadapter);
            cadapter.notifyDataSetChanged();
            sortDeaths = "desc";
        } else {
            Collections.sort(cclist, new Comparator<CovidCases>() {
                @Override
                public int compare(CovidCases o1, CovidCases o2) {
                    return o2.compareToTotalDeaths(o1);
                }
            });
            Log.d(TAG, "sortDeath: " + cclist.toString());
            cadapter = new CaseAdapter(cclist);
            recyclerView.setAdapter(cadapter);
            cadapter.notifyDataSetChanged();
            sortDeaths = "asc";
        }

    }

    private void sortRecover() {
        if (sortRecovers.equals("asc")) {
            Collections.sort(cclist, new Comparator<CovidCases>() {
                @Override
                public int compare(CovidCases o1, CovidCases o2) {
                    return o1.compareToTotalRecovered(o2);
                }
            });
            Log.d(TAG, "sortRecover: " + cclist.toString());
            cadapter = new CaseAdapter(cclist);
            recyclerView.setAdapter(cadapter);
            cadapter.notifyDataSetChanged();
            sortRecovers = "desc";
        } else {
            Collections.sort(cclist, new Comparator<CovidCases>() {
                @Override
                public int compare(CovidCases o1, CovidCases o2) {
                    return o2.compareToTotalRecovered(o1);
                }
            });
            Log.d(TAG, "sortRecover: " + cclist.toString());
            cadapter = new CaseAdapter(cclist);
            recyclerView.setAdapter(cadapter);
            cadapter.notifyDataSetChanged();
            sortRecovers = "asc";
        }

    }

    private void sortCase() {

        if (sortCases.equals("asc")) {
            Collections.sort(cclist, new Comparator<CovidCases>() {
                @Override
                public int compare(CovidCases o1, CovidCases o2) {
                    return o1.compareToTotalConfirmed(o2);
                }
            });
            Log.d(TAG, "sortCase: " + cclist.toString());

            cadapter = new CaseAdapter(cclist);
            recyclerView.setAdapter(cadapter);
            cadapter.notifyDataSetChanged();
            sortCases = "desc";
        } else {
            Collections.sort(cclist, new Comparator<CovidCases>() {
                @Override
                public int compare(CovidCases o1, CovidCases o2) {
                    return o2.compareToTotalConfirmed(o1);
                }
            });
            Log.d(TAG, "sortCase: " + cclist.toString());

            cadapter = new CaseAdapter(cclist);
            recyclerView.setAdapter(cadapter);
            cadapter.notifyDataSetChanged();
            sortCases = "asc";
        }

    }
///filter method
    private void filterDeath(int number, String syb) {

        ArrayList<CovidCases> result = new ArrayList<CovidCases>();
        if (syb.equals("<")) {
            for (CovidCases person : cclist) {
                if (person.getTotalDeaths() < number) {
                    result.add(person);
                }
            }
        } else {
            for (CovidCases person : cclist) {
                if (person.getTotalDeaths() > number) {
                    result.add(person);
                }
            }
        }

        System.out.println("filter cases");
        System.out.println(result);
        cadapter = new CaseAdapter(result);
        recyclerView.setAdapter(cadapter);
        cadapter.notifyDataSetChanged();
    }

    private void filterCase(int number, String symbol) {

        ArrayList<CovidCases> result = new ArrayList<CovidCases>();
        if (symbol.equals("<"))
            for (CovidCases person : cclist) {
                if (person.getTotalConfirmed() < number) {
                    result.add(person);
                }
            }
        if (symbol.equals(">")) {
            for (CovidCases person : cclist) {
                if (person.getTotalConfirmed() > number) {
                    result.add(person);
                }
            }
        }

        System.out.println("filter cases");
        System.out.println(result);
        cadapter = new CaseAdapter(result);
        recyclerView.setAdapter(cadapter);
        cadapter.notifyDataSetChanged();
    }

    private void filterRecovered(int number, String syb) {

        ArrayList<CovidCases> result = new ArrayList<CovidCases>();
        if (syb.equals("<")) {
            for (CovidCases person : cclist) {
                if (person.getTotalRecovered() < number) {
                    result.add(person);
                }
            }
        } else {
            for (CovidCases person : cclist) {
                if (person.getTotalRecovered() > number) {
                    result.add(person);
                }
            }
        }

        System.out.println("filter cases");
        System.out.println(result);
        cadapter = new CaseAdapter(result);
        recyclerView.setAdapter(cadapter);
        cadapter.notifyDataSetChanged();
    }


}


