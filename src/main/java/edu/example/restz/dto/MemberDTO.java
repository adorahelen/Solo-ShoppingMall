package edu.example.restz.dto;

import edu.example.restz.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String mid;
    private String mpw;
    private String mname;
    private String memail;
    private String role;
    private LocalDateTime joinDate;
    private LocalDateTime modifiedDate;

    public MemberDTO(Member member) {
        this.mid = member.getMid();
        this.mpw = member.getMpw();
        this.mname = member.getMname();
        this.memail = member.getEmail();
        this.role = member.getRole();
        this.joinDate = member.getJoinDate();
        this.modifiedDate = member.getModifiedDate();
    }

    // JWT 문자열 변환 ( 내용을 )
    public Map<String, Object> getPayload(){
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("mid", mid);
        payloadMap.put("mname", mname);
        payloadMap.put("memail", memail);
        payloadMap.put("role", role);
        return payloadMap;


    }
}
