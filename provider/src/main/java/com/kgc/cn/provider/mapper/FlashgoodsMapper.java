package com.kgc.cn.provider.mapper;

import java.util.List;

import com.kgc.cn.common.dto.Flashgoods;
import com.kgc.cn.common.dto.FlashgoodsExample;
import org.apache.ibatis.annotations.Param;

public interface FlashgoodsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    long countByExample(FlashgoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int deleteByExample(FlashgoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long goodsid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int insert(Flashgoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int insertSelective(Flashgoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    List<Flashgoods> selectByExample(FlashgoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    Flashgoods selectByPrimaryKey(Long goodsid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Flashgoods record, @Param("example") FlashgoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Flashgoods record, @Param("example") FlashgoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Flashgoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flashgoods
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Flashgoods record);

    int delGoodsNum(int num,long goodsId);
}