package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.ReportRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRecordRepository extends JpaRepository<ReportRecord, Long> {
}
