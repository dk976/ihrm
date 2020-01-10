package com.ihrm.company.dao;

import com.ihrm.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JpaRepository<Company,String>
 *     company 实体类类型
 *     string 主键类型
 */
public interface CompanyDao extends JpaRepository<Company,String> , JpaSpecificationExecutor<Company> {
}
