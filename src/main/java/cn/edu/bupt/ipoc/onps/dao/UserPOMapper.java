package cn.edu.bupt.ipoc.onps.dao;

import cn.edu.bupt.ipoc.onps.model.po.UserPO;
import cn.edu.bupt.ipoc.onps.model.po.UserPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserPOMapper {
    int countByExample(UserPOExample example);

    int deleteByExample(UserPOExample example);

    int deleteByPrimaryKey(String id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    List<UserPO> selectByExample(UserPOExample example);

    UserPO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UserPO record, @Param("example") UserPOExample example);

    int updateByExample(@Param("record") UserPO record, @Param("example") UserPOExample example);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);
}