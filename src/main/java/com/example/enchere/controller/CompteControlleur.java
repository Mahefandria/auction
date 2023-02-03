package com.example.enchere.controller;

import com.example.enchere.entity.Authenticable;
import com.example.enchere.entity.Compte;
import com.example.enchere.entity.Membre;
import com.example.enchere.entity.Recharge;
import com.example.enchere.repository.RechargeRepository;
import com.example.enchere.repository.RencherirRepository;
import com.example.enchere.service.TokenService;
import com.example.enchere.value_object.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
public class CompteControlleur {

    private TokenService tokenService;
    private RechargeRepository rechargeRepository;
    private final RencherirRepository rencherirRepository;

    public CompteControlleur(RencherirRepository rencherirRepository) {
        this.rencherirRepository = rencherirRepository;
    }

    @PostMapping("membres/{id}/comptes")
    public ResponseEntity<Message> save(@RequestBody Compte compte, HttpServletRequest request, @PathVariable Long id) throws Exception {
        Authenticable authenticable = tokenService.validate(request, id);
        compte.setMembre(new Membre(authenticable.getId()));
        compte.rechargerCompte();
        return new ResponseEntity<>(new Message("Recharged"), HttpStatus.CREATED);
    }

    @GetMapping("/membres/{id}/comptes")
    public ResponseEntity<Compte> findByMemberId(@PathVariable String id) throws Exception {
        Compte compte = new Compte();
        compte.identificationSoldeActuel(id);
        return ResponseEntity.ok(compte);
    }

    @GetMapping("/membres/{id}/recharges")
    public ResponseEntity<List<Recharge>> findRechargeHistoryByMembre(@PathVariable Long id, HttpServletRequest request){
        Authenticable authenticable = tokenService.validate(request, id);
        return ResponseEntity.ok(rechargeRepository.findAllByMembreOrderByDatesDesc(authenticable.getId().intValue()));
    }

    @Autowired
    public void setRechargeRepository(RechargeRepository rechargeRepository) {
        this.rechargeRepository = rechargeRepository;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
