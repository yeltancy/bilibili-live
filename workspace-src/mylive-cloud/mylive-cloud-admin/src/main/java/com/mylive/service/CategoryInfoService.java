package com.mylive.service;

import com.mylive.entity.po.CategoryInfo;
import com.mylive.entity.query.CategoryInfoQuery;
import com.mylive.entity.vo.PaginationResultVO;

import java.util.List;


/**
 *  业务接口
 */
public interface CategoryInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<CategoryInfo> findListByParam(CategoryInfoQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(CategoryInfoQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<CategoryInfo> findListByPage(CategoryInfoQuery param);

	/**
	 * 新增
	 */
	Integer add(CategoryInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<CategoryInfo> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<CategoryInfo> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(CategoryInfo bean,CategoryInfoQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(CategoryInfoQuery param);

	/**
	 * 根据CategoryId查询对象
	 */
	CategoryInfo getCategoryInfoByCategoryId(Integer categoryId);


	/**
	 * 根据CategoryId修改
	 */
	Integer updateCategoryInfoByCategoryId(CategoryInfo bean,Integer categoryId);


	/**
	 * 根据CategoryId删除
	 */
	Integer deleteCategoryInfoByCategoryId(Integer categoryId);


	/**
	 * 根据CategoryCode查询对象
	 */
	CategoryInfo getCategoryInfoByCategoryCode(String categoryCode);


	/**
	 * 根据CategoryCode修改
	 */
	Integer updateCategoryInfoByCategoryCode(CategoryInfo bean,String categoryCode);


	/**
	 * 根据CategoryCode删除
	 */
	Integer deleteCategoryInfoByCategoryCode(String categoryCode);

	/**
	 * 保存
	 */
	void saveCategoryInfo(CategoryInfo bean);

	/**
	 * 删除
	 */
	void delCategory(Integer categoryId);

	/**
	 * 修改排序
	 */
	void changeSort(Integer pCategoryId, String categoryIds);

	/**
	 * 获取所有分类列表
	 */
	List<CategoryInfo> getAllCategoryList();
}