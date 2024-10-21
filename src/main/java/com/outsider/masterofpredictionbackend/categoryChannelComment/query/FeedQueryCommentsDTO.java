package com.outsider.masterofpredictionbackend.categoryChannelComment.query;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedQueryCommentsDTO {
  private CategoryChannelComment comment;
  private FeedCommentsUserDTO user;
}
