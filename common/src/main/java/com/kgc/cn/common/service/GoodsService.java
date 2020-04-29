package com.kgc.cn.common.service;

import com.kgc.cn.common.dto.Flashgoods;
import com.kgc.cn.common.dto.LoginUser;
import com.kgc.cn.common.vo.OrderVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface GoodsService {
    //查看商品的库存量
    boolean checkGoodsCount(long goodsId);

    int GoodsCount(long goodsId);

    //展示商品列表,实现模糊搜索,分页显示
    List<Flashgoods> showGoodsList(String searchStr, int pageNo);

    //查询商品详情
    Flashgoods showGoods(long goodsId);

    Map showGoodsListMap(String searchStr,int pageNo,int pageSize);

    boolean timeToBuy(long gId, LoginUser loginUser);

    void creatOrders(OrderVo orderVo);

    void like(int userId, long gId, Integer var);

    /**
     * 导出 Excel（一个 sheet）
     * @param response
     * @return
     */
    List<Flashgoods> exportGoodsExcel(HttpServletResponse response);

}
