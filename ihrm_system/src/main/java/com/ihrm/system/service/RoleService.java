package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class RoleService extends BaseService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 分配角色
     */
    public void assignPerms(String roleId, List<String> permIds) {

        //1.获取分配的权限对象
        Role role = roleDao.findById(roleId).get();

        //2.构造角色的权限集合
        Set<Permission> perms = new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao.findById(permId).get();
            //根据父id和类型查询api权限列表
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PERMISSION_API, permission.getId());
            perms.addAll(apiList);//自定义赋予api权限
            perms.add(permission);//当前菜单或按钮的权限
        }
        //3.设置角色和权限集合的关系
        role.setPermissions(perms);
        //4.更新角色
        roleDao.save(role);
    }

    /**
     * 添加用户
     *
     * @param role
     */
    public void save(Role role) {
        String id = idWorker.nextId() + "";
        role.setId(id);
        roleDao.save(role);
    }

    /**
     * 根据id删除用户
     *
     * @param id
     */
    public void delete(String id) {
        roleDao.deleteById(id);
    }

    /**
     * 更新用户
     */
    public void update(Role role) {
        Role target = roleDao.getOne(role.getId());
        target.setDescription(role.getDescription());
        target.setName(role.getName());
        roleDao.save(target);
    }

    /**
     * 根据id查询用户
     */
    public Role findById(String id) {
        return roleDao.findById(id).get();
    }

    /**
     * 5.查询所有用户列表
     * 参数：map集合的形式
     * hasDept
     * department
     * companyId
     * <p>
     * 前端通过map的方式把值传过来
     */
    public Page<Role> findSearch(String companyId, int page, int pagesize) {
        return roleDao.findAll(getSpect(companyId), PageRequest.of(page - 1, pagesize));

    }

    public List<Role> findAll(String companyId) {
        return roleDao.findAll(getSpect(companyId));
    }
}
