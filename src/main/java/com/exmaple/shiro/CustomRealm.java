package com.exmaple.shiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;

public class CustomRealm extends JdbcRealm {

    protected String authenticationViaTokenQuery = "SELECT login, password ,pubtoken FROM users WHERE pubtoken=?";

    public CustomRealm() {
        this.setAuthenticationQuery("SELECT password FROM users WHERE login=?");
        this.setUserRolesQuery("SELECT role.name FROM role,users_roles,users WHERE role.roleId=users_roles.roleId AND users_roles.userId=users.userId AND users.login=?");
        this.setPermissionsQuery("SELECT permission FROM role_permission,role WHERE role_permission.roleId=role.roleId AND role.name=?");
        this.setCredentialsMatcher(new CustomCredentialMatcher());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;
        Connection conn = null;
        if (token instanceof BearerAuthenticationToken) {
            BearerAuthenticationToken bearerAuthenticationToken = (BearerAuthenticationToken) token;
            String authToken = bearerAuthenticationToken.getPubToken();
            try {
                conn = dataSource.getConnection();
                String result[] = getUserNamePasswordForPubToken(conn, authToken);
                if (result.length != 3 || result[0] == null || result[1] == null) {
                    throw new UnknownAccountException("No account found for Auth Token [" + authToken + "]");
                }
                info = new CustomSimpleAuthenticationInfo(result[0], result[1].toCharArray(), getName(), result[2]);
            } catch (SQLException e) {
                final String message = "There was a SQL error while authenticating pubtoken [" + authToken + "]";
                System.out.println(e.getMessage());
                throw new AuthenticationException(message, e);
            } finally {
                JdbcUtils.closeConnection(conn);
            }

        } else {
            info = (SimpleAuthenticationInfo) super.doGetAuthenticationInfo(token);
        }
        return info;
    }

    @SuppressWarnings("resource")
    private String[] getUserNamePasswordForPubToken(Connection conn, String authToken) throws SQLException {

        String[] result = new String[3];
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(authenticationViaTokenQuery);
            ps.setString(1, authToken);
            rs = ps.executeQuery();
            boolean foundResult = false;
            while (rs.next()) {
                if (foundResult) {
                    throw new AuthenticationException("More than one user row found for user [" + authToken
                            + "]. Usernames must be unique.");
                }
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);
                result[2] = rs.getString(3);
                foundResult = true;
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }

        return result;
    }

}
