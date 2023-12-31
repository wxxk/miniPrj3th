package com.example.myapp.purchase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.cart.dao.ICartRepository;
import com.example.myapp.purchase.dao.IPurchaseRepository;
import com.example.myapp.purchase.model.Purchase;

@Service
public class PurchaseService implements IPurchaseService{
	
	@Autowired
	IPurchaseRepository purchaseRepository;
	
	@Autowired
	ICartRepository cartRepository;

	@Override
	public Purchase selectUserInfo(String userId) {
		return purchaseRepository.selectUserInfo(userId);
	}

	@Override
	public Purchase selectProductInfo(int productId) {
		return purchaseRepository.selectProductInfo(productId);
	}

	@Override
	public List<Purchase> selectCartInfo(List<Integer> cartIdList, String UserId) {
		return purchaseRepository.selectCartInfo(cartIdList, UserId);
	}

	@Override
	public void insertPurchase(Purchase purchase) {
		purchaseRepository.insertPurchase(purchase);
	}

	@Override
	public void insertPurchaseDetail(Purchase purchase) {
		purchaseRepository.insertPurchaseDetail(purchase);
	}

	@Override
	public int getPurchaseId() {
		return purchaseRepository.getPurchaseId();
	}

	@Override
	public List<Purchase> selectPurchaseList(String userId) {
		return purchaseRepository.selectPurchaseList(userId);
	}

	@Override
	public Purchase selectPurchaseUserDetail(int purchaseId) {
		return purchaseRepository.selectPurchaseUserDetail(purchaseId);
	}

	@Override
	public List<Purchase> selectPurchaseProductDetail(int purchaseId) {
		return purchaseRepository.selectPurchaseProductDetail(purchaseId);
	}

	@Override
	public List<Purchase> selectTopThree() {
		return purchaseRepository.selectTopThree();
	}

	@Override
	public void deleteCartPurchase(int productId) {
		purchaseRepository.deleteCartPurchase(productId);
	}
}
