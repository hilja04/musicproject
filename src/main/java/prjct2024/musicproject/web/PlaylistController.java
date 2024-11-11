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

import prjct2024.musicproject.domain.AppUser;
import prjct2024.musicproject.domain.AppUserRepository;
import prjct2024.musicproject.domain.Playlist;
import prjct2024.musicproject.domain.PlaylistRepository;

@Controller
public class PlaylistController {

    @Autowired
    private PlaylistRepository repository;

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

    @RequestMapping(value = { "/playlistlist" }, method = RequestMethod.GET)
    public String getPlaylist(Model model) {
        model.addAttribute("playlists", repository.findAll());
        String loggedInUsername = getLoggedInUsername();
        String loggedInRole = getLoggedInRole();
        model.addAttribute("loggedInUsername", loggedInUsername);
        model.addAttribute("loggedInRole", loggedInRole);
        return "playlistlist";
    }

    @RequestMapping(value = "/add")
    public String addPlaylist(Model model) {
        model.addAttribute("playlist", new Playlist());
        return "addplaylist";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String savePlaylist(@ModelAttribute Playlist playlist) {

        String username = getLoggedInUsername();
        AppUser user = urepository.findByUsername(username);

        if (user != null) {
            playlist.setUser(user);
        }

        repository.save(playlist);
        return "redirect:/playlistlist";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePlaylist(@PathVariable("id") Long playlistId, Model model) {
        Playlist playlist = repository.findById(playlistId).orElse(null);
        String loggedInUsername = getLoggedInUsername();
        if (playlist != null && playlist.getUser().getUsername().equals(loggedInUsername)) {
            repository.deleteById(playlistId);
        }
        return "redirect:/playlistlist";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewPlaylist(@PathVariable Long id, Model model) {
        Playlist playlist = repository.findById(id).orElse(null);
        int amount = playlist.getSongs().size();
        String loggedInUser = getLoggedInUsername();
        String loggedInRole = getLoggedInRole();
        if (playlist != null) {
            model.addAttribute("loggedInUsername", loggedInUser);
            model.addAttribute("playlist", playlist);
            model.addAttribute("songs", playlist.getSongs());
            model.addAttribute("amount", amount);
            model.addAttribute("loggedInRole", loggedInRole);

        }
        return "viewplaylist";
    }

    @RequestMapping(value = "/delete/admin/playlist/{id}", method = RequestMethod.GET)
    public String deletePlaylistForAdmin(@PathVariable("id") Long playlistId) {
        Playlist playlist = repository.findById(playlistId).orElse(null);

        String loggedInRole = getLoggedInRole();
        if ("ADMIN".equals(loggedInRole)) {

            if (playlist != null) {
                repository.deleteById(playlistId);
            }
        }

        return "redirect:/playlistlist";
    }
}
