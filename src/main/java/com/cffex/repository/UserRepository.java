package com.cffex.repository;


import com.cffex.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findByUserName(String username) {

        User result = jdbcTemplate.queryForObject(
                "SELECT userId, username, password, salt,createdOn FROM user where username=?",
                new Object[]{username},
                new RowMapper<User>(){
                    public User mapRow(ResultSet rs, int arg1) throws SQLException {
                        User user=User.builder().build();
                        user.setUserId(rs.getInt("userId"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setSalt(rs.getString("salt"));
                        user.setCreatedOn(rs.getDate("createdOn"));
                        return user;
                    }
                });
        return result;
    }

    public void createUser(String username, String password, String salt) {
        jdbcTemplate.update("INSERT INTO user(username, password, salt,createdOn) VALUES (?,?,?,?)",
                username, password,salt, new Date());
    }

    public boolean isUsernameTaken(String username) {
        Integer count = jdbcTemplate.queryForObject("select count(1) as cnt from user where username=? ",
                new Object[]{username},
                new RowMapper<Integer>(){
                    public Integer mapRow(ResultSet arg0, int arg1) throws SQLException {
                        return arg0.getInt("cnt");
                    }
                }
//                new ResultSetExtractor<Integer>() {
//                    public Integer extractData(final ResultSet rs) throws SQLException {
//                        int cnt=0;
//                        if (rs.next()) {
//                            cnt= rs.getInt("cnt");
//                        }
//                        return cnt;
//                    }
//                }

                );
        return count>0;
    }


}
