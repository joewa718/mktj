package com.mktj.cn.web.mapper;

import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings( @Mapping(source = "phone", target = "username"))
    UserDTO userToUserDTO(User user);
    @Mappings(@Mapping(source = "username", target = "phone"))
    User userToUserVo(UserVo userVo);

    List<UserDTO> userToUserDTOList(List<User> userList);

    List<UserDTO> userToUserDTOList(Iterable<User> userList);
}