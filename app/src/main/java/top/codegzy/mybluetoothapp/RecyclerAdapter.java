package top.codegzy.mybluetoothapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BlueToothViewHolder> {

    private static final Random random;

    static {
        random = new Random(System.currentTimeMillis());
    }

    private List<BluetoothDevice> bluetoothDevices;
    private Context context;

    public RecyclerAdapter(List<BluetoothDevice> bluetoothDevices, Context context) {
        this.bluetoothDevices = bluetoothDevices;
        this.context = context;
    }

    @NonNull
    @Override
    public BlueToothViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_card, viewGroup, false);
        return new BlueToothViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull BlueToothViewHolder blueToothViewHolder, int i) {
        Typeface iconFont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        boolean flag = Objects.isNull(bluetoothDevices.get(i).getName());
        String deviceName = flag ? Constants.NO_NAME_DEVICE : bluetoothDevices.get(i).getName();
        String deviceAddress = bluetoothDevices.get(i).getAddress();
//        String[] array = context.getResources().getStringArray(R.array.icon_device);
//        blueToothViewHolder.blueToothName.setText(deviceName);
//        blueToothViewHolder.blueToothAddress.setText(bluetoothDevices.get(i).getAddress());
//        blueToothViewHolder.blueToothImage.setTypeface(iconFont);
//        blueToothViewHolder.blueToothImage.setText(array[random.nextInt(array.length)]);
//        blueToothViewHolder.blueToothDetail.setTypeface(iconFont);
//        blueToothViewHolder.blueToothDetail.setText(R.string.icon_right_arrow);
//        blueToothViewHolder.blueToothCard.setOnClickListener(v -> {
//            Constants.toastShort(v.getContext(), "我的序号是 " + i);
//        });
        blueToothViewHolder.folder_icon.setTypeface(iconFont);
        blueToothViewHolder.folder_icon.setText(R.string.icon_connect_device);
        blueToothViewHolder.folder_icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        blueToothViewHolder.folder_name_icon.setTypeface(iconFont);
        blueToothViewHolder.folder_name_icon.setText(flag ? R.string.icon_computer : R.string.icon_phone);
        blueToothViewHolder.folder_name_icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        blueToothViewHolder.folder_address_icon.setTypeface(iconFont);
        blueToothViewHolder.folder_address_icon.setText(R.string.icon_mac);
        blueToothViewHolder.folder_address_icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        blueToothViewHolder.folder_name.setText(deviceName);
        blueToothViewHolder.folder_address.setText(deviceAddress);
        blueToothViewHolder.folder_arrow_icon.setTypeface(iconFont);
        blueToothViewHolder.folder_arrow_icon.setText(R.string.icon_right_arrow);
        blueToothViewHolder.folder_arrow_icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);

        blueToothViewHolder.folderCard.setOnClickListener(v -> {
            Constants.toastShort(context, "当前序号是 " + i);
            boolean bond = bluetoothDevices.get(i).createBond();
            Log.i("perpare for communicate", "onBindViewHolder: " + bond);
        });
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    class BlueToothViewHolder extends RecyclerView.ViewHolder {
//        private TextView blueToothName;
//        private TextView blueToothAddress;
//        private TextView blueToothImage;
//        private TextView blueToothDetail;
//        private CardView blueToothCard;

        private TextView folder_icon;
        private TextView folder_name_icon;
        private TextView folder_name;
        private TextView folder_address_icon;
        private TextView folder_address;
        private TextView folder_arrow_icon;
        private CardView folderCard;

        public BlueToothViewHolder(@NonNull View itemView) {
            super(itemView);
//            blueToothName = itemView.findViewById(R.id.blueToothName);
//            blueToothAddress = itemView.findViewById(R.id.blueToothAddress);
//            blueToothImage = itemView.findViewById(R.id.bluetoothImage);
//            blueToothDetail = itemView.findViewById(R.id.blueToothDetail);
//            blueToothCard = itemView.findViewById(R.id.blueToothCard);

            folder_icon = itemView.findViewById(R.id.folder_icon);
            folder_name_icon = itemView.findViewById(R.id.folder_name_icon);
            folder_name = itemView.findViewById(R.id.folder_name);
            folder_address_icon = itemView.findViewById(R.id.folder_address_icon);
            folder_address = itemView.findViewById(R.id.folder_address);
            folder_arrow_icon = itemView.findViewById(R.id.folder_arrow_icon);
            folderCard = itemView.findViewById(R.id.folderCard);
        }
    }
}
