package cs446.budgetme.Utils;

import java.util.Calendar;

public class DateUtils {

    public static void setCalendarToBeginningOfDay(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
