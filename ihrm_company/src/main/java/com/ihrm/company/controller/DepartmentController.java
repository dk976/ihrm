package com.ihrm.company.controller;

import com.ihrm.common.comtroller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.response.DeptListResult;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//解决跨域
@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public Result save(@RequestBody Department department) {
        department.setCompanyId(companyId);
        departmentService.add(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody Department department) {
        department.setId(id);
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS, department);
    }

    @RequestMapping(value = "/department", method = RequestMethod.GET)
    public Result findALL() {
        //1.指定企业id
        Company company = companyService.findById(companyId);
        //2.完成查询
        List<Department> departmentList = departmentService.findAll(companyId);
        //3.构造返回结果
        DeptListResult deptListResult = new DeptListResult(company, departmentList);
        return new Result(ResultCode.SUCCESS, deptListResult);
    }
}
