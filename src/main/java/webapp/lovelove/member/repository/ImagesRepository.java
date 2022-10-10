package webapp.lovelove.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import webapp.lovelove.member.domain.memberprofiledomain.Images;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images save(Images images);
}
