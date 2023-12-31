package com.example.myapp.community.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.community.model.Community;
import com.example.myapp.community.model.ReplyVO;
import com.example.myapp.community.service.ICommunityService;
import com.example.myapp.community.service.IReplyService;
import com.example.myapp.user.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommunityController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IReplyService replyService;

	@Autowired
	ICommunityService communityService;

	@RequestMapping("/community/list/{page}")
	public String getListByCommunity(@PathVariable int page, HttpSession session, Model model) {
		session.setAttribute("page", page);
		List<Community> communityList = communityService.selectArticleListByCommunity(page);
		model.addAttribute("communityList", communityList);
		int bbsCount = communityService.selectTotalArticleCountByCommunity();
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 10.0);
		}
		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("bbsCount", bbsCount);
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		return "community/list";
	}

	@RequestMapping("/community/list")
	public String getListByCommunity(HttpSession session, Model model) {
		return getListByCommunity(1, session, model);
	}

	@RequestMapping("/community/{writeId}/{page}")
	public String getCommunityDetails(@PathVariable int writeId, @PathVariable int page, Model model) {
		Community community = communityService.selectArticle(writeId);
		model.addAttribute("writeId", writeId);
		List<ReplyVO> replyList = replyService.replyList(writeId);
		if (community != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedWriteDate = sdf.format(community.getWriteDate());
			community.setFormattedWriteDate(formattedWriteDate);
		}
		System.out.println(replyList);
		model.addAttribute("replyList", replyList);
		System.out.println(replyList);
		model.addAttribute("writeId", writeId);
		model.addAttribute("community", community);
		model.addAttribute("page", page);
		logger.info("getCommunityDetails " + community.toString());
		return "community/view";
	}

	@RequestMapping("/community/{writeId}")
	public String getCommunityDetails(@PathVariable int writeId, Model model) {
		return getCommunityDetails(writeId, 1, model);
	}

	@RequestMapping(value = "/community/write", method = RequestMethod.GET)
	public String writeArticle(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
		if (userId != null && !userId.equals("")) {
			model.addAttribute("userId", userId);
			return "community/write";
		} else {
			model.addAttribute("message", "로그인 하지 않은 사용자입니다.");
			return "user/login";
		}
	}

	@RequestMapping(value = "/community/write", method = RequestMethod.POST)
	public String writeArticle(@Validated Community community, BindingResult result, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
		// logger.info("/community/write : " + community.toString());
//		if (result.hasErrors()) {
//			model.addAttribute("commuinty", community);
//			return "community/write";
//		}
		try {
			String userId = (String) session.getAttribute("userId");
			community.setUserId(userId);
			communityService.insertArticle(community);
			model.addAttribute("message", "새로운 게시글이 등록 되었습니다.");
			model.addAttribute("commuinty", community);
			return "redirect:/community/list";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			return "community/error";
		}
	}

	@RequestMapping(value = "/community/update/{writeId}", method = RequestMethod.GET)
	public String updateArticle(@PathVariable int writeId, Model model) {
		Community community = communityService.selectArticle(writeId);
//		community.setWriteContent(community.getWriteContent().replaceAll("<br>", "\r\n"));
		model.addAttribute("community", community);
		return "community/update";
	}

	@RequestMapping(value = "/community/update", method = RequestMethod.POST)
	public String updateArticle(Community community, RedirectAttributes redirectAttrs) {
		logger.info("/community/update " + community.toString());
		try {
//			community.setWriteContent(community.getWriteContent().replace("\r\n", "<br>"));
//			community.setWriteTitle(Jsoup.clean(community.getWriteTitle(), Safelist.basic()));
//			community.setWriteContent(Jsoup.clean(community.getWriteContent(), Safelist.basic()));
			communityService.updateArticle(community);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/community/" + community.getWriteId();
	}

	@RequestMapping(value = "/community/delete", method = RequestMethod.GET)
	public String deleteArticle(@RequestParam("writeId") int writeId) {
		communityService.deleteArticleByWriteId(writeId);
		return "redirect:/community/list/1";
	}

	@RequestMapping("/community/search/{page}")
	public String search(@RequestParam(required = false, defaultValue = "") String keyword, @PathVariable int page,
			HttpSession session, Model model) {
		try {
			List<Community> communityList = communityService.searchListByContentKeyword(keyword, page);
			model.addAttribute("communityList", communityList);
			int bbsCount = communityService.selectTotalArticleCountByKeyword(keyword);
			int totalPage = 0;
			if (bbsCount > 0) {
				totalPage = (int) Math.ceil(bbsCount / 10.0);
			}
			int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
			int nowPageBlock = (int) Math.ceil(page / 10.0);
			int startPage = (nowPageBlock - 1) * 10 + 1;
			int endPage = 0;
			if (totalPage > nowPageBlock * 10) {
				endPage = nowPageBlock * 10;
			} else {
				endPage = totalPage;
			}
			model.addAttribute("bbsCount", bbsCount);
			model.addAttribute("keyword", keyword);
			model.addAttribute("totalPageCount", totalPage);
			model.addAttribute("nowPage", page);
			model.addAttribute("totalPageBlock", totalPageBlock);
			model.addAttribute("nowPageBlock", nowPageBlock);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "community/search";
	}

	@RequestMapping("/community/search")
	public String search(@RequestParam(required = false, defaultValue = "") String keyword, HttpSession session,
			Model model) {
		return search(keyword, 1, session, model);
	}

	@RequestMapping("/community/mylist/{page}")
	public String mylist(@RequestParam(required = false, defaultValue = "") String userId, @PathVariable int page,
			HttpSession session, Model model) {
		try {
			List<Community> communityList = communityService.searchListByContentmylist(userId, page);
			model.addAttribute("communityList", communityList);
			int bbsCount = communityService.selectTotalArticleCountBymylist(userId);
			int totalPage = 0;
			if (bbsCount > 0) {
				totalPage = (int) Math.ceil(bbsCount / 10.0);
			}
			int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
			int nowPageBlock = (int) Math.ceil(page / 10.0);
			int startPage = (nowPageBlock - 1) * 10 + 1;
			int endPage = 0;
			if (totalPage > nowPageBlock * 10) {
				endPage = nowPageBlock * 10;
			} else {
				endPage = totalPage;
			}
			model.addAttribute("userId", userId);
			model.addAttribute("totalPageCount", totalPage);
			model.addAttribute("nowPage", page);
			model.addAttribute("totalPageBlock", totalPageBlock);
			model.addAttribute("nowPageBlock", nowPageBlock);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("bbsCount", bbsCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "community/mylist";
	}

	@RequestMapping("/community/mylist")
	public String mylist(@RequestParam(required = false, defaultValue = "") String userId, HttpSession session,
			Model model) {
		return mylist(userId, 1, session, model);
	}

//
//	@ExceptionHandler({ RuntimeException.class })
//	public String error(HttpServletRequest request, Exception ex, Model model) {
//		model.addAttribute("exception", ex);
//		model.addAttribute("stackTrace", ex.getStackTrace());
//		model.addAttribute("url", request.getRequestURI());
//		return "error/runtime";
//	}
}