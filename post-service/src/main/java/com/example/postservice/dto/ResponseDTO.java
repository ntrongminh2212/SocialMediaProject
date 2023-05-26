package com.example.postservice.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseDTO {
    public static Map<String,Object> BADREQUEST;
    public static  Map<String,Object> NOTFOUND;

     static {
        BADREQUEST = new HashMap<>();
        BADREQUEST.put("status",400);
        BADREQUEST.put("error", "Bad Request");

        NOTFOUND = new HashMap<>();
        NOTFOUND.put("status",404);
        NOTFOUND.put("error", "Not Found");
    }
}
