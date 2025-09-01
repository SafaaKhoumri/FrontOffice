package com.pfe.backend.token;

import com.pfe.backend.Entities.User;
import com.pfe.backend.Entities.UtilisateurFrontoffice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true,length = 1000)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_frontoffice_id")
    public UtilisateurFrontoffice user;

}