package com.tamboraagungmakmur.winwin.Utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by innan on 10/8/2017.
 */

public class FormatPrice {

    public FormatPrice() {
    }

    public String format(String amount) {
        if (amount != null) {
            return ("Rp " + NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(amount)));
        } else {
            return ("Rp 0");
        }
    }

}
