package com.alexlatkin.filestorage.repository;

import com.alexlatkin.filestorage.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findFileByName(String name);
    @Query(value = """
            SELECT * FROM files f
            JOIN tag_files tf ON f.id = tf.file_id
            WHERE f.access_modifier = 'PUBLIC'
            AND tf.tag_id = :tagId
            """, nativeQuery = true)
    List<File> findPublicFilesByTagId(@Param("tagId") Long tagId);
    @Query(value = """
            SELECT * FROM files f
            WHERE f.access_modifier = 'PUBLIC'
            AND f.user_id = :userId
            """, nativeQuery = true)
    List<File> findPublicFilesByUserId(@Param("userId") Long userId);
    List<File> findFilesByUserId(Long userId);
    boolean existsFileByName(String name);
}
