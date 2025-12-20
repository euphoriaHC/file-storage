package com.alexlatkin.filestorage.model.entity;

import com.alexlatkin.filestorage.model.FileAccessModifier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "upload_date")
    private String uploadDate;
    @Column(name = "number_of_downloads")
    private int numberOfDownloads;
    @Column(name = "extension")
    private String extension;
    @Column(name = "access_modifier")
    @Enumerated(EnumType.STRING)
    private FileAccessModifier accessModifier;
    @Column(name = "storage_file_name")
    private String storageFileName;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @ManyToMany(mappedBy = "files", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Tag> tags;

    public File(Long id, String name, String description, String uploadDate, int numberOfDownloads,
                String extension, FileAccessModifier accessModifier, User user, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uploadDate = uploadDate;
        this.numberOfDownloads = numberOfDownloads;
        this.extension = extension;
        this.accessModifier = accessModifier;
        this.user = user;
        this.tags = tags;
    }
}
