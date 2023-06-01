package com.example.messageservice.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseDTO {
    public static Map<String,Object> BADREQUEST;
    public static  Map<String,Object> NOTFOUND;

    static  {
//        "timestamp": "2023-05-26T04:07:06.880+00:00",
//                "status": 500,
//                "error": "Internal Server Error",
//                "path": "/post/react-post"
        BADREQUEST = new HashMap<>();
        BADREQUEST.put("status",400);
        BADREQUEST.put("error", "Bad Request");

        NOTFOUND = new HashMap<>();
        NOTFOUND.put("status",404);
        NOTFOUND.put("error", "Not Found");
    }
}
