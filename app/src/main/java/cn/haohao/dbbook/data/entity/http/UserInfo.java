package cn.haohao.dbbook.data.entity.http;


public class UserInfo extends BaseResponse {
    private static String token;

    public UserInfo(int code, String msg) {
        super(code, msg);
    }

    public static String getToken() {
        return token;
    }

}
