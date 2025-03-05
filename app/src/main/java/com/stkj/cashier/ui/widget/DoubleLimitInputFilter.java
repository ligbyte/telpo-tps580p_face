package com.stkj.cashier.ui.widget;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 数字大小限制
 */
public class DoubleLimitInputFilter implements InputFilter {

    private BigDecimal maxLimit;
    private BigDecimal minLimit;

    public DoubleLimitInputFilter(double minLimit, double maxLimit) {
        this.maxLimit = new BigDecimal(maxLimit);
        this.minLimit = new BigDecimal(minLimit);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            if (TextUtils.isEmpty(source)) {
                return null;
            }
            if (minLimit.compareTo(maxLimit) == 0) {
                return null;
            }
            String destString = dest.toString();
            //限制小数点后个数
            if (destString.contains(".")) {
                int index = destString.indexOf(".");
                int mPointLength = destString.substring(index).length();
                if (mPointLength == 3) {
                    return "";
                }
            }
            //可以输入金额 0.0x
            String inputText = destString + source;
            if (TextUtils.equals("0.0", inputText)) {
                return null;
            }
            BigDecimal input = new BigDecimal(inputText);
            //限制大小
            if (Objects.equals(input.min(minLimit), minLimit) && Objects.equals(input.max(maxLimit), maxLimit)) {
                return null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

}
