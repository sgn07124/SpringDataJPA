package study.data_jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    List<Member> findListByUsername(String name);  // 컬렉션
    Member findMemberByUsername(String name);  // 단건
    Optional<Member> findOptionalByUsername(String username);  // 단건 Optional

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);  // 페이징

    //Slice<Member> findByAge(int age, Pageable pageable);  // 페이징 슬라이스

    //@Modifying  // 이게 없으면 result list나 single result 같은걸 호출한다.
    @Modifying(clearAutomatically = true)  // em.clear()을 자동으로 해줌
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})  // JPQL 없이도 객체 그래프를 한 번에 엮어서 성능 최적화를 해서 가지고 온다.
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();  // @EntityGraph와 JPQL을 같이 사용 가능

    //@EntityGraph(attributePaths = {"team"})
    //List<Member> findEntityGraphByUsername(@Param("username") String username);  // 메서드 이름으로 쿼리 생성할 때도 사용 가능

    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

}
