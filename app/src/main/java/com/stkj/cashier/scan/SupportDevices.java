package com.stkj.cashier.scan;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class SupportDevices {

    public static final Map<String, Pair<String, Integer>> sDevices;
    private int scanCount = 0;


    static {
        /*
         * 支持设备列表
         */
        sDevices = new HashMap<>();
        sDevices.put("TPS508", new Pair<>("/dev/ttyACM0", 115200));
        sDevices.put("TPS360", new Pair<>("/dev/ttyACM0", 115200));
        sDevices.put("P8", new Pair<>("/dev/ttyACM0", 115200));
        sDevices.put("TPS537", new Pair<>("/dev/ttyACM0", 115200));

//        sDevices.put("D2", new Pair<>("/dev/ttyHSL0", 115200)); // D2串口模式
//        sDevices.put("D2M", new Pair<>("/dev/ttyHSL0", 115200)); // D2M串口模式
        sDevices.put("D2M", new Pair<>("/dev/ttyS7", 115200)); // D2M串口模式
        //sDevices.put("D2", new Pair<>("/dev/ttyHSL0", 9600)); // D2串口模式
//        sDevices.put("D2", new Pair<>("/dev/ttyACM0", 115200)); // D2U转串模式

        //sDevices.put("TPS980", new Pair<>("/dev/ttyS0", 115200));
        //sDevices.put("TPS980P", new Pair<>("/dev/ttyS0", 115200));
        //sDevices.put("TPS980P", new Pair<>("/dev/ttyACM0", 115200));
        sDevices.put("TPS980P", new Pair<>("/dev/ttyS0", 115200));

//        sDevices.put("T20", new Pair<>("/dev/ttyS7", 115200));
        sDevices.put("T20", new Pair<>("/dev/ttyHS2", 115200));
        sDevices.put("T20p", new Pair<>("/dev/ttyWK0", 115200));
        sDevices.put("TPS530", new Pair<>("/dev/ttyUSB0", 115200));
        sDevices.put("CW-TB2CA-9230", new Pair<>("/dev/ttyS4", 115200));

        sDevices.put("C31", new Pair<>("/dev/ttyACM0", 115200));
        sDevices.put("TPS732", new Pair<>("/dev/ttyACM0", 115200));

        sDevices.put("C50A", new Pair<>("/dev/ttyACM0", 115200));
        sDevices.put("C50", new Pair<>("/dev/ttyACM0", 115200));

//        sDevices.put("K8",new Pair<>("/dev/ttyACM0",115200));
        sDevices.put("K8",new Pair<>("/dev/ttyS4",115200));

        sDevices.put("TPS580P", new Pair<>("/dev/ttyHSL0", 115200));

        sDevices.put("C1P", new Pair<>("/dev/ttyACM0", 115200));
    }


}
