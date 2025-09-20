package com.mylive.service.impl;

import com.mylive.api.consumer.WebClient;
import com.mylive.component.RedisComponent;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.enums.PageSize;
import com.mylive.entity.po.CategoryInfo;
import com.mylive.entity.query.CategoryInfoQuery;
import com.mylive.entity.query.SimplePage;
import com.mylive.entity.query.VideoInfoQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.exception.BusinessException;
import com.mylive.mappers.CategoryInfoMapper;
import com.mylive.service.CategoryInfoService;
import com.mylive.utils.StringTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 业务接口实现
 */
@Service("categoryInfoService")
public class  CategoryInfoServiceImpl implements CategoryInfoService {

    @Resource
    private CategoryInfoMapper<CategoryInfo, CategoryInfoQuery> categoryInfoMapper;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private WebClient webClient;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<CategoryInfo> findListByParam(CategoryInfoQuery param) {
        List<CategoryInfo> categoryInfoList = this.categoryInfoMapper.selectList(param);
        if (param.getConvert2Tree() != null && param.getConvert2Tree()) {
            categoryInfoList = convertLine2Tree(categoryInfoList, Constants.ZERO);
        }
        return categoryInfoList;
    }

    /**
     * 将平行结构的数据转换为树形结构
     */
    private List<CategoryInfo> convertLine2Tree(List<CategoryInfo> dataList, Integer pid) {
        List<CategoryInfo> children = new ArrayList<>();
        for (CategoryInfo m : dataList) {
            if (m.getCategoryId() != null && m.getpCategoryId() != null && m.getpCategoryId().equals(pid)) {
                m.setChildren(convertLine2Tree(dataList, m.getCategoryId()));
                children.add(m);
            }
        }
        return children;
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(CategoryInfoQuery param) {
        return this.categoryInfoMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<CategoryInfo> findListByPage(CategoryInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<CategoryInfo> list = this.findListByParam(param);
        PaginationResultVO<CategoryInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(CategoryInfo bean) {
        return this.categoryInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<CategoryInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.categoryInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<CategoryInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.categoryInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(CategoryInfo bean, CategoryInfoQuery param) {
        StringTools.checkParam(param);
        return this.categoryInfoMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(CategoryInfoQuery param) {
        StringTools.checkParam(param);
        return this.categoryInfoMapper.deleteByParam(param);
    }

    /**
     * 根据CategoryId获取对象
     */
    @Override
    public CategoryInfo getCategoryInfoByCategoryId(Integer categoryId) {
        return this.categoryInfoMapper.selectByCategoryId(categoryId);
    }

    /**
     * 根据CategoryId修改
     */
    @Override
    public Integer updateCategoryInfoByCategoryId(CategoryInfo bean, Integer categoryId) {
        return this.categoryInfoMapper.updateByCategoryId(bean, categoryId);
    }

    /**
     * 根据CategoryId删除
     */
    @Override
    public Integer deleteCategoryInfoByCategoryId(Integer categoryId) {
        return this.categoryInfoMapper.deleteByCategoryId(categoryId);
    }

    /**
     * 根据CategoryCode获取对象
     */
    @Override
    public CategoryInfo getCategoryInfoByCategoryCode(String categoryCode) {
        return this.categoryInfoMapper.selectByCategoryCode(categoryCode);
    }

    /**
     * 根据CategoryCode修改
     */
    @Override
    public Integer updateCategoryInfoByCategoryCode(CategoryInfo bean, String categoryCode) {
        return this.categoryInfoMapper.updateByCategoryCode(bean, categoryCode);
    }

    /**
     * 根据CategoryCode删除
     */
    @Override
    public Integer deleteCategoryInfoByCategoryCode(String categoryCode) {
        return this.categoryInfoMapper.deleteByCategoryCode(categoryCode);
    }

    /**
     * 保存
     */
    @Override
    public void saveCategoryInfo(CategoryInfo bean) {
        CategoryInfo daBean = categoryInfoMapper.selectByCategoryCode(bean.getCategoryCode());
        if (bean.getCategoryId() == null && daBean != null ||
                bean.getCategoryId() != null && daBean != null && daBean.getCategoryId().intValue() != bean.getCategoryId().intValue()) {
            throw new BusinessException("分类编码已存在");
        }
        if (bean.getCategoryId() == null) {
            Integer maxSort = categoryInfoMapper.selectMaxSort(bean.getpCategoryId());
            bean.setSort(maxSort + 1);
            this.add(bean);
        } else {
            this.updateCategoryInfoByCategoryId(bean, bean.getCategoryId());
        }
    }

    /**
     * 删除
     */
    @Override
    public void delCategory(Integer categoryId) {
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setCategoryIdOrPCategoryId(categoryId);
        //TODO web模块提供分类下的视频数量
        Integer count = webClient.getVideoCount(videoInfoQuery);
//        Integer count = videoInfoService.findCountByParam(videoInfoQuery);
        if(count>0){
            throw new BusinessException("分类下有视频信息，无法删除");
        }
        CategoryInfoQuery categoryInfoQuery = new CategoryInfoQuery();
        categoryInfoQuery.setCategoryIdOrPCategoryId(categoryId);
        categoryInfoMapper.deleteByParam(categoryInfoQuery);
        //刷新缓存
        save2Redis();
    }

    /**
     * 修改排序
     */
    @Override
    public void changeSort(Integer pCategoryId, String categoryIds) {
        String[] categoryIdArray = categoryIds.split(",");
        List<CategoryInfo> categoryInfoList = new ArrayList<>();
        Integer sort = 0;
        for (String categoryId : categoryIdArray) {
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setCategoryId(Integer.valueOf(categoryId));
            categoryInfo.setpCategoryId(pCategoryId);
            categoryInfo.setSort(++sort);
            categoryInfoList.add(categoryInfo);
            sort++;
        }
        categoryInfoMapper.updateSortBatch(categoryInfoList);
        //刷新缓存
        save2Redis();
    }

    /**
     * 刷新缓存到redis
     */
    private void save2Redis() {
        CategoryInfoQuery query = new CategoryInfoQuery();
        query.setOrderBy("sort asc");
        query.setConvert2Tree(true);
        List<CategoryInfo> categoryInfoList = findListByParam(query);
        redisComponent.saveCategoryList(categoryInfoList);
    }

    /**
     * 获取所有分类列表
     */
    @Override
    public List<CategoryInfo> getAllCategoryList() {
        List<CategoryInfo> categoryInfoList = redisComponent.getCategoryList();
        if(categoryInfoList == null || categoryInfoList.isEmpty()){
            save2Redis();
        }
        return redisComponent.getCategoryList();
    }
}