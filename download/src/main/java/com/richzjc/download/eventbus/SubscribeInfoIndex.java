package com.richzjc.download.eventbus;

import java.util.Map;

public interface SubscribeInfoIndex {
    Map<Class, SimpleSubscribeInfo> getSubscriberInfo();
}
