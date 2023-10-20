package kr.co.sboard.repository;

import kr.co.sboard.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository <FileEntity, Integer> {


}
