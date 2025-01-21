package com.hyuk.side.entities;

import lombok.Data;

import java.util.List;

@Data
public class GroupChatRequest {
    private String groupName;
    private List<String> selectedEmails;
}
