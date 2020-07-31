package com.sue.pojo;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author sue
 * @date 2020/7/31 13:29
 */

@Entity
@Data
public class Users {
    @Id
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String realname;
    private String face;
    private String mobile;
    private String email;
    private Integer sex;
    private Date birthday;
    private Timestamp createdTime;
    private Timestamp updatedTime;


}
