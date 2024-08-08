package com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.aggregate;


import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "MyChannelSubscribe")
public class MyChannelSubscribe {

        @EmbeddedId
        private MyChannelSubscribeId id;

        @Column(name = "subscription_date", nullable = false)
        private LocalDateTime subscriptionDate;

        @Column(name = "expiration_date")
        private LocalDateTime expirationDate;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive;
        public MyChannelSubscribe() {
        }
        public MyChannelSubscribe(MyChannelSubscribeId id, LocalDateTime subscriptionDate, LocalDateTime expirationDate, Boolean isActive) {
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
