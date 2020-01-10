package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class DepartmentService extends BaseService {
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 1.保存部门
     */
    public void add(Department department) {
        String id = idWorker.nextId() + "";
        department.setId(id);
        departmentDao.save(department);
    }

    /**
     * 2.更新部门
     */
    public void update(Department department) {
        Department dept = departmentDao.findById(department.getId()).get();
        dept.setName(department.getName());
        dept.setCode(department.getCode());
        departmentDao.save(dept);
    }

    /**
     * 3.根据id查询部门
     */
    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    /**
     * 4.查询全部部门列表
     */
    public List<Department> findAll(String companyId) {
        /**
         *查询全部部门列表带有参数的时候，这样弄new Specification<Department>()
         * 1.只查询companyId
         * 2.很多地方都需要根据companyId查询
         * 3.很多对象中都具有companyId
         */
//        Specification<Department> spec = new Specification<Department>() {
//            /**
//             *
//             * @param root ：包含了所有的对象数据
//             * @param criteriaQuery ：一般不用
//             * @param criteriaBuilder ：构造查询条件
//             *                        company_id = "1"
//             * @return
//             */
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                //根据企业id查询
//                return criteriaBuilder.equal(root.get("companyId").as(String.class), companyId);
//            }
//        };
        return departmentDao.findAll(getSpect(companyId));
    }

    /**
     * 5.根据id删除部门
     */
    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }
}
