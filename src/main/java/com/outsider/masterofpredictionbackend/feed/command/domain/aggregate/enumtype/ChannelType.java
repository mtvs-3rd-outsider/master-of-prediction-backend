package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype;

import com.outsider.masterofpredictionbackend.feed.command.domain.vo.CategoryChannelParams;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.HotTopicParams;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.MyChannelParams;
import lombok.Getter;

@Getter
public enum ChannelType {
    MYCHANNEL(MyChannelParams.class),
    CATEGORYCHANNEL(CategoryChannelParams.class);
    private final Class<?> paramsClass;

    ChannelType(Class<?> paramsClass) {
        this.paramsClass = paramsClass;
    }

}
