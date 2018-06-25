package com.jbb.mgt.rs.action.roles;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Permissions;
import com.jbb.mgt.core.domain.Roles;
import com.jbb.mgt.core.service.PermissionsService;
import com.jbb.mgt.core.service.RolesService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsPermissions;
import com.jbb.mgt.server.rs.pojo.RsRoles;

/**
 * 角色和权限表Action
 * 
 * @author wyq
 * @date 2018/04/28
 */
@Service(RolesAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RolesAction extends BasicAction {

    public static final String ACTION_NAME = "RolesAction";

    private static Logger logger = LoggerFactory.getLogger(RolesAction.class);

    @Autowired
    private RolesService rolesService;

    @Autowired
    private PermissionsService permissionsService;

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getRolesAndPermissions() {
        logger.debug(">getRolesAndPermissions");
        List<Roles> roles = rolesService.selectRoles();
        this.response.roles = new ArrayList<RsRoles>(roles.size());
        for (Roles role : roles) {
            //忽略系统
            if(role.getRoleId() ==1){
                continue;
            }
            List<Integer> list = rolesService.selectRolesAndPermissionsByRoleId(role.getRoleId());
            this.response.roles.add(new RsRoles(role, list));
        }

        List<Permissions> listP = permissionsService.selectPermissions();
        this.response.permissions = new ArrayList<RsPermissions>(listP.size());
        for (Permissions permissions : listP) {
          //忽略系统管理
            if(permissions.getPermissionId() == 1){
                continue;
            }
            this.response.permissions.add(new RsPermissions(permissions));
        }
        logger.debug("<getRolesAndPermissions");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<RsRoles> roles;

        private List<RsPermissions> permissions;

        public List<RsRoles> getRoles() {
            return roles;
        }

        public List<RsPermissions> getPermissions() {
            return permissions;
        }

        
    }

}
