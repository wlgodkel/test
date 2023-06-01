package egovframework.kf.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.kf.common.CommonUtil;
import egovframework.kf.common.SetParameter;
import egovframework.kf.data.ParameterVO;
import egovframework.kf.data.RestResultVO;
import egovframework.kf.service.BoardService;

/**
 * Class Name : SearchController.java
 * Description : 통합검색 조회를 위한 컨트롤러
 *
 * Modification Information
 *
 * 수정일                        수정자           수정내용
 * --------------------  -----------  ---------------------------------------
 * 2017년 12월  00일     김승희           최초 작성
 *
 * @since 2017년
 * @version V1.0
 * @see (c) Copyright (C) by KONANTECH All right reserved
 */
@Controller
public class SearchController {
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	/** BoardService */
	@Resource(name = "boardService")
	private BoardService boardService;	
	
	/** Parameter Setting */
	@Resource(name = "setParameter")
	private SetParameter setParameter;
	
	/** common util Setting */
	@Resource(name = "commonUtil")
	private CommonUtil commonUtil;
	
	
	@RequestMapping(value = "/search")
	public String search(@RequestParam Map<String, String> map, Model model) {
		
		logger.info(" ========================== >>searchTest");
		logger.info("map : " + map);

		// 파라미터 세팅
		ParameterVO paramVO = setParameter.setParameter(map);

		// 카테고리별 문서 호출
		setCategoryModel(model, paramVO);
		logger.debug(paramVO.toString());

		
		//파라미터
		model.addAttribute("params", paramVO);
		
		
		
		return "search/search";
	}
	
	
	/**
	 * 모델 세팅 부분을 분리
	 * 카테고리 :	 게시판 조회 
	 * @return Model
	 * 
	 * @throws Exception
	 */
	private Model setCategoryModel(Model model, ParameterVO paramVO) {
		RestResultVO result;
		int total = 0;
		
		// 카테고리 여부
		if(paramVO.getKwd().length() > 0) {
		
			// 게시판
			if (commonUtil.easyChkEqual("TOTAL,BOARD", paramVO.getCategory(), ",", false)) {
				try {
					result = boardService.BoardSearch(paramVO);
					total += result.getTotal();
					logger.info("boardRow : " + result.getRows());
					logger.info("boardList : " + result.getResult());
					logger.info("boardTotal : " + result.getTotal());
					model.addAttribute("boardRow", result.getRows());
					model.addAttribute("boardList", result.getResult());
					model.addAttribute("boardTotal", result.getTotal());	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		
		model.addAttribute("formatTotal", commonUtil.formatMoney(total));
		model.addAttribute("total", total);
		
		return model;
	}	
}
