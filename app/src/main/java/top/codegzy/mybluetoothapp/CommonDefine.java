package top.codegzy.mybluetoothapp;

import java.util.Arrays;

public class CommonDefine {
    public static final String MESSAGE_SEND_FLAG = "0";
    public static final String PARAM_READ_SEND_FLAG = "1";
    public static final String PARAM_WRITE_SEND_FLAG = "2";
    public static final String RECORD_SEND_FLAG = "3";

    public static final int RCV_MESSAGE_EXIST = 1;

    public static String splitDataPart(String msg){
        msg = msg.trim();
        String[] value = msg.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i=0;i<value.length;i++){

            if (i == 1){
                result.append("/ ");
            }

            if (i == 3){
                result.append("/ ");
            }

            if (i == 5){
                result.append("/ ");
            }

            if (i == 7){
                result.append("/ ");
            }

            if (i == 11){
                result.append("/ ");
            }

            if (i == 13){
                result.append("\" ");
            }

            if (i == value.length-2){
                result.append("\" ");
            }

            if (i == value.length-1){
                result.append("/ ");
            }

            result.append(value[i]).append(" ");
        }

        return result.toString().trim();
    }

    public static String bytesToHex(byte[] bytes, int length) {

        StringBuilder stringBuilder = new StringBuilder();

        if(bytes == null ){
            return null;
        }
        if (length <= 0){
            return null;
        }

        /*
        2. 計算
         */

        try {
            for (int i = 0; i < length; i++) {

                String hex = Integer.toHexString(bytes[i] & 0xff);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                if (hex.length() > 2) {
                    hex = hex.substring(hex.length() - 2, hex.length());
                }
                stringBuilder.append(" ").append(hex);
            }
        } catch (Exception e) {
            return null;
        }

        /*
        99. 復帰値
         */
        return stringBuilder.toString();
    }

    public static int memCpy(byte[] dataBuff, int index, long data, int dataSize) {

        int ret             = 0;
        byte[] dataTmp      = new byte[4];


        /*
        2. 初期化 0x00
         */
        Arrays.fill(dataTmp, (byte) 0x00);

        /*
        3. 計算
         */
        switch (dataSize){
            case 1:
                dataTmp[0] = (byte) (data & 0xFF);
                break;
            case 2:
                dataTmp[0] = (byte) ((data & 0xFF00) >> 8);
                dataTmp[1] = (byte) (data & 0xFF);
                break;
            case 4:
                dataTmp[0] = (byte) ((data & 0xFF000000) >> 24);
                dataTmp[1] = (byte) ((data & 0xFF0000) >> 16);
                dataTmp[2] = (byte) ((data & 0xFF00) >> 8);
                dataTmp[3] = (byte) (data & 0xFF);
                break;
            default:
                break;
        }

        /*
        4. Get dataBuff Data
         */
        try {
            System.arraycopy(dataTmp, 0, dataBuff, index, dataSize);
        }catch (Exception e){
            ret = 2;
        }

        /*
        99. 復帰
         */
        return ret;
    }

    public static int sysArryCopy(byte[] srcDat, int srcPos, byte[] dstDat, int destPos, int length) {

        int ret = 0;

        /*
        2. Data Copy
         */
        try {
            System.arraycopy(srcDat, srcPos, dstDat, destPos, length);
        }catch (Exception e){
            ret = 2;
        }

        /*
        99. 復帰値
         */
        return ret;
    }


}

