package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    //保存企业
    //@RequestBody  可以把前端传来的属性装到这里面去
    //@ResponseBody 自动把Result这个对象转化成字符串响应给前端
    //@RestController 已经包含@ResponseBody属性
    //public @ResponseBody Result save(@RequestBody Company company){
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result save(@RequestBody Company company){
        //业务操作
        companyService.add(company);
        //封装对象给前端
        return new Result(ResultCode.SUCCESS);
    }

    //根据id更新企业
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody Company company){
        //业务操作
        company.setId(id);
        companyService.update(company);
        //封装对象给前端
        return new Result(ResultCode.SUCCESS);
    }

    //根据id删除企业
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id){
        //业务操作
        companyService.deleteById(id);
        //封装对象给前端
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询企业
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id){
        //业务操作
        Company company = companyService.findById(id);
        //封装对象给前端
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(company);
        return result;
    }

    //查询全部企业列表
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Result findAll(){
        //业务操作
        List<Company> companyList = companyService.findAll();
        //封装对象给前端
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(companyList);
        return result;
    }
}
