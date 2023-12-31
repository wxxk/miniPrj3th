package com.example.myapp.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.product.dao.IProductRepository;
import com.example.myapp.product.model.Category;
import com.example.myapp.product.model.Product;
import com.example.myapp.product.model.UploadImage;
import com.example.myapp.product.model.UploadProduct;

@Service
public class productService implements IProductService{

	@Autowired
	IProductRepository productRepository;
	
	@Override
	public List<Product> selectAllProduct(int categoryId) {
		return productRepository.selectAllProduct(categoryId);
	}
	
	@Override
	public List<Product> selectPagingProduct(int categoryId, int page) {
		int start = (page-1) * 9 + 1;
		return productRepository.selectPagingProduct(start, start+8, categoryId);
	}
	
	@Override
	public List<Product> selectRateOrderProduct(int categoryId, int page) {
		int start = (page-1) * 9 + 1;
		return productRepository.selectRateOrderProduct(start, start+8, categoryId);
	}
	
	@Override
	public List<Product> selectPriceOrderProduct(int categoryId, int page) {
		int start = (page-1) * 9 + 1;
		return productRepository.selectPriceOrderProduct(start, start+8, categoryId);
	}

	@Override
	public int insertCategory(String categoryName) {
		return productRepository.insertCategory(categoryName);
	}

	@Override
	public List<Category> selectAllCategory() {
		return productRepository.selectAllCategory();
	}

	@Override
	public int deleteCategory(int categoryId) {
		return productRepository.deleteCategory(categoryId);
	}
	
	@Transactional
	public void insertProducts(UploadProduct product, UploadImage img) {
		int row = productRepository.insertProduct(product);
		if(row != 0) {
			int productId = productRepository.selectProductId();
			int rowNum = productRepository.insertProductImg(img, productId);
		}
	}

	@Override
	public void insertProducts(UploadProduct product) {
		productRepository.insertProduct(product);
	}
	
	@Transactional
	public int updateProduct(UploadProduct product, UploadImage img) {
		int row = productRepository.updateProduct(product);
		if(row != 0) {
			int productId = product.getProductId();
			int rowNum = productRepository.updateProductImg(img, productId);
			return rowNum;
		}else {
			return row;
		}
	}
	
	@Override
	public int updateProduct(UploadProduct product) {
		return productRepository.updateProduct(product);
	}

	@Override
	public UploadImage getFile(int imgId) {
		return productRepository.getFile(imgId);
	}

	@Override
	public int deleteProduct(int productId) {
		return productRepository.deleteProduct(productId);
	}

	@Override
	public int deleteBackProduct(int productId) {
		return productRepository.deleteBackProduct(productId);
	}

	@Override
	public Product selectProduct(int productId) {
		return productRepository.selectProduct(productId);
	}

	@Override
	public int checkCategory(String categoryName) {
		return productRepository.checkCategory(categoryName);
	}

	@Override
	public int selectCountCategory() {
		return productRepository.selectCountCategory();
	}

	@Override
	public List<Category> selectPagingCategory(int min, int max) {
		return productRepository.selectPagingCategory(min, max);
	}


	@Override
	public int selectCountUseProduct(int categoryId) {
		return productRepository.selectCountUseProduct(categoryId);
	}




}
