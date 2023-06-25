package com.trembeat.domain.repository;

import com.trembeat.domain.models.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Sound access repository
 */
public interface SoundRepository extends JpaRepository<Sound, Long> {
    /**
     * Find all sounds that match given title or username or genre name
     * @param title Title of sound to be found
     * @param username Username of author to be found
     * @param genreName Genre name of sound to be found
     * @param pageable Pagination to apply for query
     * @return Set of found sounds
     */
    Page<Sound> findAllByTitleLikeIgnoreCaseOrAuthor_UsernameLikeIgnoreCaseOrGenre_NameLikeIgnoreCase(
            String title,
            String username,
            String genreName,
            Pageable pageable);

    /**
     * Find all sounds that match given query
     * @param query Search query
     * @param pageable Pagination to apply for query
     * @return Set of found sounds
     */
    default Page<Sound> findAllByQuery(String query, Pageable pageable) {
        return findAllByTitleLikeIgnoreCaseOrAuthor_UsernameLikeIgnoreCaseOrGenre_NameLikeIgnoreCase(
                query, query, query, pageable);
    }

    /**
     * Find all sounds uploaded by user with given id
     * @param authorId Id of author of sound to find
     * @param pageable Pagination to apply for query
     * @return Set of found sounds
     */
    Page<Sound> findAllByAuthor_Id(Long authorId, Pageable pageable);

    /**
     * Find first 10 matching sounds by given title or username or genre name
     * @param title Partial title to search for
     * @param username Username of author to be found
     * @param genreName Genre name of sound to be found
     * @return Set of found sound titles
     */
    List<Sound> findTop10ByTitleLikeIgnoreCaseOrAuthor_UsernameLikeIgnoreCaseOrGenre_NameLikeIgnoreCase(
            String title, String username, String genreName);

    /**
     * Find top 10 sound titles that match given query
     * @param query Search query
     * @return List of found sound titles
     */
    default List<String> findTop10TitlesByQuery(String query) {
        return findTop10ByTitleLikeIgnoreCaseOrAuthor_UsernameLikeIgnoreCaseOrGenre_NameLikeIgnoreCase(
                        query, query, query)
                .stream()
                .map(Sound::getTitle)
                .toList();
    }
}
