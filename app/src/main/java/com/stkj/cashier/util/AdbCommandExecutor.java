package com.stkj.cashier.util;

import android.util.Log;

public class AdbCommandExecutor {

    private static final String TAG = "AdbCommandExecutor";

    /**
     * 执行 ADB 命令。
     *
     * @param command 要执行的 ADB 命令字符串，例如 "input keyevent 26"
     * @return 返回命令执行的结果输出。
     */
    public static String executeAdbCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process process;
        try {
            // 构建完整的 ADB shell 命令
            String fullCommand = "adb shell " + command;
            Log.d(TAG, "Executing command: " + fullCommand);

            // 启动进程并执行命令
            process = Runtime.getRuntime().exec(fullCommand);

            // 获取命令输出流
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            Log.d(TAG, "Command executed with exit code: " + exitCode);

            reader.close();
        } catch (Exception e) {
            Log.e(TAG, "Error executing ADB command", e);
            return "Error: " + e.getMessage();
        }

        return output.toString().trim();
    }
}
