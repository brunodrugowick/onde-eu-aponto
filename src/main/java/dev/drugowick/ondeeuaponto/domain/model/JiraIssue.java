package dev.drugowick.ondeeuaponto.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * This could be used to persist issue information to a database
 */
@Entity
@Getter
@Setter
public class JiraIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String key;
    private String summary;

    @Column(length = 2000)
    private String description;
}
