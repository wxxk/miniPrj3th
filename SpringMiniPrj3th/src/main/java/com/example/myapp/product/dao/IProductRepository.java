package com.example.myapp.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.product.model.Product;

@Repository
@Mapper
public interface IProductRepository {
	List<Product> selectAllProduct(@Param("categoryId") int categoryId);
	int insertCategory(@Param("categoryName") String categoryName);
}