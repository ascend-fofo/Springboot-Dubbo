package com.kgc.cn.provider.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kgc.cn.common.dto.*;
import com.kgc.cn.common.enums.ErrorEnums;
import com.kgc.cn.common.excel.ExcelUtil;
import com.kgc.cn.common.exception.ServiceException;
import com.kgc.cn.common.service.GoodsService;
import com.kgc.cn.common.utils.ActiveMQUtils;
import com.kgc.cn.common.utils.PageUtils;
import com.kgc.cn.common.utils.RedisUtils;
import com.kgc.cn.common.vo.GoodsVo;
import com.kgc.cn.common.vo.OrderVo;
import com.kgc.cn.provider.mapper.FlashgoodsMapper;
import com.kgc.cn.provider.mapper.OrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;


import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private FlashgoodsMapper flashgoodsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ActiveMQUtils activeMQUtils;
    @Autowired
    private RedisUtils redisUtils;

    private String like_key = "goods_like";
    private String sum_like_key = "goods_like_sum";


    /**
     * 判断库存量是否大于0
     * @param goodsId
     * @return
     */
    @Override
    public boolean checkGoodsCount(long goodsId) {
        FlashgoodsExample flashgoodsExample = new FlashgoodsExample();
        flashgoodsExample.createCriteria().andGoodsidEqualTo(goodsId);
        List<Flashgoods> goodsList = flashgoodsMapper.selectByExample(flashgoodsExample);
        int count = goodsList.get(0).getGoodscount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int GoodsCount(long goodsId) {
        FlashgoodsExample flashgoodsExample = new FlashgoodsExample();
        flashgoodsExample.createCriteria().andGoodsidEqualTo(goodsId);
        List<Flashgoods> goodsList = flashgoodsMapper.selectByExample(flashgoodsExample);
        int count = goodsList.get(0).getGoodscount();
        return count;
    }

    /**
     * 展示商品列表
     * @param searchStr
     * @param pageNo
     * @return
     */
    @Override
    public List<Flashgoods> showGoodsList(String searchStr, int pageNo) {
        FlashgoodsExample flashgoodsExample = new FlashgoodsExample();
        PageUtils pageUtils = new PageUtils();
        pageUtils.setPageNo(pageNo);

        flashgoodsExample.setLimit(pageNo);
        flashgoodsExample.setOffset(pageUtils.getPageSize());

        flashgoodsExample.createCriteria().andGoodsnameLike('%'+searchStr+'%');
        List<Flashgoods> goodsList = flashgoodsMapper.selectByExample(flashgoodsExample);

        if (CollectionUtils.isNotEmpty(goodsList)) {
            return goodsList;
        }
        return null;
    }

    /**
     * 展示商品详情
     * @param goodsId
     * @return
     */
    @Override
    public Flashgoods showGoods(long goodsId) {
        return flashgoodsMapper.selectByPrimaryKey(goodsId);
    }

    /**
     * 展示商品列表2
     * @param searchStr
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Map showGoodsListMap(String searchStr, int pageNo, int pageSize) {
        FlashgoodsExample flashgoodsExample = new FlashgoodsExample();
        if (StringUtils.isNotEmpty(searchStr)) flashgoodsExample.createCriteria().andGoodsnameLike("%" + searchStr + "%");
        flashgoodsExample.setLimit(pageNo);
        flashgoodsExample.setOffset(pageSize);
        Map<String,Object> maps = Maps.newHashMap();
        List<Flashgoods> goodsList = flashgoodsMapper.selectByExample(flashgoodsExample);
        long count = flashgoodsMapper.countByExample(flashgoodsExample);

        List<GoodsVo> goodsVoList = Lists.newArrayList();
        goodsList.forEach(goodsDto ->{
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goodsDto,goodsVo);
            goodsVoList.add(goodsVo);
        });
        maps.put("currentList",goodsVoList);
        maps.put("count",count);

        return maps;
    }

    @Override
    public boolean timeToBuy(long gId, LoginUser loginUser) {
        String nameSpace = "TIME_PURCHASE";
        //判断库存
        Flashgoods flashgoods = flashgoodsMapper.selectByPrimaryKey(gId);
        if (flashgoods.getGoodscount() > 0){
            //todo 添加订单
            Order ODto = new Order();
            //String orderId = loginUser.getId() + "" + UUID.randomUUID();
            //ODto.setOrderid(orderId);
            ODto.setPhone(loginUser.getName());
            ODto.setOrderid((long)loginUser.getId());
            orderMapper.insertSelective(ODto);
            activeMQUtils.sendQueueMsg("order",ODto);
            //redisUtils.set(nameSpace + orderId,ODto);
            return true;
        }
        //todo 抢购失败
        return false;
    }

    @Override
    @JmsListener(destination = "order")
    public void creatOrders(OrderVo orderVo) {
        Order orders = new Order();
        BeanUtils.copyProperties(orderVo,orders);
        orderMapper.insertSelective(orders);
    }

    /**
     * 点赞
     * @param userId
     * @param gId
     * @param var
     */
    @Override
    @Async
    public void like(int userId, long gId, Integer var){
        if (islike(userId,gId) && var ==+1) {
            throw new ServiceException(ErrorEnums.EMPTY_PARAM);
        }
        redisUtils.set(like_key + ":" + gId + ":" + userId,var);
        //计算总数
        if (var == +1) {
            redisUtils.incr(sum_like_key+ ":" + gId + ":" + userId,1);
        } else {
            redisUtils.decr(sum_like_key+ ":" + gId + ":" + userId,1);
        }
    }


    /**
     * 是否点赞
     * @param userId
     * @param gId
     * @return
     */
    public boolean islike(int userId, long gId){
        boolean likeInit = false;
        List<Object> redisVar = redisUtils.lGet(like_key + ":" + gId + ":" + userId, 0,-1);
        List<Integer> intVar = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(redisVar)) {
            redisVar.forEach(v ->{
                intVar.add(Integer.valueOf((Integer) v));
            });
            if (intVar.stream().mapToInt(v -> v).sum() > 0) {
                likeInit = true;
            }
        }
        return likeInit;
    }

    @Override
    @Async
    public List<Flashgoods> exportGoodsExcel(HttpServletResponse response) {
        //mybatis plus条件构造器,如果不需要条件,那么直接全查询
        FlashgoodsExample flashgoodsExample = new FlashgoodsExample();
        //flashgoodsExample.createCriteria();
        List<Flashgoods> goodsList = flashgoodsMapper.selectByExample(flashgoodsExample);
        List<GoodsExportDTO> list = new ArrayList<>();
        GoodsExportDTO goodsExportDTO = null;
        for (int i = 0; i < goodsList.size(); i++) {
            goodsExportDTO = new GoodsExportDTO();
            //获取数据库的用户集合
            Flashgoods flashgoods = goodsList.get(i);
            //对集合依次写入表格中
            goodsExportDTO.setGoodsid(flashgoods.getGoodsid());
            goodsExportDTO.setGoodscolor(flashgoods.getGoodscolor());
            goodsExportDTO.setGoodscount(flashgoods.getGoodscount());
            goodsExportDTO.setGoodsdescription(flashgoods.getGoodsdescription());
            goodsExportDTO.setGoodsname(flashgoods.getGoodsname());

            //比如这里可以对积否则创建时间分不导出,不赋值就行了(UserSheet如果写了,那就要和它保持一致即可)
            list.add(goodsExportDTO);
        }
        //文件名称
        String fileName = "goodsExport";
        //表格sheet名称(用过excel应该知道什么是这个名称)
        String sheetName = "goods";
        ExcelUtil.writeExcel(response, list, fileName, sheetName, new GoodsExportDTO());
        return null;
    }

}
