package org.example.demo.model;

public class CommonResponse {

    public static final String OK = "0";

    private String code;
    private String message;
    private Object data;

    public CommonResponse() {}

    public CommonResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static CommonResponse ok(Object data){
        return new CommonResponse(OK, "", data);
    }

    public static CommonResponse fail(String code, Exception ex){
        return new CommonResponse(code, ex.getMessage(), null);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
