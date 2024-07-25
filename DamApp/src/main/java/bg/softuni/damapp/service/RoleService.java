package bg.softuni.damapp.service;

import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;

import java.util.List;

public interface RoleService {

    void addRoleToUser(User user, UserRoleEnum roleName);

    void removeRoleFromUser(User user, UserRoleEnum role);

    List<UserRole> findAllRolesOfUser();
}
