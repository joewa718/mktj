package com.mktj.cn.web.mapper;

import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.vo.UserVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);

    User userToUserVo(UserVo userVo);

    List<UserDTO> userToUserDTOList(List<User> userList);

    List<UserDTO> userToUserDTOList(Iterable<User> userList);
}