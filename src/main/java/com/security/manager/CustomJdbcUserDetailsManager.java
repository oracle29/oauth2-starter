package com.security.manager;

import org.springframework.security.provisioning.JdbcUserDetailsManager;

/**
 * Created by oracle on 2017-09-07.
 */
public class CustomJdbcUserDetailsManager extends JdbcUserDetailsManager {
    // UserDetailsManager SQL
    public static final String DEF_CREATE_USER_SQL = "insert into oauth_users (username, password, enabled) values (?,?,?)";
    public static final String DEF_DELETE_USER_SQL = "delete from oauth_users where username = ?";
    public static final String DEF_UPDATE_USER_SQL = "update oauth_users set password = ?, enabled = ? where username = ?";
    public static final String DEF_INSERT_AUTHORITY_SQL = "insert into oauth_authorities (username, authority) values (?,?)";
    public static final String DEF_DELETE_USER_AUTHORITIES_SQL = "delete from oauth_authorities where username = ?";
    public static final String DEF_USER_EXISTS_SQL = "select username from oauth_users where username = ?";
    public static final String DEF_CHANGE_PASSWORD_SQL = "update oauth_users set password = ? where username = ?";

    // GroupManager SQL
    public static final String DEF_FIND_GROUPS_SQL = "select group_name from oauth_groups";
    public static final String DEF_FIND_USERS_IN_GROUP_SQL = "select username from oauth_group_members gm, oauth_groups g "
            + "where gm.group_id = g.id" + " and g.group_name = ?";
    public static final String DEF_INSERT_GROUP_SQL = "insert into oauth_groups (group_name) values (?)";
    public static final String DEF_FIND_GROUP_ID_SQL = "select id from oauth_groups where group_name = ?";
    public static final String DEF_INSERT_GROUP_AUTHORITY_SQL = "insert into oauth_group_authorities (group_id, authority) values (?,?)";
    public static final String DEF_DELETE_GROUP_SQL = "delete from oauth_groups where id = ?";
    public static final String DEF_DELETE_GROUP_AUTHORITIES_SQL = "delete from oauth_group_authorities where group_id = ?";
    public static final String DEF_DELETE_GROUP_MEMBERS_SQL = "delete from oauth_group_members where group_id = ?";
    public static final String DEF_RENAME_GROUP_SQL = "update oauth_groups set group_name = ? where group_name = ?";
    public static final String DEF_INSERT_GROUP_MEMBER_SQL = "insert into oauth_group_members (group_id, username) values (?,?)";
    public static final String DEF_DELETE_GROUP_MEMBER_SQL = "delete from oauth_group_members where group_id = ? and username = ?";
    public static final String DEF_GROUP_AUTHORITIES_QUERY_SQL = "select g.id, g.group_name, ga.authority "
            + "from oauth_groups g, oauth_group_authorities ga "
            + "where g.group_name = ? "
            + "and g.id = ga.group_id ";
    public static final String DEF_DELETE_GROUP_AUTHORITY_SQL = "delete from oauth_group_authorities where group_id = ? and authority = ?";


    private String createUserSql = DEF_CREATE_USER_SQL;
    private String deleteUserSql = DEF_DELETE_USER_SQL;
    private String updateUserSql = DEF_UPDATE_USER_SQL;
    private String createAuthoritySql = DEF_INSERT_AUTHORITY_SQL;
    private String deleteUserAuthoritiesSql = DEF_DELETE_USER_AUTHORITIES_SQL;
    private String userExistsSql = DEF_USER_EXISTS_SQL;
    private String changePasswordSql = DEF_CHANGE_PASSWORD_SQL;

    private String findAllGroupsSql = DEF_FIND_GROUPS_SQL;
    private String findUsersInGroupSql = DEF_FIND_USERS_IN_GROUP_SQL;
    private String insertGroupSql = DEF_INSERT_GROUP_SQL;
    private String findGroupIdSql = DEF_FIND_GROUP_ID_SQL;
    private String insertGroupAuthoritySql = DEF_INSERT_GROUP_AUTHORITY_SQL;
    private String deleteGroupSql = DEF_DELETE_GROUP_SQL;
    private String deleteGroupAuthoritiesSql = DEF_DELETE_GROUP_AUTHORITIES_SQL;
    private String deleteGroupMembersSql = DEF_DELETE_GROUP_MEMBERS_SQL;
    private String renameGroupSql = DEF_RENAME_GROUP_SQL;
    private String insertGroupMemberSql = DEF_INSERT_GROUP_MEMBER_SQL;
    private String deleteGroupMemberSql = DEF_DELETE_GROUP_MEMBER_SQL;
    private String groupAuthoritiesSql = DEF_GROUP_AUTHORITIES_QUERY_SQL;
    private String deleteGroupAuthoritySql = DEF_DELETE_GROUP_AUTHORITY_SQL;

    public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,enabled "
            + "from oauth_users " + "where username = ?";
    public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select username,authority "
            + "from oauth_authorities " + "where username = ?";
    public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority "
            + "from oauth_groups g, oauth_group_members gm, oauth_group_authorities ga "
            + "where gm.username = ? " + "and g.id = ga.group_id "
            + "and g.id = gm.group_id";

    public CustomJdbcUserDetailsManager() {
        super.setCreateUserSql(createUserSql);
        super.setDeleteUserSql(deleteUserSql);
        super.setUpdateUserSql(updateUserSql);
        super.setCreateAuthoritySql(createAuthoritySql);
        super.setDeleteUserAuthoritiesSql(deleteUserAuthoritiesSql);
        super.setUserExistsSql(userExistsSql);
        super.setChangePasswordSql(changePasswordSql);
        super.setFindAllGroupsSql(findAllGroupsSql);
        super.setFindUsersInGroupSql(findUsersInGroupSql);
        super.setInsertGroupSql(insertGroupSql);
        super.setFindGroupIdSql(findGroupIdSql);
        super.setInsertGroupAuthoritySql(insertGroupAuthoritySql);
        super.setDeleteGroupSql(deleteGroupSql);
        super.setDeleteGroupAuthoritiesSql(deleteGroupAuthoritiesSql);
        super.setDeleteGroupMemberSql(deleteGroupMemberSql);
        super.setDeleteGroupMembersSql(deleteGroupMembersSql);
        super.setRenameGroupSql(renameGroupSql);
        super.setInsertGroupMemberSql(insertGroupMemberSql);
        super.setGroupAuthoritiesSql(groupAuthoritiesSql);
        super.setDeleteGroupAuthoritySql(deleteGroupAuthoritySql);

        super.setAuthoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);
        super.setUsersByUsernameQuery(DEF_USERS_BY_USERNAME_QUERY);
        super.setGroupAuthoritiesByUsernameQuery(DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY);
    }


}
