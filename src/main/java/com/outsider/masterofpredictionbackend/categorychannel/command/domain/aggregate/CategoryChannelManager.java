package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.ManagerRole;
import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORY_CHANNEL_MANAGER")
public class CategoryChannelManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_CHANNEL_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CategoryChannel categoryChannel;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private ManagerRole role;

    // 역할별 권한 체크 메서드
    public boolean canManageContent() {
        return role == ManagerRole.OWNER || role == ManagerRole.CONTENT_MANAGER;
    }

    public boolean canManageMembers() {
        return role == ManagerRole.OWNER || role == ManagerRole.MEMBER_MANAGER;
    }

    public boolean canManageSettings() {
        return role == ManagerRole.OWNER || role == ManagerRole.SETTINGS_MANAGER;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryChannel getCategoryChannel() {
        return categoryChannel;
    }

    public void setCategoryChannel(CategoryChannel categoryChannel) {
        this.categoryChannel = categoryChannel;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ManagerRole getRole() {
        return role;
    }

    public void setRole(ManagerRole role) {
        this.role = role;
    }
}
