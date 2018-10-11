package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id) {
        return baseAttrInfoMapper.selectList(new QueryWrapper<BaseAttrInfo>().eq("catalog3_id",catalog3Id));
    }

    @Override
    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer baseAttrInfoId) {
        return baseAttrValueMapper.selectList(new QueryWrapper<BaseAttrValue>().eq("attr_id",baseAttrInfoId));
    }

    @Override
    @Transactional
    public void updatesAttrInfo(BaseAttrInfo baseAttrInfo) {
        //update
        baseAttrInfoMapper.updateById(baseAttrInfo);
        //update values
        List<BaseAttrValue> attrValues = baseAttrInfo.getAttrValues();
        List<Integer> ids =new ArrayList<>();
        //在插入之前进行删除，不然会出现新插入的数据被删除的情况
        for (BaseAttrValue attrValue : attrValues) {
            if(attrValue.getId()!=null){
                ids.add(attrValue.getId());
            }
        }
        baseAttrValueMapper.delete(
                new QueryWrapper<BaseAttrValue>()
                        .eq("attr_Id",baseAttrInfo.getId())
                        .notIn("id",ids));
        for (BaseAttrValue attrValue : attrValues) {
            if(attrValue.getId()!=null){

                baseAttrValueMapper.updateById(attrValue);
            }else{
                baseAttrValueMapper.insert(attrValue);
            }

        }


    }
}
