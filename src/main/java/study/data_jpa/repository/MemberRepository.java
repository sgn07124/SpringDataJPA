package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //@Query(name = "Member.findByUsername")  // 이게 없어도 "메소드 이름으로 쿼리 생성"을 우선적으로 처리하기 때문에 생략이 가능하다
    List<Member> findByUsername(@Param("username") String username);  // m.username = :username 처럼 명확하게 JPQL을 작성했을 때 @Param으로 값을 넘겨야함
}
