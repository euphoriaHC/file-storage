package com.alexlatkin.filestorage.repository;

import com.alexlatkin.filestorage.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByName(String name);
    boolean existsTagByName(String name);

    @Modifying
    @Query(value = """
            INSERT INTO tag_files (tag_id, file_id)
            VALUES (:tagId, :fileId)
            """, nativeQuery = true)
    void assignFile(@Param("tagId") Long tagId, @Param("fileId") Long fileId);
}
