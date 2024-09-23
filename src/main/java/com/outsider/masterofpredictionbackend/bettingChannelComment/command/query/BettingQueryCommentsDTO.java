package com.outsider.masterofpredictionbackend.bettingChannelComment.command.query;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingQueryCommentsDTO {

  private BettingChannelComment comment;

  private BettingCommentsUserDTO user;
}
