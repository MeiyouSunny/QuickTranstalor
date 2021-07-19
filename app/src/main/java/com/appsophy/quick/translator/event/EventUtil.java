package com.appsophy.quick.translator.event;

import de.greenrobot.event.EventBus;

public class EventUtil {

    private static final int EVENT_REFRESH_RECORDS = 1;
    /////////////////////////////////////////////////////////////////////////

    public static void sendRefreshRecordsEvent() {
        Event event = Event.create(EVENT_REFRESH_RECORDS);
        EventBus.getDefault().post(event);
    }

    public static boolean isRefreshRecordsEvent(Event event) {
        return event != null && event.type == EVENT_REFRESH_RECORDS;
    }


}
