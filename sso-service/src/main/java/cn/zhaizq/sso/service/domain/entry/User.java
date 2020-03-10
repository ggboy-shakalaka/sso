package cn.zhaizq.sso.service.domain.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("basic_user")
public class User {
    private Integer id;
    private String userName;
    private String password;
    private Date createTime;
}