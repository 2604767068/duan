package com.example.demo.club.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 管理员账号表
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@TableName("t_admin")
public class TAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 账号
     */
    @TableField("username")
    private Long username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getUsername() {
        return username;
    }

    public void setUsername(Long username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "TAdmin{" +
            "id=" + id +
            ", name=" + name +
            ", username=" + username +
            ", password=" + password +
        "}";
    }
}
