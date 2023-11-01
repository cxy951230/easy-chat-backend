package com.cxy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxy.annotation.Field;
import com.cxy.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.JDBCType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("chat_name_space")
public class ChatNameSpace implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Field(value = "id",primary = true,type = JDBCType.INTEGER,len = "11")
    private Integer id;
    @Field(value = "name", type = JDBCType.VARCHAR, len = "20")
    private String name;

}
