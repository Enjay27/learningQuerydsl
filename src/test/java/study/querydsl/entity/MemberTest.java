package study.querydsl.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        Member member3 = new Member("member3", 10, teamB);
        Member member4 = new Member("member4", 10, teamB);
        Member member5 = new Member("member5", 10, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        Stream<Member> memberAllStream = Stream.of(member1, member2, member3, member4, member5);
        assertThat(memberAllStream.map(Member::getUsername)
                .allMatch(m -> members.stream().map(Member::getUsername)
                        .anyMatch(e -> e.equals(m)))).isFalse();

        Stream<Member> memberStream = Stream.of(member1, member2, member3, member4);
        assertThat(memberStream.map(Member::getUsername)
                .allMatch(m -> members.stream().map(Member::getUsername)
                        .anyMatch(e -> e.equals(m)))).isTrue();
    }
}