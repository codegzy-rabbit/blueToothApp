package top.codegzy.mybluetoothapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new BlueToothViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull BlueToothViewHolder blueToothViewHolder, int i) {
        Typeface iconFont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        String deviceName = Objects.isNull(bluetoothDevices.get(i).getName()) ? Constants.NO_NAME_DEVICE : bluetoothDevices.get(i).getName();
        blueToothViewHolder.blueToothName.setText(deviceName);
        blueToothViewHolder.blueToothAddress.setText(bluetoothDevices.get(i).getAddress());
        blueToothViewHolder.blueToothImage.setTypeface(iconFont);
        String[] array = context.getResources().getStringArray(R.array.icon_device);
        blueToothViewHolder.blueToothImage.setText(array[random.nextInt(array.length)]);
        blueToothViewHolder.blueToothDetail.setTypeface(iconFont);
        blueToothViewHolder.blueToothDetail.setText(R.string.icon_right_arrow);
        blueToothViewHolder.blueToothCard.setOnClickListener(v -> {
            Constants.toastShort(v.getContext(), "我的序号是 " + i);
        });
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    class BlueToothViewHolder extends RecyclerView.ViewHolder {
        private TextView blueToothName;
        private TextView blueToothAddress;
        private TextView blueToothImage;
        private TextView blueToothDetail;
        private CardView blueToothCard;

        public BlueToothViewHolder(@NonNull View itemView) {
            super(itemView);
            blueToothName = itemView.findViewById(R.id.blueToothName);
            blueToothAddress = itemView.findViewById(R.id.blueToothAddress);
            blueToothImage = itemView.findViewById(R.id.bluetoothImage);
            blueToothDetail = itemView.findViewById(R.id.blueToothDetail);
            blueToothCard = itemView.findViewById(R.id.blueToothCard);
        }
    }
}
