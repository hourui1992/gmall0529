package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import com.atguigu.gmall.manager.vo.BaseAttrInfoAndValueVO;
import com.atguigu.gmall.manager.vo.BaseAttrValueVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/attr")
@Controller
public class AttrManagerController {

    @Reference
    BaseAttrInfoService baseAttrInfoService;
    /**
     * 去平台属性列表页面
     * 所有的去页面的请求，都加上html后缀
     * @return
     */
    @RequestMapping("/listPage.html")
    public String toAttrListPage(){

        return "attr/attrListPage";
    }

    @RequestMapping("/value/{id}")
    @ResponseBody
    public List<BaseAttrValue> getBaseAttrValueByInfoId(@PathVariable("id") Integer id){

        return baseAttrInfoService.getBaseAttrValueByAttrId(id);
    }
    @ResponseBody
    @RequestMapping("/updates")
    public String saveOrUpdateOrDeleteAttrInfoAndValue(@RequestBody BaseAttrInfoAndValueVO baseAttrInfoAndValueVO){
        BaseAttrInfo baseAttrInfo=new BaseAttrInfo();
        //将页面传过来的VO数据，转化为普通bean类型用于和数据库进行交互的数据类型（规范而已）
        BeanUtils.copyProperties(baseAttrInfoAndValueVO,baseAttrInfo);
        //BeanUtils的数据转换，不能自己将对象属性进行转换，故自己手动操作
        Integer infoId=baseAttrInfo.getId();
        List<BaseAttrValue> attrValues=new ArrayList<>();
        List<BaseAttrValueVO> avs = baseAttrInfoAndValueVO.getAttrValues();
        for (BaseAttrValueVO attrValue : avs) {
            BaseAttrValue baseAttrValue=new BaseAttrValue();
            BeanUtils.copyProperties(attrValue,baseAttrValue);
            baseAttrValue.setAttrId(infoId);
            attrValues.add(baseAttrValue);

        }
        baseAttrInfo.setAttrValues(attrValues);

        baseAttrInfoService.updatesAttrInfo(baseAttrInfo);

        return "ok";
    }

}
