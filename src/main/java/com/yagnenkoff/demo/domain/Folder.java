package com.yagnenkoff.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "folders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor
@Data
public class Folder {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private Long parentId;
}
