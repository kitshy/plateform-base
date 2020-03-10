package cn.plateform.pub.aop;

import lombok.Getter;

@Getter
public enum  AuthTypeEnum {

    Not_Limit(0,"无须登陆"),
    Need_Login(1,"需要登陆"),
    Need_Admin(2,"需要管理员");

    private int value;
    private String memo;

    AuthTypeEnum(int value,String memo){
        this.value=value;
        this.memo=memo;
    }

}
