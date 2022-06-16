package com.softland.api.apitd.repository;

import com.softland.api.apitd.domain.ApiLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogsRepository extends JpaRepository<ApiLogs, Long> {
    @Query(value = "select * from ApiLogs order by fechaactualizacion desc", nativeQuery = true)
    Page<ApiLogs> getApiLogsByLatest(Pageable pageable);
}
