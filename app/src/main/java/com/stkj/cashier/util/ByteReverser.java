package com.stkj.cashier.util;

public class ByteReverser {

    /**
     * 将给定的十六进制字符串按字节翻转。
     *
     * @param hexString 输入的十六进制字符串，长度必须是偶数。
     * @return 翻转后的十六进制字符串。
     */
    public static String reverseHexBytes(String hexString) {
        // 检查输入是否为 null 或空
        if (hexString == null || hexString.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty.");
        }

        // 检查输入长度是否为偶数
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Input string length must be even.");
        }

        // 将十六进制字符串转换为字节数组
        byte[] bytes = hexStringToByteArray(hexString);

        // 翻转字节数组
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - 1 - i];
            bytes[bytes.length - 1 - i] = temp;
        }

        // 将字节数组转换回十六进制字符串并返回
        return byteArrayToHexString(bytes);
    }

    /**
     * 将十六进制字符串转换为字节数组。
     *
     * @param hexString 十六进制字符串。
     * @return 对应的字节数组。
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param bytes 字节数组。
     * @return 对应的十六进制字符串。
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 测试方法
    public static void main(String[] args) {
        String input = "54677C76";
        String reversed = reverseHexBytes(input);
        System.out.println("Original: " + input);
        System.out.println("Reversed: " + reversed);
    }
}
