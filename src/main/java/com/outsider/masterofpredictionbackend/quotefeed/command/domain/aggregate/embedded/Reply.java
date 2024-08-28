package com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reply {

    private Long replyId;
}
