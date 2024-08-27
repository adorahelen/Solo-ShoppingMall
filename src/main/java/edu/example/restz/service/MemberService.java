package edu.example.restz.service;

import edu.example.restz.dto.MemberDTO;
import edu.example.restz.entity.Member;
import edu.example.restz.exception.MemberException;
import edu.example.restz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDTO read (String mid, String mpw) {
        Optional<Member> result = memberRepository.findById(mid);

        // 데이터베이스에 존재하지 않는 경우 - MemberE 에 예외처리 발생
        Member member = result.orElseThrow(MemberException.BAD_CREDENTIALS::get);

        // mpw 가    데이터베이스의 값과 일치하지 않는 경우/ 배드 크레덴셜
        if (!passwordEncoder.matches(mpw, member.getMpw()) ) {
            throw MemberException.BAD_CREDENTIALS.get();
        }
        // 그렇지 않으면 념겨받은 엔티티를 DTO 객체로
        return new MemberDTO(member);

    }

}
