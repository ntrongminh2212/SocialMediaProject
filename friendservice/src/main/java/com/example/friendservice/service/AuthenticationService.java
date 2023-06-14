package com.example.friendservice.service;

import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.User;
import com.example.friendservice.facade.UserFacade;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.util.JwtTokenUtil;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationService {

    private static Logger logger = Logger.getLogger(AuthenticationService.class);

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserFacade userFacade;

    @Autowired
    UserMapper userMapper;

    public Optional<UserDTO> authenticateToken(String accessToken) {
        if (StringUtils.hasText(accessToken) && jwtTokenUtil.validateToken(accessToken)) {
            logger.info("[Access token found] " + accessToken);
            // Lấy id user từ chuỗi jwt
            Long userId = jwtTokenUtil.getUserIdFromJWT(accessToken);
            // Lấy thông tin người dùng từ id
            Optional<User> userOptional = userFacade.getUserById(userId);
            if (userOptional.isPresent()) {
                UserDTO userDTO = userMapper.userToUserDTO(userOptional.get());
                return Optional.ofNullable(userDTO);
            }
        }
        return Optional.empty();
    }
}
