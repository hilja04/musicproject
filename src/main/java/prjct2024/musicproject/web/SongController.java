package prjct2024.musicproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import prjct2024.musicproject.domain.Playlist;
import prjct2024.musicproject.domain.PlaylistRepository;
import prjct2024.musicproject.domain.Song;
import prjct2024.musicproject.domain.SongRepository;
import prjct2024.musicproject.domain.AppUser;
import prjct2024.musicproject.domain.AppUserRepository;

@Controller
public class SongController {

    @Autowired
    private SongRepository srepository;
    @Autowired
    private PlaylistRepository prepository;

    @Autowired
    private AppUserRepository urepository;

    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); 
        } else {
            return principal.toString(); 
        }
    }

    private String getLoggedInRole() {
        String loggedInUsername = getLoggedInUsername();
        AppUser user = urepository.findByUsername(loggedInUsername);
        if (user != null) {
            return user.getRole(); 
        }
        return "USER"; 
    }

    @RequestMapping(value = { "/songlist" }, method = RequestMethod.GET)
    public String getaSonglist(Model model) {
        String loggedInRole = getLoggedInRole(); 
        model.addAttribute("loggedInRole", loggedInRole);
        model.addAttribute("songs", srepository.findAll());
        model.addAttribute("playlists", prepository.findAll());
        return "songlist";
    }

    @RequestMapping(value = "/addsong/{id}", method = RequestMethod.GET)
    public String addSong(@PathVariable Long id, Model model) {
        Playlist playlist = prepository.findById(id).orElse(null);
        String loggedInUsername = getLoggedInUsername();

        if (playlist != null && playlist.getUser().getUsername().equals(loggedInUsername)) {
            model.addAttribute("playlistId", id); 
            model.addAttribute("song", new Song()); 
            return "addsong"; 
        } else {
            return "redirect:/view/" + id; 
        }
    }

    @RequestMapping(value = "/savesong", method = RequestMethod.POST)
    public String saveSong(@ModelAttribute Song song, @RequestParam Long playlistId) {
        Playlist playlist = prepository.findById(playlistId).orElse(null);
        if (playlist != null) {
            song.getPlaylists().add(playlist);
            playlist.getSongs().add(song);
        }
        srepository.save(song);
        prepository.save(playlist); 
        return "redirect:/view/" + playlistId;
    }

    @RequestMapping(value = "/delete/song/{songId}", method = RequestMethod.GET)
    public String deleteSongFromPlaylist(@PathVariable Long songId, @RequestParam Long playlistId) {
        Playlist playlist = prepository.findById(playlistId).orElse(null);
        Song song = srepository.findById(songId).orElse(null);

        if (playlist != null && song != null) {
            String loggedInUsername = getLoggedInUsername();
            if (playlist.getUser().getUsername().equals(loggedInUsername)) {

                playlist.getSongs().remove(song);
                
                song.getPlaylists().remove(playlist);

               
                prepository.save(playlist);
                srepository.save(song);
            }
        }

        return "redirect:/view/" + playlistId;
    }

    @RequestMapping(value = "/addsongtoplaylist/{songId}", method = RequestMethod.GET)
    public String addSongToPlaylist(@PathVariable Long songId, Model model) {
        String loggedInUsername = getLoggedInUsername();

        
        AppUser user = urepository.findByUsername(loggedInUsername);
      
        Song song = srepository.findById(songId).orElse(null);

        if (user != null && song != null) {
           
            model.addAttribute("playlists", prepository.findByUserId(user.getId()));
            model.addAttribute("song", song); 
        }

        return "addsongtoplaylist";
    }

    @RequestMapping(value = "/savesongtoplaylist", method = RequestMethod.POST)
    public String saveSongToPlaylist(@RequestParam Long songId, @RequestParam Long playlistId) {
        Song song = srepository.findById(songId).orElse(null);
        Playlist playlist = prepository.findById(playlistId).orElse(null);

        if (song != null && playlist != null) {
            playlist.getSongs().add(song); 
            song.getPlaylists().add(playlist); 

            prepository.save(playlist); 
            srepository.save(song); 
        }

        return "redirect:/songlist";
    }
    @RequestMapping(value = "/delete/admin/song/{id}", method = RequestMethod.GET)
public String deleteSongForAdmin(@PathVariable("id") Long songId, @RequestParam Long playlistId) {
    Song song = srepository.findById(songId).orElse(null);
    Playlist playlist = prepository.findById(playlistId).orElse(null);

   
    String loggedInRole = getLoggedInRole(); 
    if ("ADMIN".equals(loggedInRole)) {
        if (song != null && playlist != null) {
           
            playlist.getSongs().remove(song);
            
            song.getPlaylists().remove(playlist);

          
            prepository.save(playlist);
            srepository.delete(song);
        }
    }

    return "redirect:/view/" + playlistId;
}
    

}
