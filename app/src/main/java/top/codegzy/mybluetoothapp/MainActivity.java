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
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    /**
     * define components
     */
    private Button sendMessage;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceName;
    private String mBluetoothAddress;
    private ArrayList<BluetoothDevice> mBlueToothDevices;
    private ArrayList<BluetoothDevice> mPairedBlueToothDevices;
    private ArrayList<BluetoothDevice> mUnPairedBlueToothDevices;
    private RecyclerView recyclerView;
    private SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(8);
    private String tmpBluetoothDeviceName = "74995588";


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
                //TODO 需要优化
//                discoverBlueTooth(mBlueToothDevices);
                // if device is bonded
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    mPairedBlueToothDevices.add(device);

                    Log.i("broadcast", "sch onReceive:BOND_BONDED= " + device.getName());

                    //  verify the name of device is not null and contains the tmpBluetoothDeviceName
                    if (Objects.nonNull(device.getName()) && device.getName().contains(tmpBluetoothDeviceName)) {
                        // get device address
                        mBluetoothAddress = device.getAddress();
                        //get device name
                        mBluetoothDeviceName = device.getName();

                        // define intent
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        // pass bluetooth address information
                        i.putExtra("BluetoothAddress", mBluetoothAddress);
                        // start up
                        startActivity(i);

                        Log.i("broadcast", "sch target device:BOND_BONDED= " + device.getName());
                        return;
                    }


                    // if device is not bond
                } else if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mUnPairedBlueToothDevices.add(device);
                    Log.i("broadcast", "sch onReceive:not BOND_BONDED= " + device.getName());

                    if (Objects.nonNull(device.getName()) && device.getName().contains(tmpBluetoothDeviceName)) {
                        // get device address
                        mBluetoothAddress = device.getAddress();
                        //get device name
                        mBluetoothDeviceName = device.getName();

                        // define intent
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        // pass bluetooth address information
                        i.putExtra("BluetoothAddress", mBluetoothAddress);
                        // start up
                        startActivity(i);


                        Log.i("broadcast", "sch target device:not BOND_BONDED= " + device.getName());
                        return;
                    }

                }

                // if bluetooth adapter the discover action is finished
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // the mBluetoothAddress and mBluetoothDeviceName is not null
                Log.i("SIZE", "onReceive: " + mBlueToothDevices.size());
                Log.i("Devices", "onReceive: " + mBlueToothDevices);

                if (Objects.nonNull(mBluetoothAddress) && Objects.nonNull(mBluetoothDeviceName)) {
                    //display a message
                    Constants.toastShort(getApplicationContext(), String.format("%s = %s", mBluetoothDeviceName, mBluetoothAddress));
                } else {
                    //display a message
                    Constants.toastShort(getApplicationContext(), "搜索完成，没有找到需要的蓝牙设备");
                }

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
    private void appInit() {
        //init array list
        mBlueToothDevices = new ArrayList<>();
        mPairedBlueToothDevices = new ArrayList<>();
        mUnPairedBlueToothDevices = new ArrayList<>();
        //init bluetooth adapter
        mBluetoothAdapter = BluetoothMain.getInstance().getmBluetoothAdapter();
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

    @SuppressLint({"MissingPermission", "NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if the bluetooth function is not open, open it
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        // start searching for bluetooth devices around you
        mBluetoothAdapter.startDiscovery();
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.bluetoothSearch:
                discoverBlueTooth(mBlueToothDevices);
                Constants.toastShort(this, Constants.IS_SEARCHING);
                break;
            case R.id.displayPairedBlueTooth:
                discoverBlueTooth(mPairedBlueToothDevices);
                Constants.toastShort(this, "正在搜索已匹配的蓝牙...");
                break;
            case R.id.displayUnPairedBlueTooth:
                discoverBlueTooth(mUnPairedBlueToothDevices);
                Constants.toastShort(this, "正在搜索可配对的蓝牙...");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

}