package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //@Query(name = "Member.findByUsername")  // 이게 없어도 "메소드 이름으로 쿼리 생성"을 우선적으로 처리하기 때문에 생략이 가능하다
    List<Member> findByUsername(@Param("username") String username);  // m.username = :username 처럼 명확하게 JPQL을 작성했을 때 @Param으로 값을 넘겨야함

    @Query("select m from Member m where m.username = :username and m.age = :age")  // 애플리케이션 로딩 시점에 파싱을 진행함 -> 오류가 있으면 실행이 멈춤
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")  // dto로 반환
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

}
