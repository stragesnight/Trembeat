package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.Comment;
import lombok.Data;

import java.util.Date;

/**
 * Comment view model during display
 */
@Data
public class CommentViewModel {
    private String text;

    private UserViewModel user;

    private Date creationDate;


    public CommentViewModel(Comment comment) {
        text = comment.getText();
        user = new UserViewModel(comment.getUser());
        creationDate = comment.getCreationDate();
    }
}
