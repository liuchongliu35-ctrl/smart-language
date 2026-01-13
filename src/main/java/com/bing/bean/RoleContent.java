package com.bing.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleContent {
    private static final String ROLE_USER="user";
    private static final String ROLE_ASSISTANT="assistant";
    private static final String ROLE_SYSTEM="system";
    private String role;
    private String content;

    /**这个类用于组合对应的关系，
     * user:用户
     * assistant:
     * @param content
     * @return
     */
    public RoleContent createUser(String content){
        return new RoleContent(ROLE_USER,content);
    }
    public RoleContent createAssistant(String content){
        return new RoleContent(ROLE_ASSISTANT,content);
    }
    public RoleContent createSystem(String content){
        return new RoleContent(ROLE_SYSTEM,content);
    }
}
