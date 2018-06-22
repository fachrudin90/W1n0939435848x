package com.tamboraagungmakmur.winwin.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Tambora on 18/08/2016.
 */
public class FormatDate {

    public static String format(String waktu, String formatAwal, String formatAkhir){

        SimpleDateFormat fromdate = new SimpleDateFormat(formatAwal);
        SimpleDateFormat todate = new SimpleDateFormat(formatAkhir);

        String reformatdate = "";

        try {
            reformatdate = todate.format(fromdate.parse(waktu));
        } catch (ParseException e) {
            e.printStackTrace();
        }

      return reformatdate;

    }
}
