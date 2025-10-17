package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Model.Lavoro;
import com.example.InfoTirocini.Model.Candidatura;
import com.example.InfoTirocini.Service.LavoroService;
import com.example.InfoTirocini.Service.CandidaturaService;
import com.example.InfoTirocini.dto.LavoroDTO;
import com.example.InfoTirocini.dto.CandidaturaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lavori")
@CrossOrigin(origins = "*")
public class LavoroController {

	@Autowired
	private LavoroService lavoroService;

	@Autowired
	private CandidaturaService candidaturaService;

	@GetMapping
	public ResponseEntity<List<Lavoro>> tuttiILavori() {
		List<Lavoro> lavori = lavoroService.tuttiILavori();
		return ResponseEntity.ok(lavori);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> dettaglioLavoro(@PathVariable Integer id) {
		try {
			Lavoro lavoro = lavoroService.trovaPerID(id);
			return ResponseEntity.ok(lavoro);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<?> creaLavoro(@Valid @RequestBody LavoroDTO dto) {
		try {
			Lavoro nuovo = lavoroService.creaLavoro(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuovo);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> modificaLavoro(@PathVariable Integer id, @Valid @RequestBody LavoroDTO dto) {
		try {
			Lavoro aggiornato = lavoroService.aggiornaLavoro(id, dto);
			return ResponseEntity.ok(aggiornato);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminaLavoro(@PathVariable Integer id) {
		try {
			lavoroService.eliminaLavoro(id);
			return ResponseEntity.ok("Lavoro eliminato con successo!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/candidatura")
	public ResponseEntity<?> candidati(@Valid @RequestBody CandidaturaDTO dto) {
		try {
			Candidatura candidatura = candidaturaService.creaCandidatura(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(candidatura);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}/candidature")
	public ResponseEntity<?> candidaturePerLavoro(@PathVariable Integer id) {
		try {
			List<Candidatura> candidature = candidaturaService.candidaturePerLavoro(id);
			return ResponseEntity.ok(candidature);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/count")
	public ResponseEntity<Long> contaLavori() {
		long count = lavoroService.contaLavori();
		return ResponseEntity.ok(count);
	}
}