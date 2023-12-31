package com.example.myapp.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.community.dao.ICommunityRepository;
import com.example.myapp.community.model.Community;

@Service
public class CommunityService implements ICommunityService {


	@Autowired
	ICommunityRepository communityRepository;


	@Transactional
	public void insertArticle(Community community) {
		//		community.setWriteId(communityRepository.selectMaxArticleNo()+1);
		community.setReplyCnt(0);
		communityRepository.insertArticle(community);
	}

	@Override
	public List<Community> selectArticleListByCommunity(int page) {
		int start = (page-1)*9 + 1;
		return communityRepository.selectArticleListByCommunity(start, start+9); // 오라클은 BETWEEN a AND b에서 a와 b모두 포함하므로 9를 더함
	}
	@Transactional
	public Community selectArticle(int writeId) {
		//		communityRepository.updateReadCount(writeId);
		return communityRepository.selectArticle(writeId);
	}
	@Override
	public int selectTotalArticleCountByCommunity() {
		return communityRepository.selectTotalArticleCountByCommunity();
	}



	@Override
	public void updateArticle(Community community) {
		communityRepository.updateArticle(community);
	}
	@Override
	public void deleteArticleByWriteId(int writeId) {
		communityRepository.deleteArticleByWriteId(writeId);
	}


	@Override
	public int selectTotalArticleCountByKeyword(String keyword) {
		return communityRepository.selectTotalArticleCountByKeyword("%"+keyword+"%");
	}
	@Override
	public List<Community> searchListByContentKeyword(String keyword, int page) {
		int start = (page-1)*9 + 1;
		return communityRepository.searchListByContentKeyword("%"+keyword+"%", start, start+9); // 오라클은 BETWEEN a AND b에서 a와 b모두 포함하므로 9를 더함
	}
	@Override
	public int selectTotalArticleCountBymylist(String userId) {
		return communityRepository.selectTotalArticleCountBymylist(userId);
	}
	@Override
	public List<Community> searchListByContentmylist(String userId, int page) {
		int start = (page-1)*9 + 1;
		return communityRepository.searchListByContentmylist(userId, start, start+9); // 오라클은 BETWEEN a AND b에서 a와 b모두 포함하므로 9를 더함
	}
	
	@Transactional
	@Override
	public void updateReplyCnt(int writeId, int amount) {
		Community community = communityRepository.selectArticle(writeId);
		if (community != null) {
			community.setReplyCnt(community.getReplyCnt() + amount);
			communityRepository.updateReplyCnt(writeId, amount); // amount 파라미터 추가
		}
	}


}