package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class UserService extends BaseService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RoleDao roleDao;

    /**
     * 根据mobile查询
     */
    public User findByMobile(String mobile){
        return userDao.findByMobile(mobile);

    }

    /**
     * 添加用户
     *
     * @param user
     */
    public void save(User user) {
        String id = idWorker.nextId() + "";
        user.setCreateTime(new Date());
        user.setPassword("123465");
        user.setEnableState(1);
        user.setId(id);
        userDao.save(user);
    }

    /**
     * 根据id删除用户
     *
     * @param id
     */
    public void delete(String id) {
        userDao.deleteById(id);
    }

    /**
     * 更新用户
     */
    public void update(User user) {
        User temp = userDao.getOne(user.getId());
        temp.setUsername(user.getUsername());
        temp.setPassword(user.getPassword());
        temp.setDepartmentId(user.getDepartmentId());
        temp.setDepartmentName(user.getDepartmentName());
        userDao.save(temp);
    }

    /**
     * 根据id查询用户
     */
    public User findById(String id) {
        Optional<User> optional = userDao.findById(id);
        if (optional != null && optional.isPresent()){
            return optional.get();
        }
        return null;
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
    public Page<User> findSearch(int page, int size, Map map) {
        //1.需要查询条件
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态拼接查询条件
             */
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();

                //根据请求的companyId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), map.get("companyId")));
                }

                //根据请求部门id构造查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), map.get("departmentId")));
                }

                //根据请求的hasDept判断，是否分配部门 0未分配(departmentId = null) 1已分配（departmentId != null)
                if (StringUtils.isEmpty(map.get("hasDept"))) {
                    if ("0".equals(map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        //2.分页
        Page<User> userPage = userDao.findAll(spec, PageRequest.of(page - 1, size));
        return userPage;
    }

    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds
     */
    public void assignRoles(String userId, List<String> roleIds) {

        //1.根据id查询用户
        User user = userDao.findById(userId).get();

        //2.更新用户
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //3.更新用户
        userDao.save(user);
    }
}
