package com.moin.transfer.entity;

import com.moin.transfer.common.enums.IdType;
import com.moin.transfer.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "id_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private IdType idType;

    @Column(name = "id_value", length = 50, nullable = false)
    private String idValue;

    @Column(name = "role", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quote> quotes = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, IdType idType, String idValue, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.idType = idType;
        this.idValue = idValue;
        this.role = role;
    }

    // 연관관계 편의 메서드
    public void addQuote(Quote quote) {
        quotes.add(quote);
        quote.setUser(this);  // 양방향 연관관계 설정
    }

    // 연관관계 제거 메서드
    public void removeQuote(Quote quote) {
        quotes.remove(quote);
        quote.setUser(null);
    }
}