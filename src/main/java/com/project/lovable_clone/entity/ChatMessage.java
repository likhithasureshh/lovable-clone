package com.project.lovable_clone.entity;

import com.project.lovable_clone.enums.MessageRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "chat_messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
            @JoinColumns(
                    {
                            @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false),
                            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
                    }
            )
    ChatSession chatSession;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "chatMessage")
    List<ChatEvent> events;

    @Column(columnDefinition = "text")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageRole role;
    Integer tokensUsed = 0;

    @CreationTimestamp
    Instant createdAt;


}
