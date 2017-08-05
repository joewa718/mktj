package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.TaskInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskInfoRepository extends CrudRepository<TaskInfo, Long> {

    List<TaskInfo> findAll();

    TaskInfo findOne(Long id);

    TaskInfo save(TaskInfo taskInfo);

    void delete(Long id);

    TaskInfo findBySignature(String signature);

    @Query("select status from TaskInfo t where t.updateTime > CURRENT_DATE")
    Integer getStatusByUpdateTime();

    @Query("select count(*) from TaskInfo t where t.status != 100")
    Integer isTaskProcessing();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update TaskInfo t set t.status = ?1 where t.updateTime > CURRENT_DATE")
    int updateStatus(int status);

}
