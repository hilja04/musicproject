package prjct2024.musicproject.domain;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PlaylistRepository extends CrudRepository<Playlist, Long> {
    List<Playlist> findByName(String name);

    List<Playlist> findByUserId(Long userId);
}
