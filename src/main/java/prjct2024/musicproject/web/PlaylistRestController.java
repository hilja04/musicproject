package prjct2024.musicproject.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import prjct2024.musicproject.domain.Playlist;
import prjct2024.musicproject.domain.PlaylistRepository;

@CrossOrigin
@Controller
public class PlaylistRestController {

    @Autowired
    private PlaylistRepository prepository;

    @RequestMapping(value = "/playlists", method = RequestMethod.GET)
    public @ResponseBody List<Playlist> playListRest() {
        return (List<Playlist>) prepository.findAll();
    }

    @RequestMapping(value = "/playlist/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Playlist> findPlaylistRest(@PathVariable("id") Long playlistId) {
        return prepository.findById(playlistId);
    }

    @RequestMapping(value = "/playlists", method = RequestMethod.POST)
    public @ResponseBody Playlist savePlaylistRest(@RequestBody Playlist playlist) {
        return prepository.save(playlist);
    }
}
