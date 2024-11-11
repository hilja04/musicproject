package prjct2024.musicproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import prjct2024.musicproject.domain.AppUser;
import prjct2024.musicproject.domain.AppUserRepository;
import prjct2024.musicproject.domain.Playlist;
import prjct2024.musicproject.domain.PlaylistRepository;
import prjct2024.musicproject.domain.SongRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MusicprojectApplication {
    private static final Logger log = LoggerFactory.getLogger(MusicprojectApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MusicprojectApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner demo(PlaylistRepository prepository, SongRepository srepository,
            AppUserRepository appUserRepository) {
        return (args) -> {
            log.info("Saving playlists");

            Playlist playlist1 = new Playlist(null, "Pop Songs", "20/10/2024", null);
            Playlist playlist2 = new Playlist(null, "Suomi Biisit", "26/10/2024", null);

            prepository.save(playlist1);
            prepository.save(playlist2);

            AppUser user1 = new AppUser("user", passwordEncoder().encode("password"), "USER");
            AppUser user2 = new AppUser("admin", passwordEncoder().encode("aer3quoh"), "ADMIN");
            appUserRepository.save(user1);
            appUserRepository.save(user2);

            log.info("Fetching all playlists");
            for (Playlist playlist : prepository.findAll()) {
                log.info(playlist.toString());
            }
        };
    }
}