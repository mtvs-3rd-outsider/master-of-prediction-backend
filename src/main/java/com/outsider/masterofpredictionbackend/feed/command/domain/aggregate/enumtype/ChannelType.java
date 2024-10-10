package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype;

import com.outsider.masterofpredictionbackend.feed.command.domain.vo.CategoryChannelParams;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.HotTopicParams;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.MyChannelParams;

public enum ChannelType {
    MY_CHANNEL(MyChannelParams.class),
    CATEGORY(CategoryChannelParams.class),
    HOT_TOPIC(HotTopicParams.class);

    private final Class<?> paramsClass;

    ChannelType(Class<?> paramsClass) {
        this.paramsClass = paramsClass;
    }

    public Class<?> getParamsClass() {
        return paramsClass;
    }
}
