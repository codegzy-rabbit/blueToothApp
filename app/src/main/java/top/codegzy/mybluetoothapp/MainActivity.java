package top.codegzy.mybluetoothapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    /**
     * define components
     */
    private Button sendMessage;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceName;
    private String mBluetoothAddress;
    private List<BluetoothDevice> mBlueToothDevices;
    private List<BluetoothDevice> mPairedBlueToothDevices;
    private List<BluetoothDevice> mUnPairedBlueToothDevices;
    private RecyclerView recyclerView;
    private SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10);
    private String tmpBluetoothDeviceName = "74995588";


    //TODO 下拉刷新

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {

            //get action type
            String action = intent.getAction();

            // get devices that have been searched
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // fill the mBluetoothDevices array
                mBlueToothDevices.add(device);
                // refresh the mBluetoothDevices array to display
                // if device is bonded
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mUnPairedBlueToothDevices.add(device);
                }

                // if bluetooth adapter the discover action is finished
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // the mBluetoothAddress and mBluetoothDeviceName is not null
                Log.i("Size", "Search for the number of devices: " + mBlueToothDevices.size());
                Log.i("Devices", "the name of devices: " + mBlueToothDevices);

                //display a message
                Constants.toastShort(getApplicationContext(), "The search is complete");

            }

        }
    };

    /**
     * define intentFilter
     *
     * @return a new Filter
     */
    private IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        //discover is finished
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // found devices
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        // bond state change
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // state change
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // discover start
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        return filter;
    }

    /**
     * app init method
     */
    @SuppressLint("MissingPermission")
    private void appInit() {
        //init array list
        mBlueToothDevices = new ArrayList<>();
        mUnPairedBlueToothDevices = new ArrayList<>();

        //init bluetooth adapter
        mBluetoothAdapter = BluetoothMain.getInstance().getmBluetoothAdapter();

        mPairedBlueToothDevices = new ArrayList<>(mBluetoothAdapter.getBondedDevices());

        // send message button
        sendMessage = findViewById(R.id.sendMessage);


        //register receiver by intent filter
        registerReceiver(mReceiver, makeFilter());


        //if sdk version higher 6 then request permission
        if (Build.VERSION.SDK_INT >= Constants.SDK_VERSION) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUESTCODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialization
        appInit();

        sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // if mBluetoothAddress is null, prompt set bluetooth message
                if (mBluetoothAddress == null) {
                    Constants.toastShort(getApplicationContext(), Constants.SET_BLUETOOTHADDRESS);
                    return;
                }

                Intent i = new Intent(MainActivity.this, MainActivity2.class);

                // pass mBluetoothAddress
                i.putExtra("BluetoothAddress", mBluetoothAddress);

                // start up activity
                startActivity(i);
            }
        });

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        //unregister
        unregisterReceiver(mReceiver);
        Log.e(Constants.DESTROY_TAG, Constants.UNREGISTER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.searchButton);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @SuppressLint("MissingPermission")
            @Override
            public boolean onQueryTextChange(String s) {
                Log.i("change", "onQueryTextChange: " + s);
                if (Objects.isNull(s) || "".equals(s)) {
                    discoverBlueTooth(mBlueToothDevices);
                    Log.i("change", "mBlueToothDevices: " + mBlueToothDevices);
                    return false;
                }
                List<BluetoothDevice> searchBlueToothDevices = mBlueToothDevices.stream()
                        .filter(bluetoothDevice -> (Objects.isNull(bluetoothDevice.getName()) ?
                                Constants.NO_NAME_DEVICE :
                                bluetoothDevice.getName()).contains(s) |
                                bluetoothDevice.getAddress().contains(s))
                        .distinct()
                        .collect(Collectors.toList());
                discoverBlueTooth(searchBlueToothDevices);
                Log.i("change", "searchBlueToothDevices: " + searchBlueToothDevices);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        // if the bluetooth function is not open, open it
        if (!mBluetoothAdapter.isEnabled() && !mBluetoothAdapter.enable()) {
            finish();
            return;
        }
        // start searching for bluetooth devices around you
        mBluetoothAdapter.startDiscovery();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        Log.i("Onstart", "onStart: " + type);
        if (Constants.ALL.equals(type)) {
            search(mBlueToothDevices);
            Constants.toastShort(this, Constants.IS_SEARCHING);
        } else if (Constants.PAIRED.equals(type)) {
            search(mPairedBlueToothDevices);
            Constants.toastShort(this, "正在搜索已配对的蓝牙...");
        } else {
            search(mUnPairedBlueToothDevices);
            Constants.toastShort(this, "正在搜索可配对的蓝牙...");
        }
    }


    private void discoverBlueTooth(List<BluetoothDevice> bluetoothDevicesArg) {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(bluetoothDevicesArg, MainActivity.this);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(spacesItemDecoration);
        }
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void search(List<BluetoothDevice> bluetoothDevicesArg) {
        new Thread(() -> {
            try {
                for (int i = 0; i < 4; i++) {
                    TimeUnit.SECONDS.sleep(3);
                    runOnUiThread(() -> {
                        discoverBlueTooth(bluetoothDevicesArg);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }

}