package com.outsider.masterofpredictionbackend.dm.command.domain.aggregate;


import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MESSAGE_REACTIONS")
public class MessageReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user; // User와의 관계 설정

    @Column(name = "reaction_type", nullable = false)
    private String reactionType; // 예: "like", "heart", "thumbs_up"

    @Column(name = "room_id", nullable = false)
    private String roomId;
    // Message와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id",  foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Message message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }




    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
