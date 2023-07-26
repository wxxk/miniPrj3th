package com.example.myapp.purchase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myapp.purchase.model.Purchase;
import com.example.myapp.purchase.service.IPurchaseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PurchaseController {

	@Autowired
	IPurchaseService purchaseService;
	
	@RequestMapping("/")
	public String index(Model model) {
		List<Purchase> list = purchaseService.selectTopThree();
		model.addAttribute("pTopThree", list);
		return "index";
	}
		
	@RequestMapping(value="/purchase/insert", method=RequestMethod.GET)
	public String InsertPurchase(@RequestParam List<Integer> cartIdList, Purchase purchase, HttpSession session, Model model) {
		String userId = (String)session.getAttribute("userId");
		purchase = purchaseService.selectUserInfo(userId);

		List<Purchase> list = purchaseService.selectCartInfo(cartIdList, userId);
		model.addAttribute("list", list);
		model.addAttribute("purchase", purchase);

		int sum = 0;
		int cnt = 0;
		for (Purchase i : list) {
			sum += i.getProductPrice() * i.getCartCnt();
			cnt++;
		}
		model.addAttribute("sum", sum);
		model.addAttribute("cnt", cnt);
		return "purchase/insert";
	}

	@RequestMapping(value = "/purchase/buy", method = RequestMethod.GET)
	public String InsertPurcahse(Purchase purchase, HttpSession session, Model model, int cartCnt) {
		Purchase buy = purchaseService.selectProductInfo(purchase.getProductId());
		int sum = (buy.getProductPrice()* cartCnt);
		buy.setCartCnt(cartCnt);
		model.addAttribute("buy", buy);

		String userId = (String) session.getAttribute("userId");
		purchase = purchaseService.selectUserInfo(userId);

		model.addAttribute("purchase", purchase);
		model.addAttribute("sum", sum);

		return "purchase/insert";
	}

	@PostMapping("/purchase/insert")
	public String InsertPurcahse(@RequestParam List<Integer> productId, @RequestParam List<Integer> purchaseCnt,
			Purchase purchase, HttpSession session) {
		
		purchase.setUserId((String) session.getAttribute("userId"));
		purchaseService.insert(productId, purchaseCnt, purchase);

		return "redirect:/purchase/list";
	}

	@RequestMapping("/purchase/list")
	public String selectAllPurchase(Model model, Purchase purchase, HttpSession session) {
		List<Purchase> purchaseList = purchaseService.selectPurchaseList((String) session.getAttribute("userId"));
		model.addAttribute("purchaseList", purchaseList);
		return "purchase/list";
	}

	@RequestMapping("/purchase/list/{purchaseId}")
	public String selectPurchaseDetail(@PathVariable int purchaseId, Model model, Purchase purchase,
			HttpSession session) {
		purchase.setUserId((String) session.getAttribute("userId"));

		Purchase userInfo = purchaseService.selectPurchaseUserDetail(purchaseId);
		model.addAttribute("userInfo", userInfo);

		List<Purchase> purchaseList = purchaseService.selectPurchaseProductDetail(purchaseId);
		model.addAttribute("purchaseList", purchaseList);

		model.addAttribute("purchaseId", purchaseId);
		System.out.println(purchaseList);
		return "purchase/detail";
	}
}
