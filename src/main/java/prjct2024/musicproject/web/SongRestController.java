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

import prjct2024.musicproject.domain.Song;
import prjct2024.musicproject.domain.SongRepository;

@CrossOrigin
@Controller
public class SongRestController {
    @Autowired
    private SongRepository repository;

    @RequestMapping(value = "/songs", method = RequestMethod.GET)
    public @ResponseBody List<Song> getSongRest() {
        return (List<Song>) repository.findAll();
    }

    @RequestMapping(value = "/songs/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Song> findSongRest(@PathVariable("id") Long Id) {
        return repository.findById(Id);
    }

    @RequestMapping(value = "/songs", method = RequestMethod.POST)
    public @ResponseBody Song saveSongRest(@RequestBody Song song) {
        return repository.save(song);
    }

}
