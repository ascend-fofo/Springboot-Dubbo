package com.kgc.cn.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kgc.cn.common.dto.Flashgoods;
import com.kgc.cn.common.dto.LoginUser;
import com.kgc.cn.common.enums.ErrorEnums;
import com.kgc.cn.common.exception.ServiceException;
import com.kgc.cn.common.service.GoodsService;
import com.kgc.cn.common.service.OrderService;
import com.kgc.cn.common.utils.PageUtils;
import com.kgc.cn.common.utils.RedisUtils;
import com.kgc.cn.common.vo.GoodsVo;
import com.kgc.cn.common.vo.OrderVo;
import com.kgc.cn.consumer.conf.CurrentUser;
import com.kgc.cn.consumer.conf.LoginRequired;
import com.kgc.cn.consumer.utils.ActiveMQUtils;
import com.kgc.cn.consumer.utils.ReturnResult;
import com.kgc.cn.consumer.utils.ReturnResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Log4j
@RequestMapping(value = "/order")
public class OrderController {

    @Reference
    private GoodsService goodsService;
    @Reference
    private RedisUtils redisUtils;
    @Reference
    private OrderService orderService;
    @Autowired
    private ActiveMQUtils activeMQUtils;


    @LoginRequired
    @GetMapping(value = "/insert")
    public ReturnResult insert( @CurrentUser LoginUser loginUser, @Validated OrderVo orderVo) {
        if (!ObjectUtils.isEmpty(orderVo)) {
            if (goodsService.checkGoodsCount(orderVo.getGoodId())) {
                if (orderService.add(loginUser, orderVo.getGoodId())) {
                    redisUtils.set(String.valueOf(orderVo.getOrderId()), orderVo);
                    //redisUtils.expire(String.valueOf(orderVo.getOrderId()), 20);
                    redisUtils.lock(String.valueOf(orderVo.getGoodId()),orderVo,200);
                    redisUtils.hasKey(String.valueOf(orderVo.getOrderId()));
                    return ReturnResultUtils.returnSuccess();
                } else {
                    return ReturnResultUtils.returnFail(12, "超时抢购");
                   // redisUtils.delLock(String.valueOf(orderVo.getGoodId()));
                }
                //redisUtils.delLock(String.valueOf(orderVo.getGoodId()));
            } else {
                return ReturnResultUtils.returnFail(11, "商品已抢购一空");

            }
        }
        throw new ServiceException(ErrorEnums.EMPTY_PARAM);
    }

    @ApiOperation(value = "返回商品列表")
    @GetMapping(value = "/showGoodsList")
    public PageUtils<List<Flashgoods>> showGoodsList(@RequestParam(name = "searchStr",required = false)String searchStr, @RequestParam(name = "pageNo",required = false) int pageNo) {
        List<Flashgoods> goodsList = goodsService.showGoodsList(searchStr, pageNo);
        if (!ObjectUtils.isEmpty(goodsList)){
            PageUtils listPageUtils=new PageUtils();
            listPageUtils.setCurrentList(goodsList);
            return listPageUtils;
        }
        return null;
    }

    @ApiOperation(value = "返回商品列表2")
    @GetMapping(value = "/showGoodsList2")
    public PageUtils<List<GoodsVo>> showGoodsList2(@RequestParam(name = "searchStr",required = false)String searchStr,
                                                   @RequestParam(name = "pageNo",required = true) int pageNo,
                                                   @RequestParam(name = "pageSize",required = true) int pageSize) {
        PageUtils pageUtils = new PageUtils();
        pageUtils.setPageNo(pageNo);
        Map<String,Object> maps = goodsService.showGoodsListMap(searchStr,pageUtils.getPageNo(),pageSize);
        pageUtils.setCurrentList((List<GoodsVo>)maps.get("currentList"));
        pageUtils.setTotalCount((long)maps.get("count"));
        pageUtils.setCurrentPage(pageNo);
        return pageUtils;
    }

    @ApiOperation(value = "查询商品详情")
    @GetMapping(value = "/showGoods")
    public Flashgoods showGoods(@RequestParam long goodsId){
        return goodsService.showGoods(goodsId);
    }

    public boolean checkFreq(String gId){
        return redisUtils.checkFreq(gId,5,30) ;
                //&& goodsService.GoodsCount(gId) > 0 ;
    }

    /*@LoginRequired
    @GetMapping(value = "/timeToBuy")
    public ReturnResult timeToBuy( @CurrentUser LoginUser loginUser, long gId) {
        if (redisUtils.hasKey(String.valueOf(gId))){
            goodsService.timeToBuy(gId,loginUser);
            return ReturnResultUtils.returnSuccess();
        }
        return ReturnResultUtils.returnFail(111, "抢购失败");
    }*/

    /*@LoginRequired//用这个注解表示用这方法之前需要登录
    @GetMapping(value = "/insert22")
    public ReturnResult LimitedTimePurchase(@CurrentUser LoginUser loginUser, @Validated OrderVo order){//@CurrentUser代表是谁来秒杀
        //通过gId查询商品，把库存放
        redisUtils.set("stock",goodsService.GoodsCount(order.getGoodId()));
        //判断库存是否大于0
        int stock = (int)redisUtils.get("stock");
        boolean flag = redisUtils.checkFreq("a",5,30);
        if (stock>0 && flag){
            //生成订单
            redisUtils.set("stock",stock-1);//生成订单成功redis数据-1
            String jsonStr = JSONObject.toJSONString(order);
            activeMQUtils.sendQueueMsg("order", order);
            redisUtils.lock(String.valueOf(order.getGoodId()),jsonStr,200);
//                redisUtils.expire(order.getGoodId(),200);
            String orders = (String) redisUtils.get(String.valueOf(order.getGoodId());
            //判断订单是否还在
            if (!StringUtils.isEmpty(orders)) {
                return ReturnResultUtils.returnSuccess(666,"抢购成功，请及时付款",order);
            }else {
                return ReturnResultUtils.returnFail(2,"订单超时");
                redisUtils.delLock(String.valueOf(order.getGoodId()));
            }
        }
        return ReturnResultUtils.returnFail(4,"库存不足");
    }*/

    @ApiOperation("点赞")
    @LoginRequired
    @GetMapping(value = "/tolike")
    public ReturnResult like(@CurrentUser LoginUser userLogin, @RequestParam(name = "gId") long gId, @RequestParam(name = "islike") int islike) {
        if (redisUtils.checkFreq("like", 1, 5)) {
            goodsService.like(userLogin.getId(), gId, islike);
            return ReturnResultUtils.returnSuccess();
        }
        return ReturnResultUtils.returnFail(555, "对不起，服务器繁忙！");
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void writeExcel(HttpServletResponse response) throws IOException {
        /**
         * 注意我这里直接使用mybatis plus的方法
         * 查询数据库所有用户信息集合
         */
        goodsService.exportGoodsExcel(response);
        log.info("恭喜,导出excel成功!");
    }

}
