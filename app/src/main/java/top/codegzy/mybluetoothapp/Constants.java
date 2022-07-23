package top.codegzy.mybluetoothapp;

import android.content.Context;
import android.widget.Toast;

public interface Constants {
    /**
     * sdk version
     */
    double SDK_VERSION = 6.0;

    /**
     * request code
     */
    int REQUESTCODE = 1;

    /**
     * Prompt searching
     */
    String IS_SEARCHING = "正在搜索...";

    /**
     * Prompt bluetooth message
     */
    String SET_BLUETOOTHADDRESS = "请设置蓝牙address";

    /**
     * destory tag
     */
    String DESTROY_TAG = "destory";

    /**
     * unregister
     */
    String UNREGISTER = "解除注册";

    /**
     * device without name
     */
    String NO_NAME_DEVICE = "未知设备";

    /**
     * prompt message short
     *
     * @param context context param
     * @param message display the message on the screen
     */
    static void toastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * prompt message long
     *
     * @param context context param
     * @param message display the message on the screen
     */
    static void toastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
