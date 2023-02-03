package com.example.enchere.controller;

import com.example.enchere.dto.EnchereDTO;
import com.example.enchere.entity.Authenticable;
import com.example.enchere.entity.Enchere;
import com.example.enchere.entity.Membre;
import com.example.enchere.exception.EntityNotFoundException;
import com.example.enchere.exception.HttpException;
import com.example.enchere.repository.CategorieRepository;
import com.example.enchere.service.EnchereService;
import com.example.enchere.service.TokenService;
import com.example.enchere.value_object.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
public class EnchereController {

    private EnchereService service;
    private TokenService tokenService;

    private CategorieRepository categorieRepository;

    @PostMapping("/membres/{id}/encheres")
    public ResponseEntity<Message> save(@RequestBody Enchere enchere, HttpServletRequest request, @PathVariable Long id){
        if(!categorieRepository.findById(enchere.getCategorie().getId()).isPresent()){
            throw new HttpException(HttpStatus.BAD_REQUEST, "Category is not defined");
        }
        Authenticable authenticable = tokenService.validate(request, id);
        enchere.setMembre(new Membre(authenticable.getId()));
        service.save(enchere);
        return new ResponseEntity<>(new Message("Successfully saved"), HttpStatus.CREATED);
    }

    @GetMapping("/encheres")
    public List<EnchereDTO> listAllWithCurrentPrice(@RequestParam(required = false) String desc,
                                                    @RequestParam(required = false) Double prixMin, @RequestParam(required = false) Double prixMax) {
        return service.findAllWithCurrentPrice2(desc, prixMin, prixMax);
    }

    @GetMapping("membres/{id}/encheres")
    public List<EnchereDTO> findAllByOwnerId(@PathVariable Long id, HttpServletRequest request){
        Authenticable authenticable = tokenService.validate(request, id);
        return service.findAllByOwnerId(id);
    }

    @GetMapping("/encheres/{id}")
    public EnchereDTO one(@PathVariable Long id){
        EnchereDTO enchereDTO =  service.findById(id);

        return enchereDTO;
    }

    @Autowired
    public void setCategorieRepository(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setService(EnchereService service) {
        this.service = service;
    }
}
