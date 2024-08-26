package edu.example.restz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tbl_member")
@EntityListeners(value = { AuditingEntityListener.class })
// 프로젝트 내에서 시큐리티 대상이 되는 사용자들의 정보
// - 등록 시간과 수정 시간 : JPA 의 엔티티 객체가 저장돠거나 수정될 경우 이에 대한 시간이 자동으로 기록된다.,
// - 사용자 등급 : 사용자가 일반 회원인지, 관리자인지 알 수 있도록 한다. ( ROLE)

public class Member {       //엔티티 객체 insert/update 시 자동으로 시간 갱신
    @Id
    private String mid;
    private String mpw;
    private String mname;
    private String email;
    private String role;

    @CreatedDate            //등록 일시 자동 저장
    private LocalDateTime joinDate;

    @LastModifiedDate       //수정 일시 자동 저장
    private LocalDateTime modifiedDate;

    public void changePassword(String mpw) {
        this.mpw = mpw;
    }

    public void changeName(String mname) {
        this.mname = mname;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeRole(String role) {
        this.role = role;
    }
}
