package edu.example.restz.repository;

import edu.example.restz.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
                                                    // 타켓 : 기본키 설정한거(맴버는 스트링)
}
