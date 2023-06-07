package com.example.friendservice.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseDTO {
    public static final Map<String,Object> UNAUTHORIZED = new HashMap<>();
    public static Map<String,Object> BADREQUEST = new HashMap<>();
    public static  Map<String,Object> NOTFOUND = new HashMap<>();

    static  {
//        "timestamp": "2023-05-26T04:07:06.880+00:00",
//                "status": 500,
//                "error": "Internal Server Error",
//                "path": "/post/react-post"
        BADREQUEST.put("status",400);
        BADREQUEST.put("error", "Bad Request");

        NOTFOUND.put("status",404);
        NOTFOUND.put("error", "Not Found");

        UNAUTHORIZED.put("status",401);
        UNAUTHORIZED.put("error", "Unauthorized");
    }
}
