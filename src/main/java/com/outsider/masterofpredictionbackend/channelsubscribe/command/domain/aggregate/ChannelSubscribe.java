package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate;


import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChannelSubscribe")
public class ChannelSubscribe extends BaseEntity {

        @EmbeddedId
        private MyChannelSubscribeId id;

        @Column(name = "subscription_date", nullable = false)
        private LocalDateTime subscriptionDate;

        @Column(name = "expiration_date")
        private LocalDateTime expirationDate;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive;

        public void setSubscriptionDate(LocalDateTime subscriptionDate) {
                this.subscriptionDate = subscriptionDate;
        }

        public ChannelSubscribe() {
        }
        public ChannelSubscribe(MyChannelSubscribeId id, LocalDateTime subscriptionDate, LocalDateTime expirationDate, Boolean isActive) {
                this.id = id;
                this.subscriptionDate = subscriptionDate;
                this.expirationDate = expirationDate;
                this.isActive = isActive;
        }
        // getters and setters
        public MyChannelSubscribeId getId() {
                return id;
        }
        public LocalDateTime getSubscriptionDate() {
                return subscriptionDate;
        }
        public LocalDateTime getExpirationDate() {
                return expirationDate;
        }
        public Boolean getIsActive() {
                return isActive;
        }

        public void setActive(Boolean active) {
                isActive = active;
        }

        public void setExpirationDate(LocalDateTime expirationDate) {
                this.expirationDate = expirationDate;
        }
}
