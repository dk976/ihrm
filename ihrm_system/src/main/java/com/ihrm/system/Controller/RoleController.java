package com.ihrm.system.Controller;

import com.ihrm.common.comtroller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;
    /**
     * 分配权限
     * @param map
     * @return
     */
    @RequestMapping(value = "/role/assignPrem", method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String, Object> map) {
        //1.获取被分配的角色id
        String roleId = (String) map.get("id");
        //2.获取到权限的id列表
        List<String> permIds = (List<String>) map.get("permIds");
        //3.调用service完成权限分配
        roleService.assignPerms(roleId,permIds);
        return new Result(ResultCode.SUCCESS);
    }
    //添加角色
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Result save(@RequestBody Role role) {
        role.setCompanyId(companyId);
        roleService.save(role);
        return new Result(ResultCode.SUCCESS);
    }
    //更新角色
    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody Role role) {
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }
    //删除角色
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        roleService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 根据ID获取角色信息
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        Role role = roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS, roleResult);
    }
    /**
     * 分页查询角色
     */
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Result findByPage(int page, int pagesize, Role role) {

        Page<Role> pageRole = roleService.findSearch(companyId, page, pagesize);

        PageResult<Role> pageResult = new PageResult(pageRole.getTotalElements(), pageRole.getContent());

        return new Result(ResultCode.SUCCESS, pageResult);
    }

    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public Result findAll() throws Exception {
        List<Role> roleList = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS, roleList);
    }

}
