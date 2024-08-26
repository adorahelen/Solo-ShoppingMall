package edu.example.restz.repository;

import edu.example.restz.entity.Member;
import edu.example.restz.exception.MemberException;
import edu.example.restz.exception.MemberTaskException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsert(){
        // USer 1 - 80 is just user
        // 80 - 100 is admin

        // Given - Member entity 객체 엔티티 생성
        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder().mid("user" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .mname("USER" + i)
                    .email("user" + i + "@aaa.com")
                    .role(i <= 80 ? "USER" : "ADMIN")
                    .build();

            // When - 엔티티 저장
            Member saveMember = memberRepository.save(member);

            // then - 기댓값, : saveTodo 는 NULL 이 아니며, 아이디는 1이다.
            assertNotNull(saveMember);

            if (i <= 80) assertEquals(saveMember.getRole(), "USER");
            else  assertEquals(saveMember.getRole(), "ADMIN");
            // user1 ~ user80 is USER role
            // user81 ~ user100 is ADMIN role
          //  assertEquals(i, saveMember.());


        });

    }

    @Test // select
    public void testFindById(){
        // given
        String mid = "user1";

        // when
        Optional<Member> foundMember = memberRepository.findById(mid);

        // Then
        assertNotNull(foundMember);
        assertEquals(mid,foundMember.get().getMid());

        log.info(foundMember + " : found member");
        log.info(foundMember.get().getMid() + " :  Mid found member");

        try {
            mid = "user123";
            foundMember = memberRepository.findById(mid);
            foundMember.orElseThrow(MemberException.NOT_FOUND::get);
        }catch (MemberTaskException e) {
            assertEquals(404, e.getCode());
            log.info("e : " + e);
        }
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate(){
        String mid = "user1";

        Optional<Member> foundMember = memberRepository.findById(mid);

        foundMember.orElseThrow(MemberException.NOT_FOUND::get);

        foundMember.get().changePassword(passwordEncoder.encode("2222"));
        foundMember.get().changeEmail("bbb@bbb.com");

        assertEquals("bbb@bbb.com", foundMember.get().getEmail());
    }


    @Test // Delete 테스트 - 트랜잭션 O
    @Transactional
    @Commit
    public void testDelete(){
        String mid = "user100";
        memberRepository.deleteById(mid);

        Optional<Member> foundMember = memberRepository.findById(mid);
        assertTrue( foundMember.isEmpty());
    }
}
